package una.sistema.backend.proyecto2sistemadespachobackend.logic;

import una.sistema.backend.proyecto2sistemadespachobackend.datos.FarmaceutaDatos;
import una.sistema.backend.proyecto2sistemadespachobackend.model.Farmaceuta;

import java.sql.SQLException;
import java.util.List;

public class FarmaceutaLogica {
    private final FarmaceutaDatos store = new FarmaceutaDatos();

    // --------- Lectura ---------
    public List<Farmaceuta> findAll() throws SQLException {
        return store.findAll();
    }

    public Farmaceuta findById(int id) throws SQLException {
        return store.findById(id);
    }

    public Farmaceuta findByIdentificacion(String identificacion) throws SQLException {
        if (identificacion == null || identificacion.isBlank()) return null;
        return store.findByIdentificacion(identificacion);
    }

    // --------- Escritura ---------
    public Farmaceuta create(Farmaceuta nuevo) throws SQLException {
        validarNuevo(nuevo);

        // Unicidad por identificación
        if (existeIdentificacion(nuevo.getIdentificacion(), null)) {
            throw new IllegalArgumentException(
                    "Ya existe un farmaceuta con la identificación: " + nuevo.getIdentificacion()
            );
        }

        return store.insert(nuevo);
    }

    public Farmaceuta update(Farmaceuta f) throws SQLException {
        if (f == null || f.getId() <= 0)
            throw new IllegalArgumentException("El farmaceuta a actualizar requiere un ID válido.");

        validarCampos(f);

        // Unicidad por identificación (excluyendo su propio id)
        if (existeIdentificacion(f.getIdentificacion(), f.getId())) {
            throw new IllegalArgumentException(
                    "La identificación '" + f.getIdentificacion() + "' ya está registrada en otro farmaceuta."
            );
        }

        return store.update(f);
    }

    public boolean deleteById(int id) throws SQLException {
        if (id <= 0) return false;
        return store.delete(id) > 0;
    }

    // --------- Helpers ---------
    private void validarNuevo(Farmaceuta f) {
        if (f == null) throw new IllegalArgumentException("Farmaceuta nulo.");
        validarCampos(f);
    }

    private void validarCampos(Farmaceuta f) {
        if (f.getIdentificacion() == null || f.getIdentificacion().isBlank())
            throw new IllegalArgumentException("La identificación es obligatoria.");
        if (f.getNombre() == null || f.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (f.getClave() == null || f.getClave().isBlank())
            throw new IllegalArgumentException("La clave es obligatoria.");
        if (f.getClave().length() < 4)
            throw new IllegalArgumentException("La clave debe tener al menos 4 caracteres.");
    }

    private boolean existeIdentificacion(String identificacion, Integer excluirId) throws SQLException {
        for (Farmaceuta x : store.findAll()) {
            if (x.getIdentificacion().equals(identificacion)) {
                if (excluirId == null || x.getId() != excluirId) return true;
            }
        }
        return false;
    }
}

