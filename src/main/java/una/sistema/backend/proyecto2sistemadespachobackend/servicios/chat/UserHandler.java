package una.sistema.backend.proyecto2sistemadespachobackend.servicios.chat;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserHandler extends Thread {
    private final Socket socket;
    private final HospitalChatServer server;
    private PrintWriter out;
    private BufferedReader in;

    private String nombre = "anonimo";
    private String role = "usuario";

    private static final Logger LOGGER = Logger.getLogger("HospitalChatServer");

    public UserHandler(Socket socket, HospitalChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void setNombre(String n) { this.nombre = n; }

    public String getNombre() { return nombre; }

    public void send(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            out.println("Bienvenido al chat del hospital. Escribe tu nombre (una sola línea).");

            // Primer mensaje: nombre propuesto (o vacío)
            String first = in.readLine();
            String assigned = server.register(this, first);

            // Loop de mensajes
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // PRIVADO
                if (line.startsWith("@")) {
                    int space = line.indexOf(' ');
                    if (space > 1) {
                        String destino = line.substring(1, space).trim();
                        String msg = line.substring(space + 1).trim();
                        if (!msg.isEmpty()) {
                            server.sendPrivate(assigned, destino, msg);
                        } else {
                            send("[SISTEMA] Mensaje vacío.");
                        }
                    } else {
                        send("[SISTEMA] Formato: @usuario mensaje");
                    }
                    continue;
                }

                // Comando opcional: /usuarios → fuerza refresco (debug)
                if (line.equalsIgnoreCase("/usuarios")) {
                    server.sendUsersList();
                    continue;
                }

                // GENERAL
                server.broadcast(assigned + ": " + line);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Conexion finalizada: " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "Error al cerrar el socket: " + ex);
            }
            server.remove(this);
        }
    }
}
