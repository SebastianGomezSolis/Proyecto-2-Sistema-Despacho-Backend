package una.sistema.backend.proyecto2sistemadespachobackend.model;

public class RecetaDetalle {
    private int id;
    private Medicamento medicamento;
    private int cantidad;
    private String indicaciones;
    private int diasDuracion;

    public RecetaDetalle() {}

    public RecetaDetalle(int id,Medicamento medicamento, int cantidad, String indicaciones, int diasDuracion) {
        this.id = id;
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.diasDuracion = diasDuracion;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }

    public int getDiasDuracion() { return diasDuracion; }
    public void setDiasDuracion(int diasDuracion) { this.diasDuracion = diasDuracion; }

}
