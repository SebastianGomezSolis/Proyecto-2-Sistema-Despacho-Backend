package una.sistema.backend.proyecto2sistemadespachobackend.datos;

import una.sistema.backend.proyecto2sistemadespachobackend.model.Farmaceuta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FarmaceutaDatos {
    public List<Farmaceuta> findAll() throws SQLException {
        String sql = "SELECT * FROM farmaceuta ORDER BY id";

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Farmaceuta> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Farmaceuta(
                        rs.getInt("id"),
                        rs.getString("identificacion"),
                        rs.getString("clave"),
                        rs.getString("nombre")
                ));
            }
            return list;
        }
    }

    public Farmaceuta findById(int id) throws SQLException {
        String sql = "Select * from farmaceuta WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Farmaceuta encontrado = null;
            if (rs.next()) {
                encontrado = new Farmaceuta(
                        rs.getInt("id"),
                        rs.getString("identificacion"),
                        rs.getString("clave"),
                        rs.getString("nombre")
                );
            }
            return encontrado;
        }
    }

    public Farmaceuta findByIdentificacion(String identificacion) throws SQLException {
        String sql = "SELECT * FROM farmaceuta WHERE identificacion = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Farmaceuta(
                            rs.getInt("id"),
                            rs.getString("identificacion"),
                            rs.getString("clave"),
                            rs.getString("nombre")
                    );
                }
                return null;
            }
        }
    }

    public Farmaceuta insert(Farmaceuta administrador) throws SQLException {
        String sql = "INSERT INTO farmaceuta (identificacion, clave, nombre) VALUES (?, ?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, administrador.getIdentificacion());
            ps.setString(2, administrador.getClave());
            ps.setString(3, administrador.getNombre());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return administrador;
                }
            }
            return null;
        }
    }

    public Farmaceuta update(Farmaceuta administrador) throws SQLException {
        String sql = "UPDATE farmaceuta set identificacion = ?, clave = ?, nombre = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, administrador.getIdentificacion());
            ps.setString(2, administrador.getClave());
            ps.setString(3, administrador.getNombre());
            ps.setInt(4, administrador.getId());
            if (ps.executeUpdate() > 0) {
                return administrador;
            } else {
                return null;
            }
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM farmaceuta WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }
}
