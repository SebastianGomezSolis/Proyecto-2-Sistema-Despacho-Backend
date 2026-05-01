package una.sistema.backend.proyecto2sistemadespachobackend.model;

import java.time.LocalDate;

public class Receta {
    private int id;                           // Id unico consecutivo de la receta
    private String identificacion;
    private Paciente paciente;                   // Paciente que se le atribuye la receta
    private RecetaDetalle medicamentos;;             // Medicamento que se receta
    private LocalDate fechaEntrega;
    private String estado;                       // Estado de la receta

    // Constructor sin parametros
    public Receta() {}

    // Constructor con parametros
    public Receta(int id, String identificacion, Paciente paciente, LocalDate fechaEntrega, String estado) {
        this.id = id;
        this.identificacion = identificacion;
        this.paciente = paciente;
        this.fechaEntrega = fechaEntrega;
        this.estado = "Confeccionada";
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public RecetaDetalle getDetalles() { return medicamentos; }
    public void setDetalles(RecetaDetalle medicamentos) { this.medicamentos = medicamentos; }

}
