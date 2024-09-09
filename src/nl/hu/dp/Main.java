package nl.hu.dp;

import nl.hu.dp.domain.Reiziger;
import nl.hu.dp.interfaces.ReizigerDAO;
import nl.hu.dp.sql.ReizigerDAOPsql;

import java.sql.Connection;
import java.sql.*;
import java.util.List;

public class Main {
    private static Connection connection;

    public static void main(String[] args) {
        try {
            getConnection();
            ReizigerDAO rdao = new ReizigerDAOPsql(connection);
            testReizigerDAO(rdao);
            closeConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Connection getConnection() throws SQLException {
        //CHANGE PASSWORD "PRIVATE" BELOW TO YOUR OWN BEFORE USING
        if (connection == null) {
            String url =
                    "jdbc:postgresql://localhost/ovchip?user=postgres&password=PASSWORD";
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

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Haal reizigers op bij geboortedatum
        List<Reiziger> geboorteReizigers = rdao.findByGbdatum(Date.valueOf("2002-09-17"));
        System.out.println("[Test] ReizigerDAO.findByGbDatum(2002-09-17) geeft de volgende reizigers:");
        for (Reiziger r : geboorteReizigers) {
            System.out.println(r);
        }
        System.out.println();

        //Haal reiziger op door id
        Reiziger reizigerId = rdao.findById(2);
        System.out.println("[Test] ReizigerDAO.findById(1) geeft de volgende reizigers:");
        System.out.println(reizigerId);
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Delete een aangemaakte reiziger
        String gbdatum2 = "1981-03-14";
        Reiziger jan = new Reiziger(80, "J", "", "Kop", Date.valueOf(gbdatum2));
        rdao.save(jan);
        reizigers = rdao.findAll();
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers");
        rdao.delete(jan);
        reizigers = rdao.findAll();
        System.out.print(" en na de delete " + reizigers.size() + " reizigers\n");

        //Update een bestaande reiziger
        System.out.println();
        System.out.print("[Test] Originele gebruiker");
        Reiziger gevondenGebruiker = rdao.findById(77);
        System.out.println();
        System.out.println(gevondenGebruiker);
        gevondenGebruiker.setVoorletters("B");
        rdao.update(gevondenGebruiker);
        System.out.print("[Test] Aangepaste gebruiker");
        gevondenGebruiker = rdao.findById(77);
        System.out.println();
        System.out.println(gevondenGebruiker);
    }
}
