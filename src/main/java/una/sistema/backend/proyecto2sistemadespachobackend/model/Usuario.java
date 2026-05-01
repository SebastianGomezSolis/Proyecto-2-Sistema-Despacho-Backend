package una.sistema.backend.proyecto2sistemadespachobackend.model;

public abstract class Usuario {
    protected int id;
    protected String identificacion;
    protected String clave;

    public Usuario() {}

    protected Usuario(int id, String identificacion, String clave) {
        this.id = id;
        this.identificacion = identificacion;
        this.clave = clave;
    }

    public abstract int getId();
    public abstract String getIdentificacion();
    public abstract String getClave();

    public abstract void setId(int id);
    public abstract void setIdentificacion(String identificacion);
    public abstract void setClave(String clave);

    public abstract String getTipoUsuario();
}
