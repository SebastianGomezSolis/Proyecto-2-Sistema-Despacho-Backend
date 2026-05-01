package una.sistema.backend.proyecto2sistemadespachobackend.logic;

import una.sistema.backend.proyecto2sistemadespachobackend.datos.MedicoDatos;
import una.sistema.backend.proyecto2sistemadespachobackend.model.Medico;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoLogica {

    private final MedicoDatos store = new MedicoDatos();

    // --------- Lectura ---------
    public List<Medico> findAll() throws SQLException {
        return store.findAll();
    }

    public Medico findById(int id) throws SQLException {
        return store.findById(id);
    }

    public Medico findByIdentificacion(String identificacion) throws SQLException {
        if (identificacion == null || identificacion.isBlank()) return null;
        return store.findByIdentificacion(identificacion);
    }

    // Búsqueda simple en memoria (por nombre, especialidad).
    public List<Medico> searchByNombreOEspecialidad(String texto) throws SQLException {
        String q = (texto == null) ? "" : texto.trim().toLowerCase();
        List<Medico> result = new ArrayList<>();
        for (Medico m : store.findAll()) {
            if ((m.getNombre() != null && m.getNombre().toLowerCase().contains(q)) ||
                    (m.getEspecialidad() != null && m.getEspecialidad().toLowerCase().contains(q))) {
                result.add(m);
            }
        }
        return result;
    }

    // --------- Escritura ---------
    public Medico create(Medico nuevo) throws SQLException {
        validarNuevo(nuevo);

        // Unicidad por identificación
        if (existeIdentificacion(nuevo.getIdentificacion(), null)) {
            throw new IllegalArgumentException(
                    "Ya existe un médico con la identificación: " + nuevo.getIdentificacion()
            );
        }

        return store.insert(nuevo);
    }

    public Medico update(Medico m) throws SQLException {
        if (m == null || m.getId() <= 0)
            throw new IllegalArgumentException("El médico a actualizar requiere un ID válido.");

        validarCampos(m);

        // Unicidad por identificación (excluyendo su propio id)
        if (existeIdentificacion(m.getIdentificacion(), m.getId())) {
            throw new IllegalArgumentException(
                    "La identificación '" + m.getIdentificacion() + "' ya está registrada en otro médico."
            );
        }

        return store.update(m);
    }

    public boolean deleteById(int id) throws SQLException {
        if (id <= 0) return false;
        return store.delete(id) > 0;
    }

    // --------- Helpers ---------
    private void validarNuevo(Medico m) {
        if (m == null) throw new IllegalArgumentException("Médico nulo.");
        validarCampos(m);
    }

    private void validarCampos(Medico m) {
        if (m.getIdentificacion() == null || m.getIdentificacion().isBlank())
            throw new IllegalArgumentException("La identificación es obligatoria.");
        if (m.getNombre() == null || m.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (m.getEspecialidad() == null || m.getEspecialidad().isBlank())
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        if (m.getClave() == null || m.getClave().isBlank())
            throw new IllegalArgumentException("La clave es obligatoria.");
        if (m.getClave().length() < 4)
            throw new IllegalArgumentException("La clave debe tener al menos 4 caracteres.");
    }

    // Revisa si existe otra fila con la misma identificación.
    private boolean existeIdentificacion(String identificacion, Integer excluirId) throws SQLException {
        for (Medico x : store.findAll()) {
            if (x.getIdentificacion().equals(identificacion)) {
                if (excluirId == null || x.getId() != excluirId) return true;
            }
        }
        return false;
    }
}
