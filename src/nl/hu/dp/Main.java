package nl.hu.dp;

import nl.hu.dp.data.*;
import nl.hu.dp.domain.Adres;
import nl.hu.dp.domain.OVChipkaart;
import nl.hu.dp.domain.Reiziger;

import java.sql.Connection;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    private static Connection connection;

    public static void main(String[] args) {
        try {
            getConnection();
            ReizigerDAO rdao = new ReizigerDAOPsql(connection);
            //testReizigerDAO(rdao);

            //AdresDAO adao = new AdresDAOPsql(connection);
            //testAdresDAO(adao, rdao);

            OVChipkaartDAO ovdao = new OVChipkaartDAOPsql(connection);
            testOVChipkaartDAO(ovdao, rdao);
            closeConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Connection getConnection() throws SQLException {
        //CHANGE PASSWORD "PRIVATE" BELOW TO YOUR OWN BEFORE USING
        if (connection == null) {
            String url =
                    "jdbc:postgresql://localhost/ovchip?user=postgres&password=Guppies@7";
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

    //Opdracht P2. ReizigerDAO
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : rdao.findAll()) {
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
        System.out.print("[Test] Eerst " + rdao.findAll().size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        System.out.println(rdao.findAll().size()  + " reizigers\n");

        // Delete een aangemaakte reiziger
        String gbdatum2 = "1981-03-14";
        Reiziger jan = new Reiziger(80, "J", "", "Kop", Date.valueOf(gbdatum2));
        rdao.save(jan);
        System.out.print("[Test] Eerst " + rdao.findAll().size()  + " reizigers");
        rdao.delete(jan);
        System.out.print(" en na de delete " + rdao.findAll().size()  + " reizigers\n");

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

        //Verwijder de reiziger die is aangemaakt voor de test
        rdao.delete(sietske);
        //Zet wijziging terug naar originele waarde
        gevondenGebruiker.setVoorletters("S");
    }

    //Opdracht P3. AdresDAO
    public static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException{
        System.out.println("\n---------- Test AdresDAO -------------");

        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        List<Adres> adressen = adao.findAll();
        for(Adres adres : adressen){
            System.out.println(adres);
        }

        System.out.println("[Test] AdresDAO.findById(1) geeft het volgende adres:");
        Adres adres = adao.findById(1);
        System.out.println(adres);

        System.out.println("[Test] AdresDAO.findByReiziger(Reiziger) geeft het volgende adres:");
        Reiziger reiziger = rdao.findById(2);
        Adres adresReiziger = adao.findByReiziger(reiziger);
        System.out.println(adresReiziger);

        System.out.println("[Test] AdresDAO.save()");
        Reiziger nieuweReiziger = new Reiziger(6, "J", "", "Kop", Date.valueOf("1981-03-14"));
        Adres nieuwAdres = new Adres(6, "1234AB", "1", "Straatweg", "Utrecht", nieuweReiziger);
        rdao.save(nieuweReiziger);
        adao.save(nieuwAdres);
        System.out.println("[Test] Opgeslagen adres:");
        System.out.println(adao.findById(6));

        System.out.println("[Test] AdresDAO.update()");
        Adres adresUpdate = adao.findById(6);
        adresUpdate.setPostcode("2314YE");
        adao.update(adresUpdate);
        System.out.println("[Test] Aangepast adres:");
        System.out.println(adao.findById(6));

        System.out.println("[Test] AdresDAO.delete()");
        Adres adresDelete = adao.findById(6);
        System.out.println(adresDelete);
        adao.delete(adresDelete);
        System.out.println("[Test] Verwijderd adres:");
        System.out.println(adao.findById(6));

        //Verijder de reiziger die is aangemaakt voor de test
        rdao.delete(nieuweReiziger);
    }

    //Opdracht P4. OVChipkaartDAO
    public static void testOVChipkaartDAO(OVChipkaartDAO ovdao, ReizigerDAO rdao) throws SQLException{
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende ov-chipkaarten:");
        List<OVChipkaart> ovChipkaarten = ovdao.findAll();
        for(OVChipkaart ovChipkaart : ovChipkaarten){
            System.out.println(ovChipkaart);
        }

        System.out.println("[Test] OVChipkaartDAO.findById(35283) geeft de volgende ov-chipkaart:");
        OVChipkaart ovChipkaart = ovdao.findById(35283);
        System.out.println(ovChipkaart);

        System.out.println("[Test] OVChipkaartDAO.findByReiziger(Reiziger) geeft de volgende ov-chipkaarten:");
        Reiziger reiziger = rdao.findById(2);
        List<OVChipkaart> ovChipkaartenReiziger = ovdao.findByReiziger(reiziger);
        for(OVChipkaart ovChipkaartReiziger : ovChipkaartenReiziger){
            System.out.println(ovChipkaartReiziger);
        }

        System.out.println("[Test] OVChipkaartDAO.save()");
        Reiziger nieuweReiziger = new Reiziger(6, "J", "", "Kop", Date.valueOf("1981-03-14"));
        rdao.save(nieuweReiziger);
        OVChipkaart nieuweOVChipkaart = new OVChipkaart(35284, LocalDate.of(2021, 2, 2), 2, 25.00, nieuweReiziger);
        ovdao.save(nieuweOVChipkaart);
        System.out.println("[Test] Opgeslagen ov-chipkaart:");
        System.out.println(ovdao.findById(35284));

        System.out.println("[Test] OVChipkaartDAO.update()");
        OVChipkaart ovChipkaartUpdate = ovdao.findById(35284);
        ovChipkaartUpdate.setSaldo(30.00);
        ovdao.update(ovChipkaartUpdate);
        System.out.println("[Test] Aangepaste ov-chipkaart:");
        System.out.println(ovdao.findById(35284));

        System.out.println("[Test] OVChipkaartDAO.delete()");
        OVChipkaart ovChipkaartDelete = ovdao.findById(35284);
        System.out.println(ovChipkaartDelete);
        ovdao.delete(ovChipkaartDelete);
        System.out.println("[Test] Verwijderde ov-chipkaart:");
        System.out.println(ovdao.findById(35284));
    }
}
