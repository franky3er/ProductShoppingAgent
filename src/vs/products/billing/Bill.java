package vs.products.billing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by dev on 19.05.17.
 */
public class Bill {
    long timestamp;
    String shopName;
    String productName;
    String amount;
    long price;


    public Bill(long timestamp, String shopName, String productName, String amount, long price) {
        this.timestamp = timestamp;
        this.shopName = shopName;
        this.productName = productName;
        this.amount = amount;
        this.price = price;
    }

    public void book(Connection connection) {
        String sqlFormatString = "INSERT INTO Bill(timestamp, shopName, productName, amount, price) VALUES(%s,%s,%s,%s,%s)";
        String sqlStatement = String.format(sqlFormatString, "?", "?", "?", "?", "?");
        String sqlString = String.format(sqlFormatString, Long.toString(timestamp), shopName, productName, amount,
                Long.toString(price));
        try {
            System.out.println("INFO : Executing SQL Querry: " + sqlString);
            PreparedStatement prepStmt = connection.prepareStatement(sqlStatement);
            prepStmt.setLong(1, timestamp);
            prepStmt.setString(2, shopName);
            prepStmt.setString(3, productName);
            prepStmt.setString(4, amount);
            prepStmt.setLong(5, price);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("ERROR : Failed to execute SQL Querry: " + sqlString);
            e.printStackTrace();
        }
    }
}