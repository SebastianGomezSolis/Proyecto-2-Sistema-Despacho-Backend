package una.sistema.backend.proyecto2sistemadespachobackend.logic;

import una.sistema.backend.proyecto2sistemadespachobackend.datos.AdministradorDatos;
import una.sistema.backend.proyecto2sistemadespachobackend.model.Administrador;

import java.sql.SQLException;
import java.util.List;

public class AdministradorLogica {
    private final AdministradorDatos store = new AdministradorDatos();

    // --------- Lectura ---------
    public List<Administrador> findAll() throws SQLException {
        return store.findAll();
    }

    public Administrador findById(int id) throws SQLException {
        return store.findById(id);
    }

    public Administrador findByIdentificacion(String identificacion) throws SQLException {
        if (identificacion == null || identificacion.isBlank()) return null;
        return store.findByIdentificacion(identificacion);
    }

    // --------- Escritura ---------
    public Administrador create(Administrador nuevo) throws SQLException {
        validarNuevo(nuevo);
        return store.insert(nuevo);
    }

    public Administrador update(Administrador admin) throws SQLException {
        if (admin == null || admin.getId() <= 0)
            throw new IllegalArgumentException("El administrador a actualizar requiere un ID válido.");
        validarCampos(admin);
        return store.update(admin);
    }

    public boolean deleteById(int id) throws SQLException {
        if (id <= 0) return false;
        return store.delete(id) > 0;
    }

    // --------- Helpers ---------
    private void validarNuevo(Administrador a) {
        if (a == null) throw new IllegalArgumentException("Administrador nulo.");
        validarCampos(a);
    }

    private void validarCampos(Administrador a) {
        if (a.getIdentificacion() == null || a.getIdentificacion().isBlank())
            throw new IllegalArgumentException("La identificación es obligatoria.");
        if (a.getClave() == null || a.getClave().isBlank())
            throw new IllegalArgumentException("La clave es obligatoria.");
        if (a.getClave().length() < 4)
            throw new IllegalArgumentException("La clave debe tener al menos 4 caracteres.");
    }
}

