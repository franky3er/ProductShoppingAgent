package vs.products;

import org.apache.thrift.TException;
import vs.products.billing.Bill;
import vs.products.iohandler.database.ProductDatabaseHandler;
import vs.products.iohandler.wrapper.ProductHistoryHandler;
import vs.products.refillinfo.ProductRefillInfo;
import vs.shopservice.ShopService;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Class to refill the fridge depending on the refill information of each product
 * and the cheapest price of the specified shops.
 */
public class ProductShoppingAgent {
    private Connection connection;
    private Map<String, ShopService.Client> shops;
    private List<ProductRefillInfo> productsRefillInfo;
    private String deliveryAddress;
    private long cheapestPrice;

    public ProductShoppingAgent(Connection connection, Map<String, ShopService.Client> clients,
                                List<ProductRefillInfo> productsRefillInfo, String deliveryAddress) {
        this.connection = connection;
        this.shops = clients;
        this.productsRefillInfo = productsRefillInfo;
        this.deliveryAddress = deliveryAddress;
    }

    public void refill() {
        System.out.println("INFO : Refill products");
        for (ProductRefillInfo productRefillInfo : productsRefillInfo) {
            refill(productRefillInfo);
        }
    }

    private void refill(ProductRefillInfo productRefillInfo) {
        System.out.println(
                String.format(
                        "INFO : Refill product: %s",
                        productRefillInfo.getProductName()
                )
        );
        ScannedProduct product = getLatestScannedProduct(productRefillInfo.getProductName());
        refillIfNeeded(product, productRefillInfo);
    }

    private ScannedProduct getLatestScannedProduct(String productName) {
        System.out.println(
                String.format(
                        "INFO : Get latest scanned product: %s from productDB",
                        productName
                )
        );
        ProductHistoryHandler historyHandler = new ProductHistoryHandler(
                new ProductDatabaseHandler(connection)
        );
        return historyHandler.getProductHistoryDESC(productName).get(0);
    }

    private void refillIfNeeded(ScannedProduct product, ProductRefillInfo productRefillInfo) {
        String refillAmount = calculateRefillAmount(product, productRefillInfo);
        if (refillAmount == null) {
            System.out.println(
                    String.format(
                            "INFO : No refill needed  Product: %s, Amount: %s, AmountMin: %s, AmountDesired: %s",
                            product.getName(), product.getAmmount(), productRefillInfo.getAmountMinimum(),
                            productRefillInfo.getAmountDesired()
                    ));
            return;
        }
        Map.Entry<String, ShopService.Client> cheapestShop = getCheapestShop(product, refillAmount);
        buyProduct(product, refillAmount, cheapestShop);
    }

    private String calculateRefillAmount(ScannedProduct product, ProductRefillInfo productRefillInfo) {
        System.out.println(
                String.format(
                        "INFO : calculate refill amount for product: %s, productAmount: %s, " +
                                "amountMin: %s, amountDesired: %s",
                        product.getName(), product.getAmmount(),
                        productRefillInfo.getAmountMinimum(), productRefillInfo.getAmountDesired()
                )
        );
        String refillAmount;
        if (product.getAmmount().contains(".")) {
            refillAmount = calculateRefillAmount(Double.parseDouble(product.getAmmount()),
                    Double.parseDouble(productRefillInfo.getAmountMinimum()),
                    Double.parseDouble(productRefillInfo.getAmountDesired()));
        } else {
            refillAmount = calculateRefillAmount(Integer.parseInt(product.getAmmount()),
                    Integer.parseInt(productRefillInfo.getAmountMinimum()),
                    Integer.parseInt(productRefillInfo.getAmountDesired()));
        }
        System.out.println(
                String.format(
                        "INFO : Calculated refill amount: %s",
                        refillAmount
                )
        );
        return refillAmount;
    }

    private String calculateRefillAmount(double amountProduct, double amountMin, double amountDesired) {
        if (amountProduct >= amountMin) {
            return null;
        }
        return Double.toString(amountDesired - amountProduct);
    }

    private String calculateRefillAmount(int amountProduct, int amountMin, int amountDesired) {
        if (amountProduct >= amountMin) {
            return null;
        }
        return Integer.toString(amountDesired - amountProduct);
    }

    private Map.Entry<String, ShopService.Client> getCheapestShop(ScannedProduct product, String refillAmount) {
        System.out.println(
                String.format(
                        "INFO : get cheapest shop for product: %s, amount: %s",
                        product.getName(), refillAmount
                ));
        Map.Entry<String, ShopService.Client> cheapestShop = null;
        cheapestPrice = -1;

        for (Map.Entry<String, ShopService.Client> currentShop : shops.entrySet()) {
            try {
                long currentPrice = currentShop.getValue().fetchProductPrice(product.getName(), refillAmount);
                if (currentPrice >= 0 && cheapestPrice == -1) {
                    cheapestPrice = currentPrice;
                    cheapestShop = currentShop;
                } else if (currentPrice >= 0 && currentPrice < cheapestPrice) {
                    cheapestPrice = currentPrice;
                    cheapestShop = currentShop;
                }
            } catch (TException e) {
                System.err.println("WARNING : Unable to access shop");
                e.printStackTrace();
            }
        }
        return cheapestShop;
    }

    private void buyProduct(ScannedProduct product, String amount, Map.Entry<String, ShopService.Client> shop) {
        System.out.println(
                String.format(
                        "INFO : Buy product: %s, amount: %s from shop",
                        product.getName(), amount
                )
        );
        try {
            if (shop != null) {
                if (shop.getValue().buyProduct(product.getName(), amount, deliveryAddress)) {
                    System.out.println(String.format(
                            "INFO : Buy product: %s, amount: %s Successful",
                            product.getName(), amount
                            )
                    );
                    Bill bill = new Bill(System.currentTimeMillis(), shop.getKey(), product.getName(), amount,
                            cheapestPrice);
                    bill.book(connection);
                } else {
                    System.err.println(String.format(
                            "INFO : Buy product: %s, amount: %s Failed",
                            product.getName(), amount
                            )
                    );
                }
            } else {
                System.err.println(String.format("WARNING : No shop found for product: %s, amount: %s",
                        product.getName(), amount));
            }
        } catch (TException e) {
            System.err.println(
                    "WARING : Unable to access shop"
            );
            e.printStackTrace();
        }
    }
}
