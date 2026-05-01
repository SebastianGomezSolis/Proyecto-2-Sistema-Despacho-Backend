package una.sistema.backend.proyecto2sistemadespachobackend.datos;

import una.sistema.backend.proyecto2sistemadespachobackend.model.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDatos {
    public List<Medico> findAll() throws SQLException {
        String sql = "SELECT * FROM medico ORDER BY id";

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Medico> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Medico(
                        rs.getInt("id"),
                        rs.getString("identificacion"),
                        rs.getString("clave"),
                        rs.getString("nombre"),
                        rs.getString("especialidad")
                ));
            }
            return list;
        }
    }

    public Medico findById(int id) throws SQLException {
        String sql = "Select * from medico WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Medico encontrado = null;
            if (rs.next()) {
                encontrado = new Medico(
                        rs.getInt("id"),
                        rs.getString("identificacion"),
                        rs.getString("clave"),
                        rs.getString("nombre"),
                        rs.getString("especialidad")
                );
            }
            return encontrado;
        }
    }

    public Medico findByIdentificacion(String identificacion) throws SQLException {
        String sql = "SELECT * FROM medico WHERE identificacion = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medico(
                            rs.getInt("id"),
                            rs.getString("identificacion"),
                            rs.getString("clave"),
                            rs.getString("nombre"),
                            rs.getString("especialidad")
                    );
                }
                return null;
            }
        }
    }

    public Medico insert(Medico administrador) throws SQLException {
        String sql = "INSERT INTO medico (identificacion, clave, nombre, especialidad) VALUES (?, ?, ?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, administrador.getIdentificacion());
            ps.setString(2, administrador.getClave());
            ps.setString(3, administrador.getNombre());
            ps.setString(4, administrador.getEspecialidad());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return administrador;
                }
            }
            return null;
        }
    }

    public Medico update(Medico administrador) throws SQLException {
        String sql = "UPDATE medico set identificacion = ?, clave = ?, nombre = ?, especialidad = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, administrador.getIdentificacion());
            ps.setString(2, administrador.getClave());
            ps.setString(3, administrador.getNombre());
            ps.setString(4, administrador.getEspecialidad());
            ps.setInt(5, administrador.getId());
            if (ps.executeUpdate() > 0) {
                return administrador;
            } else {
                return null;
            }
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM medico WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }
}