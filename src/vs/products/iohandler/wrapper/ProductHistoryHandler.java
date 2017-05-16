package vs.products.iohandler.wrapper;

import vs.products.ScannedProduct;
import vs.products.iohandler.ProductIOHandler;
import vs.products.iohandler.database.ProductDatabaseHandler;

import java.util.List;

/**
 * Created by Frank on 27.04.2017.
 */
public class ProductHistoryHandler implements ProductIOHandler {
    private ProductIOHandler productIOHandler;

    public ProductHistoryHandler(ProductIOHandler productIOHandler) {
        this.productIOHandler = productIOHandler;
    }

    @Override
    public void write(ScannedProduct scannedProduct) {
        productIOHandler.write(scannedProduct);
    }

    @Override
    public List<ScannedProduct> read(String statement) {
        return this.productIOHandler.read(statement);
    }

    /**
     * Returns the history of a scanned product in a list ordered by the time stampt.
     *
     * @param productName
     * @return List<ScannedProduct>
     */
    public List<ScannedProduct> getProductHistoryASC(String productName) {
        String statement = String.format("SELECT %s, %s, %s, %s " +
                        "FROM %s INNER JOIN %S ON %s=%s " +
                        "WHERE %s='%s' " +
                        "ORDER BY %s ASC; ",
                ProductDatabaseHandler.PRODUCT_TABLE_PK_PRODUCTNAME_COMPLETE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_AMMOUNT_COMPLETE,
                ProductDatabaseHandler.PRODUCT_TABLE_PRODUCTUNIT_COMPLETE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_TIMESTAMP_COMPLETE,
                ProductDatabaseHandler.PRODUCT_TABLE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE,
                ProductDatabaseHandler.PRODUCT_TABLE_PK_PRODUCTNAME_COMPLETE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME_COMPLETE,
                ProductDatabaseHandler.PRODUCT_TABLE_PK_PRODUCTNAME_COMPLETE, productName,
                ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_TIMESTAMP_COMPLETE);
        return this.read(statement);
    }

    /**
     * Returns the history of a scanned product in a list ordered by the time stampt.
     *
     * @param productName
     * @return List<ScannedProduct>
     */
    public List<ScannedProduct> getProductHistoryDESC(String productName) {
        String statement = String.format("SELECT %s, %s, %s, %s " +
                        "FROM %s INNER JOIN %S ON %s=%s " +
                        "WHERE %s='%s' " +
                        "ORDER BY %s DESC; ",
                ProductDatabaseHandler.PRODUCT_TABLE_PK_PRODUCTNAME_COMPLETE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_AMMOUNT_COMPLETE,
                ProductDatabaseHandler.PRODUCT_TABLE_PRODUCTUNIT_COMPLETE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_TIMESTAMP_COMPLETE,
                ProductDatabaseHandler.PRODUCT_TABLE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE,
                ProductDatabaseHandler.PRODUCT_TABLE_PK_PRODUCTNAME_COMPLETE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME_COMPLETE,
                ProductDatabaseHandler.PRODUCT_TABLE_PK_PRODUCTNAME_COMPLETE, productName,
                ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_TIMESTAMP_COMPLETE);
        return this.read(statement);
    }
}
