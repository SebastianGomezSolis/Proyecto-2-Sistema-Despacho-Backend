package una.sistema.backend.proyecto2sistemadespachobackend.logic;

import una.sistema.backend.proyecto2sistemadespachobackend.datos.PacienteDatos;
import una.sistema.backend.proyecto2sistemadespachobackend.model.Paciente;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PacienteLogica {

    private final PacienteDatos store = new PacienteDatos();

    // --------- Lectura ---------
    public List<Paciente> findAll() throws SQLException {
        return store.findAll();
    }

    public Paciente findById(int id) throws SQLException {
        return store.findById(id);
    }

    public Paciente findByIdentificacion(String identificacion) throws SQLException {
        if (identificacion == null || identificacion.isBlank()) return null;
        return store.findByIdentificacion(identificacion);
    }

    // --------- Escritura ---------
    public Paciente create(Paciente nuevo) throws SQLException {
        validarNuevo(nuevo);

        // Unicidad por identificación
        if (existeIdentificacion(nuevo.getIdentificacion(), null)) {
            throw new IllegalArgumentException("Ya existe un paciente con la identificación: " + nuevo.getIdentificacion());
        }

        return store.insert(nuevo);
    }

    public Paciente update(Paciente p) throws SQLException {
        if (p == null || p.getId() <= 0)
            throw new IllegalArgumentException("El paciente a actualizar requiere un ID válido.");

        validarCampos(p);

        // Unicidad por identificación (excluyendo su propio id)
        if (existeIdentificacion(p.getIdentificacion(), p.getId())) {
            throw new IllegalArgumentException(
                    "La identificación '" + p.getIdentificacion() + "' ya está registrada en otro paciente."
            );
        }

        return store.update(p);
    }

    public boolean deleteById(int id) throws SQLException {
        if (id <= 0) return false;
        return store.delete(id) > 0;
    }

    // --------- Helpers ---------
    private void validarNuevo(Paciente p) {
        if (p == null) throw new IllegalArgumentException("Paciente nulo.");
        validarCampos(p);
    }

    private void validarCampos(Paciente p) {
        if (p.getIdentificacion() == null || p.getIdentificacion().isBlank())
            throw new IllegalArgumentException("La identificación es obligatoria.");
        if (p.getNombre() == null || p.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (p.getTelefono() == null || p.getTelefono().isBlank())
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        if (p.getFechaNacimiento() == null)
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        if (p.getFechaNacimiento().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
    }

    /** Revisa si existe otra fila con la misma identificación. */
    private boolean existeIdentificacion(String identificacion, Integer excluirId) throws SQLException {
        for (Paciente x : store.findAll()) {
            if (x.getIdentificacion().equals(identificacion)) {
                if (excluirId == null || x.getId() != excluirId) return true;
            }
        }
        return false;
    }
}

