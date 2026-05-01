package una.sistema.backend.proyecto2sistemadespachobackend.servicios.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class HospitalChatServer {
    private final int port;
    private final Set<UserHandler> users = Collections.synchronizedSet(new HashSet<>());
    private static final Logger LOGGER = Logger.getLogger(HospitalChatServer.class.getName());

    public HospitalChatServer(int port) {
        this.port = port;
        configureLogger();
    }

    public void configureLogger() {
        try {
            LOGGER.setUseParentHandlers(false);
            var fileHandler = new FileHandler("hospital-server.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Error al tratar de generar la bitacora del servidor: " + e.getMessage());
        }
    }

    public void start() {
        LOGGER.info("Iniciando el servidor en el puerto: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                UserHandler userHandler = new UserHandler(socket, this);
                users.add(userHandler);
                userHandler.start();
                LOGGER.info("Conexion aceptada desde: " + socket.getRemoteSocketAddress());
            }
        } catch (IOException e) {
            LOGGER.info("Error en el servidor: " + e);
        }
    }

    // Registra un usuario con nombre único. Devuelve el nombre asignado
    public String register(UserHandler handler, String proposed) {
        String base = (proposed == null || proposed.isBlank()) ? "anonimo" : proposed.trim();
        String unique = ensureUniqueName(base);
        handler.setNombre(unique);
        broadcast("[SISTEMA] " + unique + " se unió al chat");
        sendUsersList(); // Enviar la lista a todos los usuarios
        LOGGER.info("Usuario registrado: " + unique);
        return unique;
    }

    private String ensureUniqueName(String base) {
        synchronized (users) {
            boolean taken = true;
            String candidate = base;
            int i = 2;
            while (taken) {
                taken = false;
                for (UserHandler u : users) {
                    if (candidate.equals(u.getNombre())) {
                        taken = true;
                        break;
                    }
                }
                if (taken) {
                    candidate = base + "(" + i + ")";
                    i++;
                }
            }
            return candidate;
        }
    }

    public void remove(UserHandler userHandler) {
        users.remove(userHandler);
        if (userHandler.getNombre() != null) {
            broadcast("[SISTEMA] " + userHandler.getNombre() + " salió del chat");
            sendUsersList();
        }
        LOGGER.info("Cliente eliminado de la conexion: " + userHandler.getNombre());
    }

    public void broadcast(String message) {
        synchronized (users) {
            for (UserHandler user : users) {
                user.send(message);
            }
        }
    }

    // Metodo para poder enviar un mensaje privado, retorna true si se pudo enviar
    public boolean sendPrivate(String from, String to, String message) {
        UserHandler encontrado = null;
        UserHandler user = null;

        // Buscar emisor y destinatario por nombre recorriendo el Set
        synchronized (this.users) {
            for (UserHandler u : this.users) {
                String nombre = u.getNombre();
                if (nombre == null) continue;
                if (nombre.equals(to)) encontrado = u;
                if (nombre.equals(from)) user = u;
            }
        }

        if (encontrado != null) {
            encontrado.send("[PRIVADO] " + from + ": " + message);
            if (user != null && user != encontrado) {
                user.send("[PRIVADO a " + to + "] " + message);
            }
            return true;
        }
        if (user != null) {
            user.send("[SISTEMA] Usuario '" + to + "' no encontrado.");
        }
        return false;
    }

    // Muestra la lista de usuarios
    public void sendUsersList() {
        List<String> nombres = new ArrayList<>();
        synchronized (users) {
            for (UserHandler u : users) {
                if (u.getNombre() != null) nombres.add(u.getNombre());
            }
        }
        String line = "[USUARIOS] " + String.join(",", nombres);
        broadcast(line);
    }

    public static void main(String[] args) {
        new HospitalChatServer(6000).start();
    }
}
