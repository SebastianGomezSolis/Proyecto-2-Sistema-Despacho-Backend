package una.sistema.backend.proyecto2sistemadespachobackend.logic;

import una.sistema.backend.proyecto2sistemadespachobackend.datos.PacienteDatos;
import una.sistema.backend.proyecto2sistemadespachobackend.datos.RecetaDatos;
import una.sistema.backend.proyecto2sistemadespachobackend.datos.RecetaDetalleDatos;
import una.sistema.backend.proyecto2sistemadespachobackend.model.Paciente;
import una.sistema.backend.proyecto2sistemadespachobackend.model.Receta;
import una.sistema.backend.proyecto2sistemadespachobackend.model.RecetaDetalle;

import java.sql.SQLException;
import java.util.List;

public class RecetaLogica {

    private final RecetaDatos store = new RecetaDatos();
    private final PacienteDatos pacStore = new PacienteDatos();
    private final RecetaDetalleDatos detStore = new RecetaDetalleDatos();

    // --------- Lectura ---------
    public List<Receta> findAll() throws SQLException {
        return store.findAll();
    }

    public Receta findById(int id) throws SQLException {
        return store.findById(id);
    }

    // --------- Escritura ---------
    public Receta create(Receta nueva) throws SQLException {
        validarNueva(nueva);

        Paciente p = pacStore.findById(nueva.getPaciente().getId());
        if (p == null)
            throw new IllegalArgumentException("El paciente no existe (id=" + nueva.getPaciente().getId() + ").");

        RecetaDetalle d = nueva.getDetalles();
        if (d.getId() == 0) {
            d = detStore.insert(d);
            nueva.setDetalles(d);
        } else {
            if (detStore.findById(d.getId()) == null)
                throw new IllegalArgumentException("El detalle de receta no existe (id=" + d.getId() + ").");
        }

        return store.insert(nueva);
    }


    public Receta update(Receta r) throws SQLException {
        if (r == null || r.getId() <= 0)
            throw new IllegalArgumentException("La receta a actualizar requiere un ID válido.");

        validarCampos(r);

        if (pacStore.findById(r.getPaciente().getId()) == null)
            throw new IllegalArgumentException("El paciente no existe (id=" + r.getPaciente().getId() + ").");
        if (detStore.findById(r.getDetalles().getId()) == null)
            throw new IllegalArgumentException("El detalle de receta no existe (id=" + r.getDetalles().getId() + ").");

        return store.update(r);
    }

    public boolean deleteById(int id) throws SQLException {
        if (id <= 0) return false;
        return store.delete(id) > 0;
    }

    // --------- Helpers ---------
    private void validarNueva(Receta r) {
        if (r == null) throw new IllegalArgumentException("Receta nula.");
        validarCampos(r);
    }

    private void validarCampos(Receta r) {
        if (r.getPaciente() == null) throw new IllegalArgumentException("El paciente es obligatorio.");
        if (r.getDetalles() == null) throw new IllegalArgumentException("El detalle de receta es obligatorio.");
        var d = r.getDetalles();
        if (d.getMedicamento() == null) throw new IllegalArgumentException("Debe seleccionar un medicamento.");
        if (d.getCantidad() <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        if (d.getDiasDuracion() <= 0) throw new IllegalArgumentException("Los días de duración deben ser mayores a cero.");
        if (d.getIndicaciones() == null || d.getIndicaciones().isBlank()) throw new IllegalArgumentException("Las indicaciones son obligatorias.");

    }
}
