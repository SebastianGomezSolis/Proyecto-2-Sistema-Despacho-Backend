package una.sistema.backend.proyecto2sistemadespachobackend.servicios.chat;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class UserChat {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void conectar(String host, int port, String nombre, Consumer<String> onMsg, Consumer<List<String>> onUsers) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("[USUARIOS] ")) {
                        String data = line.substring(11); // quita "[USUARIOS] " completo
                        List<String> lista = data.isBlank() ? List.of() : Arrays.asList(data.split("\\s*,\\s*"));
                        onUsers.accept(lista);
                    } else {
                        onMsg.accept(line);
                    }
                }
            } catch (IOException e) {
                onMsg.accept("[SISTEMA] Error, desconectado del servidor: " + e.getMessage());
            }
        }).start();

        // Enviar nombre propuesto
        out.println(nombre != null ? nombre : "");
    }

    public void enviarMensaje(String msg) {
        if (out != null) out.println(msg);
    }

    public void enviarPrivado(String destino, String msg) {
        if (out != null) out.println("@" + destino + " " + msg);
    }

    public void cerrar() throws IOException {
        if (socket != null) socket.close();
    }
}
