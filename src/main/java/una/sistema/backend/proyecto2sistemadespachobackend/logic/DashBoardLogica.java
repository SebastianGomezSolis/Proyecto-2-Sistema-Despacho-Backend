package una.sistema.backend.proyecto2sistemadespachobackend.logic;

import una.sistema.backend.proyecto2sistemadespachobackend.model.Receta;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DashBoardLogica {
    private final RecetaLogica recetaLogica;

    public DashBoardLogica(RecetaLogica recetaLogica) { this.recetaLogica = recetaLogica; }

    public List<Receta> cargarRecetas() {
        try {
            return recetaLogica.findAll();
        } catch (Exception e) {
            Logger.getLogger(DashBoardLogica.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public int totalRecetas() { return cargarRecetas().size(); }

    // Mapa rangoEstados -> cantidad
    public LinkedHashMap<String, Long> recetasPorEstado() {
        // Mantener orden logico de los rangos
        LinkedHashMap<String, Long> resultado = new LinkedHashMap<>();
        List<String> estados = Arrays.asList("Confeccionada", "Proceso", "Lista", "Entregada");

        Map<String, Long> conteos = cargarRecetas().stream()
                .filter(receta -> receta.getEstado() != null)
                .collect(Collectors.groupingBy(
                        receta -> receta.getEstado(),
                        Collectors.counting()
                ));

        for (String e : estados) {
            resultado.put(e, conteos.getOrDefault(e, 0L));
        }
        return resultado;
    }

}
