package helper;

import java.sql.*;

public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static final String password = "Passw0rd!";
    public static Connection connection;

    public static void openConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("connection successful!");
        }
        catch (Exception e) {
            System.out.println("connection error: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("connection closed!");
        } catch (Exception e) {
            System.out.println("connection error: " + e.getMessage());
        }
    }

    public static boolean checkLogin(String userName, String password)
    {
        PreparedStatement statement;
        ResultSet res;
        boolean checkUser = false;
        String query = "SELECT * FROM `users` WHERE `User_Name` =?";

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, userName);

            res = statement.executeQuery();

            if(res.next())
            {
                if(password.equals(res.getString("Password"))) {
                    checkUser = true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("login error: " + ex);
        }
        return checkUser;
    }
}
