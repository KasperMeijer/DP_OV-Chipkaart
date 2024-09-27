package nl.hu.dp.data;

import nl.hu.dp.domain.Adres;
import nl.hu.dp.domain.OVChipkaart;
import nl.hu.dp.domain.Product;
import nl.hu.dp.domain.Reiziger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOsql implements ProductDAO{
    private Connection connection;
    private OVChipkaartDAO OVChipkaartDAO;

    public ProductDAOsql(Connection conn, OVChipkaartDAO ovdao){
        this.connection = conn;
        OVChipkaartDAO = ovdao;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        if(findById(product.getProduct_nummer()) == null){
            try(PreparedStatement pst = connection.prepareStatement(
                    "INSERT INTO product (product_nummer, naam, beschrijving, prijs) " +
                            "VALUES (?, ?, ?, ?)")){
                pst.setInt(1, product.getProduct_nummer());
                pst.setString(2, product.getNaam());
                pst.setString(3, product.getBeschrijving());
                pst.setDouble(4, product.getPrijs());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }

        // Call save method in OVChipkaartDAOsql
        for(OVChipkaart ovChipkaart : product.getOvChipkaarten()){
            OVChipkaartDAO.save(ovChipkaart);
        }

        return true;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        if(findById(product.getProduct_nummer()) != null){
            try(PreparedStatement pst = connection.prepareStatement(
                    "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?")){
                pst.setString(1, product.getNaam());
                pst.setString(2, product.getBeschrijving());
                pst.setDouble(3, product.getPrijs());
                pst.setInt(4, product.getProduct_nummer());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }

            // Update link
            try(PreparedStatement pst = connection.prepareStatement(
                    "UPDATE ov_chipkaart_product SET status = ?, last_update = ? WHERE product_nummer = ?")){
                pst.setString(1, "actief");
                pst.setDate(2, Date.valueOf(LocalDate.now()));
                pst.setInt(3, product.getProduct_nummer());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        if(findById(product.getProduct_nummer()) != null){
            // Delete link
            try(PreparedStatement pst = connection.prepareStatement(
                    "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?")){
                pst.setInt(1, product.getProduct_nummer());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }

            // Delete product
            try(PreparedStatement pst = connection.prepareStatement(
                    "DELETE FROM product WHERE product_nummer = ?")){
                pst.setInt(1, product.getProduct_nummer());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
            return true;
        }
        return false;
    }

    @Override
    public Product findById(int id) throws SQLException {
        String q = "SELECT * FROM product WHERE product_nummer = ?";
        try(PreparedStatement pst = connection.prepareStatement(q)){
            pst.setInt(1, id);
            try(ResultSet rs = pst.executeQuery()){
                if (rs != null && rs.next()){
                    return new Product(
                            rs.getInt("product_nummer"),
                            rs.getString("naam"),
                            rs.getString("beschrijving"),
                            rs.getDouble("prijs")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        String q = "SELECT * FROM product p " +
                "JOIN ov_chipkaart_product ocp ON p.product_nummer = ocp.product_nummer " +
                "WHERE ocp.kaart_nummer = ?";
        List<Product> list = new ArrayList<>();
        try(PreparedStatement pst = connection.prepareStatement(q)){
            pst.setInt(1, ovChipkaart.getKaart_nummer());
            try(ResultSet rs = pst.executeQuery()){
                while (rs != null && rs.next()){
                    list.add(new Product(
                            rs.getInt("product_nummer"),
                            rs.getString("naam"),
                            rs.getString("beschrijving"),
                            rs.getDouble("prijs")
                    ));
                }
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Product> findAll() throws SQLException {
        String q = "SELECT * FROM product";
        try(PreparedStatement pst = connection.prepareStatement(q);
            ResultSet rs = pst.executeQuery()){
            List<Product> list = new ArrayList<>();
            while (rs != null && rs.next()){
                list.add(new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs")
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
