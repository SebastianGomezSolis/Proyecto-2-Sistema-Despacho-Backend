package una.sistema.backend.proyecto2sistemadespachobackend.model;

import java.time.LocalDate;

public class Paciente {
    private int id;
    private String identificacion;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String telefono;

    // Constructor sin parametros
    public Paciente() {}

    // Constructor con parametros
    public Paciente(int id, String identificacion, String nombre, LocalDate fechaNacimiento, String telefono) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getTelefono() { return telefono; }

    public void setId(int id) { this.id = id; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

}
