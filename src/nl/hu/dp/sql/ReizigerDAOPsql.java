package nl.hu.dp.sql;

import nl.hu.dp.domain.Reiziger;
import nl.hu.dp.interfaces.ReizigerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection connection;

    public ReizigerDAOPsql(Connection con) {
        //Create connection to database
        this.connection = con;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        if(findById(reiziger.getId()) == null) {
            String q = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(q);
            pst.setInt(1, reiziger.getId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, reiziger.getGeboortedatum());
            ResultSet rs = pst.executeQuery();

            return true;
        }
        return false;
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        if(findById(reiziger.getId()) != null) {
            String q = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, " +
                    "achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
            PreparedStatement pst = connection.prepareStatement(q);
            pst.setString(1, reiziger.getVoorletters());
            pst.setString(2, reiziger.getTussenvoegsel());
            pst.setString(3, reiziger.getAchternaam());
            pst.setDate(4, reiziger.getGeboortedatum());
            pst.setInt(5, reiziger.getId());
            pst.executeUpdate();
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        if(findById(reiziger.getId()) != null) {
            String q = "DELETE FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = connection.prepareStatement(q);
            pst.setInt(1, reiziger.getId());
            pst.executeUpdate();
            return true;
        }
        return false;
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String q = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        while (rs != null && rs.next()){
            if(id == rs.getInt("reiziger_id")) {
                int reiziger_id = rs.getInt("reiziger_id");
                String voorletters = rs.getString("voorletters");
                String tussenvoegsel = "";
                if (rs.getString("tussenvoegsel") != null) {
                    tussenvoegsel = rs.getString("tussenvoegsel");
                }
                String achternaam = rs.getString("achternaam");
                java.sql.Date gbdatum = rs.getDate("geboortedatum");

                return new Reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, gbdatum);
            }
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) throws SQLException {
        String q = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        PreparedStatement pst = connection.prepareStatement(q);
        pst.setDate(1, date);
        ResultSet rs = pst.executeQuery();

        List<Reiziger> list = new ArrayList<>();

        while (rs != null && rs.next()){
            int id = rs.getInt("reiziger_id");
            String voorletters = rs.getString("voorletters");
            String tussenvoegsel = "";
            if(rs.getString("tussenvoegsel") != null){
                tussenvoegsel = rs.getString("tussenvoegsel");
            }
            String achternaam = rs.getString("achternaam");
            java.sql.Date gbdatum = rs.getDate("geboortedatum");

            Reiziger reiziger = new Reiziger(id, voorletters, tussenvoegsel, achternaam, gbdatum);
            list.add(reiziger);
        }
        return list;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        String q = "SELECT * FROM reiziger";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(q);

        List<Reiziger> list = new ArrayList<>();

        while (rs != null && rs.next()) {
            int id = rs.getInt("reiziger_id");
            String voorletters = rs.getString("voorletters");
            String tussenvoegsel = "";
            if(rs.getString("tussenvoegsel") != null){
                tussenvoegsel = rs.getString("tussenvoegsel");
            }
            String achternaam = rs.getString("achternaam");
            java.sql.Date gbdatum = rs.getDate("geboortedatum");

            Reiziger reiziger = new Reiziger(id, voorletters, tussenvoegsel, achternaam, gbdatum);
            list.add(reiziger);
        }
        return list;
    }
}
