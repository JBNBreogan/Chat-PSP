package Servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * La clase HiloCliente maneja la comunicación con un cliente específico en un servidor de chat.
 * Se encarga de escuchar permanentemente los mensajes del cliente y gestionar solicitudes 
 * de conexión, desconexión y envío de mensajes a otros clientes.
 */
public class HiloCliente extends Thread {
    private final Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private final Servidor server;
    private String identificador;
    private boolean escuchando;

    /**
     * Constructor que inicializa el socket y el servidor con los cuales se establecerá la comunicación.
     * También crea los streams de entrada y salida de datos.
     * 
     * @param socket El socket a través del cual se comunica el cliente.
     * @param server La referencia al servidor que maneja a todos los clientes.
     */
    public HiloCliente(Socket socket, Servidor server) {
        this.server = server;
        this.socket = socket;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println("Error al inicializar los streams de entrada y salida.");
        }
    }

    /**
     * Método que cierra la conexión con el cliente al cerrar el socket y detener el proceso de escucha.
     */
    public void desconnectar() {
        try {
            socket.close();
            escuchando = false;
        } catch (IOException ex) {
            System.err.println("Error al cerrar el socket de comunicación con el cliente.");
        }
    }

    /**
     * Método principal del hilo. Se ejecuta continuamente para escuchar lo que el cliente está enviando.
     */
    @Override
    public void run() {
        try {
            escuchar();
        } catch (Exception ex) {
            System.err.println("Error al ejecutar el método de escucha del hilo del cliente.");
        }
        desconnectar();
    }

    /**
     * Escucha de forma constante los mensajes enviados por el cliente a través del socket.
     * Si recibe un objeto de tipo LinkedList, se ejecutan las acciones correspondientes.
     */
    public void escuchar() {
        escuchando = true;
        while (escuchando) {
            try {
                Object aux = objectInputStream.readObject();
                if (aux instanceof LinkedList) {
                    ejecutar((LinkedList<String>) aux);
                }
            } catch (Exception e) {
                System.err.println("Error al leer lo enviado por el cliente.");
            }
        }
    }

    /**
     * Ejecuta acciones en función del tipo de solicitud recibida del cliente, como conexión, 
     * desconexión o envío de mensajes a otros clientes.
     * 
     * @param lista Lista de cadenas que contiene los datos recibidos desde el cliente.
     */
    public void ejecutar(LinkedList<String> lista) {
        String tipo = lista.get(0);
        switch (tipo) {
            case "SOLICITUD_CONEXION":
                confirmarConexion(lista.get(1));
                break;
            case "SOLICITUD_DESCONEXION":
                confirmarDesConexion();
                break;
            case "MENSAJE":
                String destinatario = lista.get(2);
                server.clientes
                        .stream()
                        .filter(h -> destinatario.equals(h.getIdentificador()))
                        .forEach(h -> h.enviarMensaje(lista));
                break;
            default:
                break;
        }
    }

    /**
     * Envía un mensaje al cliente a través del stream de salida del socket.
     * 
     * @param lista Lista de cadenas que representa el mensaje a ser enviado.
     */
    private void enviarMensaje(LinkedList<String> lista) {
        try {
            objectOutputStream.writeObject(lista);
        } catch (Exception e) {
            System.err.println("Error al enviar el objeto al cliente.");
        }
    }

    /**
     * Confirma la conexión de un nuevo cliente, notificando al servidor y a los demás
     * clientes de su presencia.
     * 
     * @param identificador Identificador único asignado al cliente conectado.
     */
    private void confirmarConexion(String identificador) {
        Servidor.idClientes++;
        this.identificador = Servidor.idClientes + " - " + identificador;
        LinkedList<String> lista = new LinkedList<>();
        lista.add("CONEXION_ACEPTADA");
        lista.add(this.identificador);
        lista.addAll(server.getUsuarios());
        enviarMensaje(lista);
        server.addLog("\nNuevo cliente: " + this.identificador);

        LinkedList<String> auxLista = new LinkedList<>();
        auxLista.add("NUEVO_USUARIO_CONECTADO");
        auxLista.add(this.identificador);
        server.clientes.forEach(cliente -> cliente.enviarMensaje(auxLista));
        server.clientes.add(this);
    }

    /**
     * Retorna el identificador del cliente dentro del sistema.
     * 
     * @return El identificador único del cliente.
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Gestiona la desconexión de un cliente, notificando al servidor y a los demás clientes
     * para que lo eliminen de su lista de contactos.
     */
    private void confirmarDesConexion() {
        LinkedList<String> auxLista = new LinkedList<>();
        auxLista.add("USUARIO_DESCONECTADO");
        auxLista.add(this.identificador);
        server.addLog("\nEl cliente \"" + this.identificador + "\" se ha desconectado.");
        this.desconnectar();
        for (int i = 0; i < server.clientes.size(); i++) {
            if (server.clientes.get(i).equals(this)) {
                server.clientes.remove(i);
                break;
            }
        }
        server.clientes.forEach(h -> h.enviarMensaje(auxLista));
    }
}
