package vs.products.iohandler.database;

import vs.products.SQLHandableScannedProduct;
import vs.products.ScannedProduct;
import vs.products.factory.ScannedProductFactory;
import vs.products.iohandler.ProductIOHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class implements the ProductIOHandler to read and write ScannedProduct's
 * to the concrete implemented database.
 */
public class ProductDatabaseHandler implements ProductIOHandler {
    public final static String PRODUCT_TABLE = "Product";
    public final static String PRODUCT_TABLE_PK_PRODUCTNAME = "productName";
    public final static String PRODUCT_TABLE_PK_PRODUCTNAME_COMPLETE = PRODUCT_TABLE + "." + PRODUCT_TABLE_PK_PRODUCTNAME;
    public final static String PRODUCT_TABLE_PRODUCTUNIT = "productUnit";
    public final static String PRODUCT_TABLE_PRODUCTUNIT_COMPLETE = PRODUCT_TABLE + "." + PRODUCT_TABLE_PRODUCTUNIT;


    public final static String SCANNEDPRODUCT_TABLE = "ScannedProduct";
    public final static String SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME = PRODUCT_TABLE_PK_PRODUCTNAME;
    public final static String SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME_COMPLETE = SCANNEDPRODUCT_TABLE + "." + SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME;
    public final static String SCANNEDPRODUCT_TABLE_PK_TIMESTAMP = "timeStamp";
    public final static String SCANNEDPRODUCT_TABLE_PK_TIMESTAMP_COMPLETE = SCANNEDPRODUCT_TABLE + "." + SCANNEDPRODUCT_TABLE_PK_TIMESTAMP;
    public final static String SCANNEDPRODUCT_TABLE_AMMOUNT = "ammount";
    public final static String SCANNEDPRODUCT_TABLE_AMMOUNT_COMPLETE = SCANNEDPRODUCT_TABLE + "." + SCANNEDPRODUCT_TABLE_AMMOUNT;

    public final static String BILL_TABLE = "Bill";
    public final static String BILL_TABLE_PK_TIMESTAMP = "timestamp";
    public final static String BILL_TABLE_PK_TIMESTAMP_COMPLETE = BILL_TABLE + "." + BILL_TABLE_PK_TIMESTAMP;
    public final static String BILL_TABLE_SHOPNAME = "shopName";
    public final static String BILL_TABLE_SHOPNAME_COMPLETE = BILL_TABLE + "." + BILL_TABLE_SHOPNAME;
    public final static String BILL_TABLE_PK_PRODUCTNAME = "productName";
    public final static String BILL_TABLE_PK_PRODUCTNAME_COMPLETE = BILL_TABLE + "." + BILL_TABLE_PK_PRODUCTNAME;
    public final static String BILL_TABLE_AMOUNT = "amount";
    public final static String BILL_TABLE_AMOUNT_COMPLETE = BILL_TABLE + "." + BILL_TABLE_AMOUNT;
    public final static String BILL_TABLE_PRICE = "price";
    public final static String BILL_TABLE_PRICE_COMPLETE = BILL_TABLE + "." + BILL_TABLE_PRICE;

    protected Connection connection = null;

    public ProductDatabaseHandler(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void write(ScannedProduct scannedProduct) {
        if (!this.isConnected()) {
            System.err.println("ERROR : Can't insert scanned product. Not connected to DB");
            return;
        }

        SQLHandableScannedProduct sqlHandableScannedProduct = new SQLHandableScannedProduct(scannedProduct);
        String sql = sqlHandableScannedProduct.getSQLInsertStatement();

        try {
            System.out.println("INFO : Write ScannedProduct to SQLite DB");
            executeStatements(sql);
        } catch (SQLException e) {
            System.err.println("ERROR : Insert scanned product Failed.");
            e.printStackTrace();
        }
    }

    @Override
    public List<ScannedProduct> read(String statement) {
        List<ScannedProduct> scannedProducts = new ArrayList<>();
        try {
            System.out.println("INFO : Read List<ScannedProduct> from SQLite DB");
            System.out.println(String.format("INFO : ExecuteQuery: %s", statement));
            Statement statement1 = this.connection.createStatement();
            ResultSet resultSet = statement1.executeQuery(statement);
            while (resultSet.next()) {
                scannedProducts.add(ScannedProductFactory.build(resultSet));
            }
        } catch (SQLException e) {
            System.err.println(String.format("ERROR : Read scanned vs.products failed: %s", statement));
            e.printStackTrace();
        }
        return scannedProducts;

    }

    /**
     * Checks if the there is a running connection to the db
     *
     * @return boolean
     * true --> connected
     * false --> not connected
     */
    public boolean isConnected() {
        try {
            return !(this.connection == null || !this.connection.isValid(10));
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Creates the necessary tables in the db if they don't exist
     */
    public void createTablesIfNotExist() {
        System.out.println("INFO : Create necessary tables if not existing");
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s VARCHAR(100), " +
                        "%s VARCHAR(100), " +
                        "PRIMARY KEY (%s));", PRODUCT_TABLE,
                PRODUCT_TABLE_PK_PRODUCTNAME,
                PRODUCT_TABLE_PRODUCTUNIT,
                PRODUCT_TABLE_PK_PRODUCTNAME);
        sql += String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s VARCHAR(100), " +
                        "%s DATETIME, " +
                        "%s VARCHAR(100), " +
                        "PRIMARY KEY (%s, %s), " +
                        "FOREIGN KEY (%s) REFERENCES %s(%s)); ", SCANNEDPRODUCT_TABLE,
                SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME,
                SCANNEDPRODUCT_TABLE_PK_TIMESTAMP,
                SCANNEDPRODUCT_TABLE_AMMOUNT,
                SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME, SCANNEDPRODUCT_TABLE_PK_TIMESTAMP,
                SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME, PRODUCT_TABLE, PRODUCT_TABLE_PK_PRODUCTNAME);
        sql += String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER, " +
                        "%s VARCHAR(100), " +
                        "%s VARCHAR(100), " +
                        "%s VARCHAR(100), " +
                        "%s INTEGER, " +
                        "FOREIGN KEY (%s) REFERENCES %s(%s) " +
                        "PRIMARY KEY (%s, %s));", BILL_TABLE,
                BILL_TABLE_PK_TIMESTAMP,
                BILL_TABLE_SHOPNAME,
                BILL_TABLE_PK_PRODUCTNAME,
                BILL_TABLE_AMOUNT,
                BILL_TABLE_PRICE,
                BILL_TABLE_PK_PRODUCTNAME, PRODUCT_TABLE, PRODUCT_TABLE_PK_PRODUCTNAME,
                BILL_TABLE_PK_TIMESTAMP, BILL_TABLE_PK_PRODUCTNAME);

        try {
            executeStatements(sql);
        } catch (SQLException e) {
            System.err.println("ERROR : Create tables if not exist failed");
            e.printStackTrace();
        }
    }

    protected void executeStatements(String statements) throws SQLException {
        Statement statement = this.connection.createStatement();
        String[] sqlStatements = statements.split(";");
        for (String sqlStatement : sqlStatements) {
            if (!sqlStatement.trim().isEmpty()) {
                sqlStatement = sqlStatement.trim();
                sqlStatement += ";";
                System.out.println(String.format("INFO : Execute Statement: %s", sqlStatement));
                statement.execute(sqlStatement);
            }
        }
    }
}
