package nl.hu.dp;

import java.sql.Connection;
import java.sql.*;

public class Main {
    private static Connection connection;

    public static void main(String[] args) {
        try {
            //Test connection function gebruiken om reizigers te printen in de console
            testConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Connection getConnection() throws SQLException {
        //CHANGE PASSWORD "PRIVATE" BELOW TO YOUR OWN BEFORE USING
        if (connection == null) {
            String url =
                    "jdbc:postgresql://localhost/ovchip?user=postgres&password=PRIVATE";
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }

    private static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    private static void testConnection() throws SQLException {
        //Open de connection to the database
        getConnection();

        //Normal statement instead of PreparedStatement
        String query = "SELECT * FROM reiziger;";
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(query);

        System.out.println("Alle Reizigers:");
        int x = 1;

        while (set != null && set.next()) {
            StringBuilder str = new StringBuilder();
            //#1:
            str.append("#");
            str.append(x);
            str.append(": ");

            //G.
            str.append(set.getString("voorletters"));
            str.append(". ");

            // van
            if(set.getString("tussenvoegsel") != null) {
                str.append(set.getString("tussenvoegsel"));
                str.append(" ");
            }

            // Rijn (2002-09-17)
            str.append(set.getString("achternaam"));
            str.append(" (");
            str.append(set.getString("geboortedatum"));
            str.append(")");

            System.out.println(str);
            x++;
        }

        //Close the connection to the database
        closeConnection();
    }
}
