package com.github.corviv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

@Listeners(ListenerLogger.class)
public class JDBCUtils {

    static Connection conn = null;
    public static String defUrl = "jdbc:postgresql://127.0.0.1:5432/";

    private static final Logger logger = LoggerFactory.getLogger("JDBCUtils");

    public static boolean connectDB(String url, String user, String pass) {

        try {
            conn = DriverManager.getConnection(url, user, pass);

            if (conn == null) {
                logger.debug("Failed to connect to the '{}'!", url);
                return false;
            }
            logger.debug("Connected to the '{}'!", url);
            return true;

        } catch (SQLException e) {
            logger.error("SQL State: {}\n{}", e.getSQLState(), e.getMessage());
        }

        return false;
    }

    public static boolean checkDBExists(String dbName){

        try {
            ResultSet resultSet = conn.getMetaData().getCatalogs();

            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if(databaseName.equals(dbName)){
                    return true;
                }
            }
            resultSet.close();
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return false;
    }
}
