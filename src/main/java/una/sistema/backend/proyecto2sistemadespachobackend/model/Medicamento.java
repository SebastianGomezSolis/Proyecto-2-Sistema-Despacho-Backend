package una.sistema.backend.proyecto2sistemadespachobackend.model;

public class Medicamento {
    int id;
    private String codigo;
    private String nombre;
    private String descripcion;

    // Constructor sin parametros
    public Medicamento() {}

    // Constructor con parametros
    public Medicamento(int id, String codigo, String nombre, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setId(int id) { this.id = id; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

}
