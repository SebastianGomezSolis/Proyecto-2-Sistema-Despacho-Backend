package una.sistema.backend.proyecto2sistemadespachobackend.datos;

import una.sistema.backend.proyecto2sistemadespachobackend.model.Medicamento;
import una.sistema.backend.proyecto2sistemadespachobackend.model.RecetaDetalle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaDetalleDatos {
    public List<RecetaDetalle> findAll() throws SQLException {
        String sql = "SELECT * FROM recetadetalle ORDER BY id";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<RecetaDetalle> list = new ArrayList<>();
            MedicamentoDatos medDAO = new MedicamentoDatos();

            while (rs.next()) {
                int medId = rs.getInt("medicamentoId");
                Medicamento med = medDAO.findById(medId);

                list.add(new RecetaDetalle(
                        rs.getInt("id"),
                        med,
                        rs.getInt("cantidad"),
                        rs.getString("indicaciones"),
                        rs.getInt("diasDuracion")
                ));
            }
            return list;
        }
    }

    public RecetaDetalle findById(int id) throws SQLException {
        String sql = "SELECT * FROM recetadetalle WHERE id = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                int medId = rs.getInt("medicamentoId");
                MedicamentoDatos medDAO = new MedicamentoDatos();
                Medicamento med = medDAO.findById(medId);

                return new RecetaDetalle(
                        rs.getInt("id"),
                        med,
                        rs.getInt("cantidad"),
                        rs.getString("indicaciones"),
                        rs.getInt("diasDuracion")
                );
            }
        }
    }

    public RecetaDetalle insert(RecetaDetalle d) throws SQLException {
        String sql = "INSERT INTO recetadetalle (medicamentoId, cantidad, indicaciones, diasDuracion) VALUES (?, ?, ?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, d.getMedicamento().getId());
            ps.setInt(2, d.getCantidad());
            ps.setString(3, d.getIndicaciones());
            ps.setInt(4, d.getDiasDuracion());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) d.setId(keys.getInt(1));
            }
            return d;
        }
    }

    public RecetaDetalle update(RecetaDetalle d) throws SQLException {
        String sql = "UPDATE recetadetalle SET medicamentoId = ?, cantidad = ?, indicaciones = ?, diasDuracion = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, d.getMedicamento().getId());
            ps.setInt(2, d.getCantidad());
            ps.setString(3, d.getIndicaciones());
            ps.setInt(4, d.getDiasDuracion());
            ps.setInt(5, d.getId());

            return (ps.executeUpdate() > 0) ? d : null;
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM recetadetalle WHERE id = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
}

