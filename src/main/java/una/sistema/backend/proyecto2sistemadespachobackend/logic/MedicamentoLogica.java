package una.sistema.backend.proyecto2sistemadespachobackend.logic;

import una.sistema.backend.proyecto2sistemadespachobackend.datos.MedicamentoDatos;
import una.sistema.backend.proyecto2sistemadespachobackend.model.Medicamento;

import java.sql.SQLException;
import java.util.List;

public class MedicamentoLogica {

    private final MedicamentoDatos store = new MedicamentoDatos();

    // --------- Lectura ---------
    public List<Medicamento> findAll() throws SQLException {
        return store.findAll();
    }

    public Medicamento findById(int id) throws SQLException {
        return store.findById(id);
    }

    public Medicamento findByCodigo(String codigo) throws SQLException {
        if (codigo == null || codigo.isBlank()) return null;
        return store.findByCodigo(codigo);
    }

    // --------- Escritura ---------
    public Medicamento create(Medicamento nuevo) throws SQLException {
        validarNuevo(nuevo);

        // Unicidad por codigo
        if (existeCodigo(nuevo.getCodigo(), null)) {
            throw new IllegalArgumentException("Ya existe un medicamento con código: " + nuevo.getCodigo());
        }

        return store.insert(nuevo);
    }

    public Medicamento update(Medicamento m) throws SQLException {
        if (m == null || m.getId() <= 0)
            throw new IllegalArgumentException("El medicamento a actualizar requiere un ID válido.");

        validarCampos(m);

        // Unicidad por codigo (excluyéndose a sí mismo)
        if (existeCodigo(m.getCodigo(), m.getId())) {
            throw new IllegalArgumentException("El código '" + m.getCodigo() + "' ya está registrado en otro medicamento.");
        }

        return store.update(m);
    }

    public boolean deleteById(int id) throws SQLException {
        if (id <= 0) return false;
        return store.delete(id) > 0;
    }

    // --------- Helpers ---------
    private void validarNuevo(Medicamento m) {
        if (m == null) throw new IllegalArgumentException("Medicamento nulo.");
        validarCampos(m);
    }

    private void validarCampos(Medicamento m) {
        if (m.getNombre() == null || m.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (m.getCodigo() == null || m.getCodigo().isBlank())
            throw new IllegalArgumentException("El código es obligatorio.");
        if (m.getDescripcion() == null || m.getDescripcion().isBlank())
            throw new IllegalArgumentException("La descripción es obligatoria.");
    }

    /** Revisa si existe otra fila con el mismo código. */
    private boolean existeCodigo(String codigo, Integer excluirId) throws SQLException {
        for (Medicamento x : store.findAll()) {
            if (x.getCodigo().equals(codigo)) {
                if (excluirId == null || x.getId() != excluirId) return true;
            }
        }
        return false;
    }
}

