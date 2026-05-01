package una.sistema.backend.proyecto2sistemadespachobackend.datos;

import una.sistema.backend.proyecto2sistemadespachobackend.model.Paciente;
import una.sistema.backend.proyecto2sistemadespachobackend.model.Receta;
import una.sistema.backend.proyecto2sistemadespachobackend.model.RecetaDetalle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaDatos {
    public List<Receta> findAll() throws SQLException {
        String sql = "SELECT * FROM receta ORDER BY id";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Receta> list = new ArrayList<>();
            PacienteDatos pacDAO = new PacienteDatos();
            RecetaDetalleDatos detDAO = new RecetaDetalleDatos();

            while (rs.next()) {
                int idPac = rs.getInt("pacienteId");
                int idDet = rs.getInt("recetaDetalleId");

                Paciente p = pacDAO.findById(idPac);
                RecetaDetalle d = detDAO.findById(idDet);

                Receta r = new Receta();
                r.setId(rs.getInt("id"));
                r.setIdentificacion(rs.getString("identificacion"));
                r.setPaciente(p);
                r.setFechaEntrega(rs.getDate("fechaEntrega").toLocalDate());
                r.setEstado(rs.getString("estado"));
                r.setDetalles(d);

                list.add(r);
            }
            return list;
        }
    }

    public Receta findById(int id) throws SQLException {
        String sql = "SELECT * FROM receta WHERE id = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                int idPac = rs.getInt("pacienteId");
                int idDet = rs.getInt("recetaDetalleId");

                PacienteDatos      pacDAO = new PacienteDatos();
                RecetaDetalleDatos detDAO = new RecetaDetalleDatos();

                Paciente p = pacDAO.findById(idPac);
                RecetaDetalle d = detDAO.findById(idDet);

                Receta r = new Receta();
                r.setId(rs.getInt("id"));
                r.setIdentificacion(rs.getString("identificacion"));
                r.setPaciente(p);
                r.setFechaEntrega(rs.getDate("fechaEntrega").toLocalDate());
                r.setEstado(rs.getString("estado"));
                r.setDetalles(d);
                return r;
            }
        }
    }

    public Receta insert(Receta r) throws SQLException {
        String sql = "INSERT INTO receta (identificacion, pacienteId, fechaEntrega, recetaDetalleId, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getIdentificacion());
            ps.setInt(2, r.getPaciente().getId());
            ps.setDate(3, r.getFechaEntrega() == null ? null : Date.valueOf(r.getFechaEntrega()));
            ps.setInt(4, r.getDetalles().getId());
            ps.setString(5, r.getEstado());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setId(keys.getInt(1));
            }

            // Autogenerar 'identificacion' si viene vacía
            if (r.getIdentificacion() == null || r.getIdentificacion().isBlank()) {
                String SQL_NEXT = "SELECT COALESCE(MAX(CAST(SUBSTRING(identificacion, 4) AS UNSIGNED)), 0) + 1 FROM receta WHERE identificacion LIKE 'REC%'";
                int next = 1;
                try (PreparedStatement psn = cn.prepareStatement(SQL_NEXT);
                     ResultSet rs = psn.executeQuery()) {
                    if (rs.next()) next = rs.getInt(1);
                }
                String ident = "REC" + String.format("%03d", next);
                try (PreparedStatement ps2 = cn.prepareStatement("UPDATE receta SET identificacion=? WHERE id=?")) {
                    ps2.setString(1, ident);
                    ps2.setInt(2, r.getId());
                    ps2.executeUpdate();
                }
                r.setIdentificacion(ident);
            }
            return r;
        }
    }

    public Receta update(Receta r) throws SQLException {
        String sql = "UPDATE receta SET identificacion = ?, pacienteId = ?, fechaEntrega = ?, recetaDetalleId = ?, estado = ? WHERE id = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, r.getIdentificacion());
            ps.setInt(2, r.getPaciente().getId());
            ps.setDate(3, r.getFechaEntrega() == null ? null : Date.valueOf(r.getFechaEntrega()));
            ps.setInt(4, r.getDetalles().getId());
            ps.setString(5, r.getEstado());
            ps.setInt(6, r.getId());

            return (ps.executeUpdate() > 0) ? r : null;
        }
    }

    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM receta WHERE id = ?";
        try (Connection cn = DataBase.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
}
