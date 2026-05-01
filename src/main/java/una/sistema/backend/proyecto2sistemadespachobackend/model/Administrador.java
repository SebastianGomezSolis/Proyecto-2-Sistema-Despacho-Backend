package una.sistema.backend.proyecto2sistemadespachobackend.model;

public class Administrador extends Usuario {
    // Constructor con parametros
    public Administrador() {}

    public Administrador(int id, String identificacion, String clave) {
        super(id, identificacion, clave);
    }

    // Getters y Setters
    @Override public int getId() { return id; }
    @Override public String getIdentificacion() { return identificacion; }
    @Override public String getClave() { return clave; }

    @Override public void setId(int id) { this.id = id; }
    @Override public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    @Override public void setClave(String clave) { this.clave = clave; }

    @Override public String getTipoUsuario() { return "Administrador"; }

}
