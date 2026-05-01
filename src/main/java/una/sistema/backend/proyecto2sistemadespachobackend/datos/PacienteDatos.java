package una.sistema.backend.proyecto2sistemadespachobackend.datos;

import una.sistema.backend.proyecto2sistemadespachobackend.model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDatos {
    public List<Paciente> findAll() throws SQLException {
        String sql = "Select * from paciente ORDER BY id";

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Paciente> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Paciente(
                        rs.getInt("id"),
                        rs.getString("identificacion"),
                        rs.getString("nombre"),
                        rs.getDate("fechaNacimiento").toLocalDate(),
                        rs.getString("telefono")
                ));
            }
            return list;
        }
    }

    public Paciente findById(int id) throws SQLException {
        String sql = "Select * from paciente WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Paciente encontrado = null;
            if (rs.next()) {
                encontrado = new Paciente(
                        rs.getInt("id"),
                        rs.getString("identificacion"),
                        rs.getString("nombre"),
                        rs.getDate("fechaNacimiento").toLocalDate(),
                        rs.getString("telefono")
                );
            }
            return encontrado;
        }
    }

    public Paciente findByIdentificacion(String identificacion) throws SQLException {
        String sql = "SELECT * FROM paciente WHERE identificacion = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, identificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                            rs.getInt("id"),
                            rs.getString("identificacion"),
                            rs.getString("nombre"),
                            rs.getDate("fechaNacimiento").toLocalDate(),
                            rs.getString("telefono")
                    );
                }
                return null;
            }
        }
    }

    public Paciente insert(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente (identificacion, nombre, fechaNacimiento, telefono) VALUES (?, ?, ?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, paciente.getIdentificacion());
            ps.setString(2, paciente.getNombre());
            ps.setDate(3, Date.valueOf(paciente.getFechaNacimiento()));
            ps.setString(4, paciente.getTelefono());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return paciente;
                }
            }
            return null;
        }
    }

    public Paciente update(Paciente paciente) throws SQLException {
        String sql = "UPDATE paciente set identificacion = ?, nombre = ?, fechaNacimiento = ?, telefono = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, paciente.getIdentificacion());
            ps.setString(2, paciente.getNombre());
            ps.setDate(3, Date.valueOf(paciente.getFechaNacimiento()));
            ps.setString(4, paciente.getTelefono());
            ps.setInt(5, paciente.getId());
            if (ps.executeUpdate() > 0) {
                return paciente;
            } else {
                return null;
            }
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }

}
