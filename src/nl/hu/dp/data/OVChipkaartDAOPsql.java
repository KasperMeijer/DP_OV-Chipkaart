package nl.hu.dp.data;

import nl.hu.dp.domain.Adres;
import nl.hu.dp.domain.OVChipkaart;
import nl.hu.dp.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection connection;

    private ReizigerDAO ReizigerDAOPsql;

    public OVChipkaartDAOPsql(Connection conn){
        this.connection = conn;
        this.ReizigerDAOPsql = new ReizigerDAOPsql(conn);
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        if(findById(ovChipkaart.getKaart_nummer()) == null) {
            String q = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try(PreparedStatement pst = connection.prepareStatement(q)){
                pst.setInt(1, ovChipkaart.getKaart_nummer());
                pst.setDate(2, Date.valueOf(ovChipkaart.getGeldig_tot()));
                pst.setInt(3, ovChipkaart.getKlasse());
                pst.setDouble(4, ovChipkaart.getSaldo());
                pst.setInt(5, ovChipkaart.getReiziger().getId());
                pst.executeQuery();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        if(findById(ovChipkaart.getKaart_nummer()) != null) {
            String q = "UPDATE ov_chipkaart SET kaart_nummer= ?, geldig_tot = ?, " +
                    "klasse = ?, saldo = ? , reiziger_id = ? WHERE kaart_nummer = ?";
            try(PreparedStatement pst = connection.prepareStatement(q)){
                pst.setInt(1, ovChipkaart.getKaart_nummer());
                pst.setDate(2, Date.valueOf(ovChipkaart.getGeldig_tot()));
                pst.setInt(3, ovChipkaart.getKlasse());
                pst.setDouble(4, ovChipkaart.getSaldo());
                pst.setInt(5, ovChipkaart.getReiziger().getId());
                pst.setInt(6, ovChipkaart.getKaart_nummer());
                pst.executeUpdate();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        if(findById(ovChipkaart.getKaart_nummer()) != null) {
            String q = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
            try(PreparedStatement pst = connection.prepareStatement(q)){
                pst.setInt(1, ovChipkaart.getKaart_nummer());
                pst.executeUpdate();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String q = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
        try(PreparedStatement pst = connection.prepareStatement(q);) {
            pst.setInt(1, reiziger.getId());
            try(ResultSet rs = pst.executeQuery()){
                List<OVChipkaart> list = new ArrayList<>();
                while (rs != null && rs.next()) {
                    list.add(new OVChipkaart(
                            rs.getInt("kaart_nummer"),
                            rs.getDate("geldig_tot").toLocalDate(),
                            rs.getInt("klasse"),
                            rs.getDouble("saldo"),
                            reiziger
                    ));
                }
                return list;
            }
        }
    }

    @Override
    public OVChipkaart findById(int id) throws SQLException {
        String q = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
        try(PreparedStatement pst = connection.prepareStatement(q)){
            pst.setInt(1, id);
            try(ResultSet rs = pst.executeQuery()){
                if(rs != null && rs.next()){
                    return new OVChipkaart(
                            rs.getInt("kaart_nummer"),
                            rs.getDate("geldig_tot").toLocalDate(),
                            rs.getInt("klasse"),
                            rs.getDouble("saldo"),
                            ReizigerDAOPsql.findById(rs.getInt("reiziger_id"))
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        String q = "SELECT * FROM ov_chipkaart";
        try(Statement st = connection.createStatement()){
            try(ResultSet rs = st.executeQuery(q)){
                List<OVChipkaart> list = new ArrayList<>();
                while (rs != null && rs.next()) {
                    list.add(new OVChipkaart(
                            rs.getInt("kaart_nummer"),
                            rs.getDate("geldig_tot").toLocalDate(),
                            rs.getInt("klasse"),
                            rs.getDouble("saldo"),
                            ReizigerDAOPsql.findById(rs.getInt("reiziger_id"))
                    ));
                }
                return list;
            }
        }
    }
}
