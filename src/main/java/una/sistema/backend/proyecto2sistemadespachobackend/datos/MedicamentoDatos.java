package una.sistema.backend.proyecto2sistemadespachobackend.datos;

import una.sistema.backend.proyecto2sistemadespachobackend.model.Medicamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDatos {
    public List<Medicamento> findAll() throws SQLException {
        String sql = "SELECT * FROM medicamento ORDER BY id";

        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Medicamento> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Medicamento(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
            return list;
        }
    }

    public Medicamento findById(int id) throws SQLException {
        String sql = "Select * from medicamento WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Medicamento encontrado = null;
            if (rs.next()) {
                encontrado = new Medicamento(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
            }
            return encontrado;
        }
    }

    public Medicamento findByCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM medicamento WHERE codigo = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medicamento(
                            rs.getInt("id"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    );
                }
                return null;
            }
        }
    }

    public Medicamento insert(Medicamento medicamento) throws SQLException {
        String sql = "INSERT INTO medicamento (codigo, nombre, descripcion) VALUES (?, ?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, medicamento.getCodigo());
            ps.setString(2, medicamento.getNombre());
            ps.setString(3, medicamento.getDescripcion());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return medicamento;
                }
            }
            return null;
        }
    }

    public Medicamento update(Medicamento medicamento) throws SQLException {
        String sql = "UPDATE medicamento set codigo = ?, nombre = ?, descripcion = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, medicamento.getCodigo());
            ps.setString(2, medicamento.getNombre());
            ps.setString(3, medicamento.getDescripcion());
            ps.setInt(4, medicamento.getId());
            if (ps.executeUpdate() > 0) {
                return medicamento;
            } else {
                return null;
            }
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM medicamento WHERE id = " + id;
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }

}
