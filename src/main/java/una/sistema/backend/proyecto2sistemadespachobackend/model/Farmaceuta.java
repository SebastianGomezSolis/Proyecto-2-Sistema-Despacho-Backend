package una.sistema.backend.proyecto2sistemadespachobackend.model;

public class Farmaceuta  extends Usuario {
    private String nombre;

    public Farmaceuta() {}

    // Constructor con parametros
    public Farmaceuta(int id, String identificacion, String clave, String nombre) {
        super(id, identificacion, clave);
        this.nombre = nombre;
    }

    // Getters y Setters
    @Override public int getId() { return id; }
    @Override public String getIdentificacion() { return identificacion; }
    @Override public String getClave() { return clave; }
    public String getNombre() { return nombre; }

    @Override public void setId(int id) { this.id = id; }
    @Override public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    @Override public void setClave(String clave) { this.clave = clave; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override public String getTipoUsuario() { return "Farmaceuta"; }

}
