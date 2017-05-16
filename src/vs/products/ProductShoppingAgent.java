package vs.products;

import org.apache.thrift.TException;
import vs.products.iohandler.database.ProductDatabaseHandler;
import vs.products.iohandler.wrapper.ProductHistoryHandler;
import vs.products.refillinfo.ProductRefillInfo;
import vs.shopservice.ShopService;

import java.sql.Connection;
import java.util.List;

/**
 * Class to refill the fridge depending on the refill information of each product
 * and the cheapest price of the specified shops.
 */
public class ProductShoppingAgent {
    private Connection connection;
    private List<ShopService.Client> shops;
    private List<ProductRefillInfo> productsRefillInfo;

    public ProductShoppingAgent(Connection connection, List<ShopService.Client> clients, List<ProductRefillInfo> productsRefillInfo) {
        this.connection = connection;
        this.shops = clients;
        this.productsRefillInfo = productsRefillInfo;
    }

    public void refill() throws TException {
        for (ProductRefillInfo productRefillInfo : productsRefillInfo) {
            refill(productRefillInfo);
        }
    }

    private void refill(ProductRefillInfo productRefillInfo) throws TException {
        ScannedProduct product = getLatestScannedProduct(productRefillInfo.getProductName());
        refillIfNeeded(product, productRefillInfo);
    }

    private ScannedProduct getLatestScannedProduct(String productName) {
        ProductHistoryHandler historyHandler = new ProductHistoryHandler(
                new ProductDatabaseHandler(connection)
        );
        return historyHandler.getProductHistoryDESC(productName).get(0);
    }

    private void refillIfNeeded(ScannedProduct product, ProductRefillInfo productRefillInfo) throws TException {
        String refillAmount = calculateRefillAmount(product, productRefillInfo);
        if(refillAmount == null) {
            System.out.println(String.format("INFO : No refill needed  Product: %s, Amount: %s, AmountMin: %s, AmountDesired: %s",
                    product.getName(), product.getAmmount(), productRefillInfo.getAmountMinimum(),
                    productRefillInfo.getAmountDesired()));
            return;
        }
        ShopService.Client cheapestShop = getCheapestShop(product, refillAmount);
        if (cheapestShop != null) {
            cheapestShop.buyProduct(product.getName(), refillAmount);
        } else {
            System.err.println(String.format("WARNING : No shop found for product: %s, amount: %s",
                    product.getName(), refillAmount));
        }
    }

    private String calculateRefillAmount(ScannedProduct product, ProductRefillInfo productRefillInfo) {
        if (product.getAmmount().contains(".")) {
            return calculateRefillAmount(Double.parseDouble(product.getAmmount()),
                    Double.parseDouble(productRefillInfo.getAmountMinimum()),
                    Double.parseDouble(productRefillInfo.getAmountDesired()));
        } else {
            return calculateRefillAmount(Integer.parseInt(product.getAmmount()),
                    Integer.parseInt(productRefillInfo.getAmountMinimum()),
                    Integer.parseInt(productRefillInfo.getAmountDesired()));
        }
    }

    private String calculateRefillAmount(double amountProduct, double amountMin, double amountDesired) {
        if (amountDesired > amountMin) {
            return null;
        }
        return Double.toString(amountDesired - amountProduct);
    }

    private String calculateRefillAmount(int amountProduct, int amountMin, int amountDesired) {
        if (amountDesired > amountMin) {
            return null;
        }
        return Integer.toString(amountDesired - amountProduct);
    }

    private ShopService.Client getCheapestShop(ScannedProduct product, String refillAmount) throws TException {
        ShopService.Client cheapestShop = null;
        long cheapestPrice = -1;
        for (ShopService.Client currentShop : shops) {
            long currentPrice = currentShop.fetchProductPrice(product.getName(), refillAmount);
            if (currentPrice >= 0 && cheapestPrice == -1) {
                cheapestPrice = currentPrice;
                cheapestShop = currentShop;
            } else if (currentPrice >= 0 && currentPrice < cheapestPrice) {
                cheapestPrice = currentPrice;
                cheapestShop = currentShop;
            }
        }
        return cheapestShop;
    }
}
