package vs.products;

import vs.products.iohandler.database.ProductDatabaseHandler;

/**
 * Wrapper class for a ScannedProduct to handle SQL Statements.
 */
public class SQLHandableScannedProduct extends ScannedProduct {

    public SQLHandableScannedProduct(ScannedProduct scannedProduct) {
        super(scannedProduct.getProduct());
        super.setTimeStamp(scannedProduct.getTimeStamp());
    }

    /**
     * Creates an SQL statement to insert a scannedProduct into the database.
     *
     * @return String sqlStatemnt
     */
    public String getSQLInsertStatement() {
        String sql = String.format("INSERT OR IGNORE INTO %s " +
                        "(%s, %s) " +
                        "VALUES ('%s', '%s'); ",
                ProductDatabaseHandler.PRODUCT_TABLE,
                ProductDatabaseHandler.PRODUCT_TABLE_PK_PRODUCTNAME, ProductDatabaseHandler.PRODUCT_TABLE_PRODUCTUNIT,
                super.getName(), super.getUnit());
        sql += String.format("INSERT INTO %s " +
                        "(%s, %s, %s) " +
                        "VALUES ('%s', '%s', '%s'); ",
                ProductDatabaseHandler.SCANNEDPRODUCT_TABLE,
                ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_TIMESTAMP, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_AMMOUNT,
                super.getName(), ScannedProduct.SCANNED_PRODUCT_DATE_FORMAT.format(super.getTimeStamp()), super.getAmmount());

        return sql;
    }
}
