package vs.products.factory;

import vs.products.ScannedProduct;
import vs.products.iohandler.database.ProductDatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * This factory creates concrete ScannedProducts
 */
public class ScannedProductFactory {

    /**
     * Splits the given String by ';' and creates a new ScannedProduct object out of the information of the
     * splitted String
     *
     * @param scannedProductState
     * @return ScannedProduct
     */
    public static ScannedProduct build(String scannedProductState) {
        System.out.println(String.format("INFO : Create new ScannedProduct out of state: %s", scannedProductState));
        ScannedProduct scannedProduct = null;
        String[] scannedProdutStateDetails = scannedProductState.split(";");
        scannedProduct = new ScannedProduct(ProductFactory.build(scannedProdutStateDetails[0]));
        if (scannedProduct != null) {
            scannedProduct.setAmmount(scannedProdutStateDetails[1]);
            try {
                scannedProduct.setTimeStamp(
                        ScannedProduct.SCANNED_PRODUCT_DATE_FORMAT
                                .parse(scannedProdutStateDetails[3]));
            } catch (ParseException e) {
                System.err.println(String.format("ERROR : Could not parse date '%s'",
                        scannedProdutStateDetails[3]));
                e.printStackTrace();
            }
        }
        return scannedProduct;
    }

    /**
     * Creates a ScannedProduct Object out of the information in the ResultSet
     *
     * @param resultSet
     * @return ScannedProduct
     */
    public static ScannedProduct build(ResultSet resultSet) {
        System.out.println("INFO : Create new ScannedProduct out of ResultSet");
        ScannedProduct scannedProduct = null;
        try {
            String productName = resultSet.getString(ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME);
            scannedProduct = new ScannedProduct(ProductFactory.build(productName));

            String timeStamp = resultSet.getString(ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_TIMESTAMP);
            System.out.println(String.format("INFO : set time stamp to '%s'", timeStamp));
            scannedProduct.setTimeStamp(ScannedProduct.SCANNED_PRODUCT_DATE_FORMAT.parse(timeStamp));

            String ammount = resultSet.getString(ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_AMMOUNT);
            System.out.println(String.format("INFO : set ammount to '%s'", ammount));
            scannedProduct.setAmmount(ammount);
        } catch (SQLException e) {
            System.err.println("ERROR : Could not create scanned product from ResultSet");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("ERROR : Could not create scanned product from ResultSet - " +
                    "Parsing date failed");
        }
        return scannedProduct;
    }
}
