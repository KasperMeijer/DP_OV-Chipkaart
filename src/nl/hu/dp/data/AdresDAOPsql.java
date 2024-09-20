package nl.hu.dp.data;

import nl.hu.dp.domain.Adres;
import nl.hu.dp.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO{
    private Connection connection;

    private ReizigerDAO ReizigerDAOPsql;

    public AdresDAOPsql(Connection conn){
        this.connection = conn;
        this.ReizigerDAOPsql = new ReizigerDAOPsql(conn);
    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        if(findById(adres.getId()) == null) {
            String q = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try(PreparedStatement pst = connection.prepareStatement(q)){
                pst.setInt(1, adres.getId());
                pst.setString(2, adres.getPostcode());
                pst.setString(3, adres.getHuisnummer());
                pst.setString(4, adres.getStraat());
                pst.setString(5, adres.getWoonplaats());
                pst.setInt(6, adres.getReiziger().getId());
                pst.executeQuery();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        if(findById(adres.getId()) != null) {
            String q = "UPDATE adres SET adres_id = ?, postcode = ?, " +
                    "huisnummer = ?, straat = ? , woonplaats = ?, reiziger_id = ? WHERE adres_id = ?";
            try(PreparedStatement pst = connection.prepareStatement(q)){
                pst.setInt(1, adres.getId());
                pst.setString(2, adres.getPostcode());
                pst.setString(3, adres.getHuisnummer());
                pst.setString(4, adres.getStraat());
                pst.setString(5, adres.getWoonplaats());
                pst.setInt(6, adres.getReiziger().getId());
                pst.setInt(7, adres.getId());
                pst.executeUpdate();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        if(findById(adres.getId()) != null) {
            String q = "DELETE FROM adres WHERE adres_id = ?";
            try(PreparedStatement pst = connection.prepareStatement(q)){
                pst.setInt(1, adres.getId());
                pst.executeUpdate();
                return true;
            }
        }
        return false;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
            String q = "SELECT * FROM adres WHERE reiziger_id = ?";
            try(PreparedStatement pst = connection.prepareStatement(q);) {
                pst.setInt(1, reiziger.getId());
                try(ResultSet rs = pst.executeQuery()){
                    if (rs != null && rs.next()) {
                        return new Adres(
                                rs.getInt("adres_id"),
                                rs.getString("postcode"),
                                rs.getString("huisnummer"),
                                rs.getString("straat"),
                                rs.getString("woonplaats"),
                                reiziger
                        );
                    }
                }
            }
            return null;
    }

    @Override
    public Adres findById(int id) throws SQLException {
        String q = "SELECT * FROM adres WHERE adres_id = ?";

        try(PreparedStatement pst = connection.prepareStatement(q)){
            pst.setInt(1, id);
            try(ResultSet rs = pst.executeQuery()){
                if(rs != null && rs.next()){
                    return new Adres(
                            rs.getInt("adres_id"),
                            rs.getString("postcode"),
                            rs.getString("huisnummer"),
                            rs.getString("straat"),
                            rs.getString("woonplaats"),
                            ReizigerDAOPsql.findById(rs.getInt("reiziger_id"))
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        String q = "SELECT * FROM adres";
        try(Statement st = connection.createStatement()){
            try(ResultSet rs = st.executeQuery(q)){
                List<Adres> list = new ArrayList<>();

                while (rs != null && rs.next()) {
                    list.add(new Adres(
                            rs.getInt("adres_id"),
                            rs.getString("postcode"),
                            rs.getString("huisnummer"),
                            rs.getString("straat"),
                            rs.getString("woonplaats"),
                            ReizigerDAOPsql.findById(rs.getInt("reiziger_id"))
                    ));
                }
                return list;
            }
        }
    }
}
