package una.sistema.backend.proyecto2sistemadespachobackend.servicios;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import una.sistema.backend.proyecto2sistemadespachobackend.logic.*;
import una.sistema.backend.proyecto2sistemadespachobackend.model.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorGeneral {
    private static final Gson gson = GsonProvider.get();

    // Lógica de cada entidad
    private final MedicoLogica medicoLogica = new MedicoLogica();
    private final PacienteLogica pacienteLogica = new PacienteLogica();
    private final FarmaceutaLogica farmaceutaLogica = new FarmaceutaLogica();
    private final AdministradorLogica administradorLogica = new AdministradorLogica();
    private final MedicamentoLogica medicamentoLogica = new MedicamentoLogica();
    private final RecetaLogica recetaLogica = new RecetaLogica();
    private final DashBoardLogica dashBoardLogica = new DashBoardLogica(recetaLogica);

    public void iniciar() throws Exception {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("Servidor general en puerto 5000...");
            while (true) {
                Socket s = server.accept();
                new Thread(() -> manejar(s)).start();
            }
        }
    }

    private void manejar(Socket socket) {
        try (DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            String json = in.readUTF();
            System.out.println("[RX] " + json);

            String respuesta;

            try {
                Peticion p = gson.fromJson(json, Peticion.class);
                if (p == null || p.tipo == null || p.op == null) {
                    throw new IllegalArgumentException("Petición inválida (tipo/op nulo)");
                }

                // Ruteo por tipo de recurso (entidad)
                switch (p.tipo) {
                    case "medico" -> respuesta = manejarMedico(p);
                    case "paciente" -> respuesta = manejarPaciente(p);
                    case "farmaceuta" -> respuesta = manejarFarmaceuta(p);
                    case "administrador" -> respuesta = manejarAdministrador(p);
                    case "medicamento" -> respuesta = manejarMedicamento(p);
                    case "receta" -> respuesta = manejarReceta(p);
                    case "dashboard" -> respuesta = manejarDashboard(p);
                    default -> respuesta = "{\"error\":\"Tipo no soportado: " + p.tipo + "\"}";
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                String msg = ex.getMessage() == null ? "" : ex.getMessage().replace("\"", "'");
                respuesta = "{\"error\":\"" + ex.getClass().getSimpleName() + ":" + msg + "\"}";
            }

            System.out.println("[TX] " + respuesta);
            out.writeUTF(respuesta);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =============== MANEJADORES POR ENTIDAD ===============
    private String manejarMedico(Peticion p) throws Exception {
        String respuesta;
        switch (p.op) {
            case "create": {
                Medico m = gson.fromJson(p.data, Medico.class);
                respuesta = gson.toJson(medicoLogica.create(m));
                break;
            }
            case "update": {
                Medico m = gson.fromJson(p.data, Medico.class);
                respuesta = gson.toJson(medicoLogica.update(m));
                break;
            }
            case "deleteById": {
                medicoLogica.deleteById(p.id);
                respuesta = "{\"ok\":true}";
                break;
            }
            case "findAll": {
                respuesta = gson.toJson(medicoLogica.findAll());
                break;
            }
            case "findById": {
                respuesta = gson.toJson(medicoLogica.findById(p.id));
                break;
            }
            case "findByIdentificacion": {
                respuesta = gson.toJson(medicoLogica.findByIdentificacion(p.identificacion));
                break;
            }
            default: {
                respuesta = "{\"error\":\"Operación no válida para medico\"}";
                break;
            }
        }
        return respuesta;
    }

    private String manejarPaciente(Peticion p) throws Exception {
        String respuesta;
        switch (p.op) {
            case "create": {
                Paciente pac = gson.fromJson(p.data, Paciente.class);
                respuesta = gson.toJson(pacienteLogica.create(pac));
                break;
            }
            case "update": {
                Paciente pac = gson.fromJson(p.data, Paciente.class);
                respuesta = gson.toJson(pacienteLogica.update(pac));
                break;
            }
            case "deleteById": {
                pacienteLogica.deleteById(p.id);
                respuesta = "{\"ok\":true}";
                break;
            }
            case "findAll": {
                respuesta = gson.toJson(pacienteLogica.findAll());
                break;
            }
            case "findById": {
                respuesta = gson.toJson(pacienteLogica.findById(p.id));
                break;
            }
            case "findByIdentificacion": {
                respuesta = gson.toJson(pacienteLogica.findByIdentificacion(p.identificacion));
                break;
            }
            default: {
                respuesta = "{\"error\":\"Operación no válida para paciente\"}";
                break;
            }
        }
        return respuesta;
    }

    private String manejarFarmaceuta(Peticion p) throws Exception {
        String respuesta;
        switch (p.op) {
            case "create": {
                Farmaceuta f = gson.fromJson(p.data, Farmaceuta.class);
                respuesta = gson.toJson(farmaceutaLogica.create(f));
                break;
            }
            case "update": {
                Farmaceuta f = gson.fromJson(p.data, Farmaceuta.class);
                respuesta = gson.toJson(farmaceutaLogica.update(f));
                break;
            }
            case "deleteById": {
                farmaceutaLogica.deleteById(p.id);
                respuesta = "{\"ok\":true}";
                break;
            }
            case "findAll": {
                respuesta = gson.toJson(farmaceutaLogica.findAll());
                break;
            }
            case "findById": {
                respuesta = gson.toJson(farmaceutaLogica.findById(p.id));
                break;
            }
            case "findByIdentificacion": {
                respuesta = gson.toJson(farmaceutaLogica.findByIdentificacion(p.identificacion));
                break;
            }
            default: {
                respuesta = "{\"error\":\"Operación no válida para farmaceuta\"}";
                break;
            }
        }
        return respuesta;
    }

    private String manejarAdministrador(Peticion p) throws Exception {
        String respuesta;
        switch (p.op) {
            case "create": {
                Administrador a = gson.fromJson(p.data, Administrador.class);
                respuesta = gson.toJson(administradorLogica.create(a));
                break;
            }
            case "update": {
                Administrador a = gson.fromJson(p.data, Administrador.class);
                respuesta = gson.toJson(administradorLogica.update(a));
                break;
            }
            case "deleteById": {
                administradorLogica.deleteById(p.id);
                respuesta = "{\"ok\":true}";
                break;
            }
            case "findAll": {
                respuesta = gson.toJson(administradorLogica.findAll());
                break;
            }
            case "findById": {
                respuesta = gson.toJson(administradorLogica.findById(p.id));
                break;
            }
            case "findByIdentificacion": {
                respuesta = gson.toJson(administradorLogica.findByIdentificacion(p.identificacion));
                break;
            }
            default: {
                respuesta = "{\"error\":\"Operación no válida para administrador\"}";
                break;
            }
        }
        return respuesta;
    }

    private String manejarMedicamento(Peticion p) throws Exception {
        String respuesta;
        switch (p.op) {
            case "create": {
                Medicamento med = gson.fromJson(p.data, Medicamento.class);
                respuesta = gson.toJson(medicamentoLogica.create(med));
                break;
            }
            case "update": {
                Medicamento med = gson.fromJson(p.data, Medicamento.class);
                respuesta = gson.toJson(medicamentoLogica.update(med));
                break;
            }
            case "deleteById": {
                medicamentoLogica.deleteById(p.id);
                respuesta = "{\"ok\":true}";
                break;
            }
            case "findAll": {
                respuesta = gson.toJson(medicamentoLogica.findAll());
                break;
            }
            case "findById": {
                respuesta = gson.toJson(medicamentoLogica.findById(p.id));
                break;
            }
            case "findByCodigo": {
                respuesta = gson.toJson(medicamentoLogica.findByCodigo(p.codigo));
                break;
            }
            default: {
                respuesta = "{\"error\":\"Operación no válida para medicamento\"}";
                break;
            }
        }
        return respuesta;
    }

    private String manejarReceta(Peticion p) throws Exception {
        String respuesta;
        switch (p.op) {
            case "create": {
                Receta r = gson.fromJson(p.data, Receta.class);
                respuesta = gson.toJson(recetaLogica.create(r));
                break;
            }
            case "update": {
                Receta r = gson.fromJson(p.data, Receta.class);
                respuesta = gson.toJson(recetaLogica.update(r));
                break;
            }
            case "deleteById": {
                recetaLogica.deleteById(p.id);
                respuesta = "{\"ok\":true}";
                break;
            }
            case "findAll": {
                respuesta = gson.toJson(recetaLogica.findAll());
                break;
            }
            case "findById": {
                respuesta = gson.toJson(recetaLogica.findById(p.id));
                break;
            }
            default: {
                respuesta = "{\"error\":\"Operación no válida para receta\"}";
                break;
            }
        }
        return respuesta;
    }

    private String manejarDashboard(Peticion p) throws Exception {
        String respuesta;
        switch (p.op) {
            case "cargarRecetas": {
                respuesta = gson.toJson(dashBoardLogica.cargarRecetas());
                break;
            }
            case "totalRecetas": {
                respuesta = String.valueOf(dashBoardLogica.totalRecetas());
                break;
            }
            case "recetasPorEstado": {
                respuesta = gson.toJson(dashBoardLogica.recetasPorEstado());
                break;
            }
            default: {
                respuesta = "{\"error\":\"Operación no válida para dashboard\"}";
                break;
            }
        }
        return respuesta;
    }


    // =============== DTO DE LA PETICIÓN ===============
    public static class Peticion {
        public String tipo;
        public String op;
        public int id;
        public String identificacion;
        public String codigo;
        public JsonElement data;

        public Peticion() {}

        @Override
        public String toString() {
            return "Peticion{" +
                    "tipo='" + tipo + '\'' +
                    ", op='" + op + '\'' +
                    ", id=" + id +
                    ", identificacion='" + identificacion + '\'' +
                    ", codigo='" + codigo + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    public static void main(String[] args) throws Exception {
        new ServidorGeneral().iniciar();
    }
}
