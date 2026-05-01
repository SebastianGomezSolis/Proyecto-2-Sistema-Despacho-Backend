package una.sistema.backend.proyecto2sistemadespachobackend.datos;

import una.sistema.backend.proyecto2sistemadespachobackend.model.Administrador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDatos {
    public List<Administrador> findAll() throws SQLException {
        String sql = "SELECT * FROM administrador ORDER BY id";

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Administrador> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Administrador(
                        rs.getInt("id"),
                        rs.getString("identificacion"),
                        rs.getString("clave")
                ));
            }
            return list;
        }
    }

    public Administrador findById(int id) throws SQLException {
        String sql = "Select * from administrador WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Administrador encontrado = null;
            if (rs.next()) {
                encontrado = new Administrador(
                        rs.getInt("id"),
                        rs.getString("identificacion"),
                        rs.getString("clave")
                );
            }
            return encontrado;
        }
    }

    public Administrador findByIdentificacion(String identificacion) throws SQLException {
        String sql = "SELECT * FROM administrador WHERE identificacion = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Administrador(
                            rs.getInt("id"),
                            rs.getString("identificacion"),
                            rs.getString("clave")
                    );
                }
                return null;
            }
        }
    }

    public Administrador insert(Administrador administrador) throws SQLException {
        String sql = "INSERT INTO administrador (identificacion, clave) VALUES (?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, administrador.getIdentificacion());
            ps.setString(2, administrador.getClave());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return administrador;
                }
            }
            return null;
        }
    }

    public Administrador update(Administrador administrador) throws SQLException {
        String sql = "UPDATE administrador set identificacion = ?, clave = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, administrador.getIdentificacion());
            ps.setString(2, administrador.getClave());
            ps.setInt(3, administrador.getId());
            if (ps.executeUpdate() > 0) {
                return administrador;
            } else {
                return null;
            }
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM administrador WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }
}
