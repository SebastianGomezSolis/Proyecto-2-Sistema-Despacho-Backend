package una.sistema.backend.proyecto2sistemadespachobackend.model;

public class Medico extends Usuario {
    private String nombre;
    private String especialidad;

    public Medico() {}

    // Constructor con parametros
    public Medico(int id, String identificacion, String clave, String nombre, String especialidad) {
        super(id, identificacion, clave);
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    // Getters y Setters
    @Override public int getId() { return id; }
    @Override public String getIdentificacion() { return identificacion; }
    @Override public String getClave() { return clave; }
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }

    @Override public void setId(int id) { this.id = id; }
    @Override public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    @Override public void setClave(String clave) { this.clave = clave; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    @Override public String getTipoUsuario() { return "Medico"; }

}
