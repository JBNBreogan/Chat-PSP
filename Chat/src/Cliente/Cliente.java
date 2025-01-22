package Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

/**
 * Clase Cliente que maneja la conexión y comunicación con el servidor de chat.
 * Extiende Thread para permitir la escucha de mensajes en segundo plano.
 */
public class Cliente extends Thread {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final VentanaC ventana;    
    private String id;
    private boolean preparado;
    private final String host;
    private final int puerto;

    /**
     * Constructor de la clase Cliente.
     * 
     * @param ventana La interfaz gráfica asociada al cliente.
     * @param host Dirección del servidor.
     * @param puerto Puerto del servidor.
     * @param id Identificador del usuario.
     */
    public Cliente(VentanaC ventana, String host, Integer puerto, String id) {
        this.ventana = ventana;        
        this.host = host;
        this.puerto = puerto;
        this.id = id;
        preparado = true;
        this.start();
    }

    /**
     * Escucha los mensajes entrantes del servidor y los procesa.
     */
    public void listen() {
        try {
            while (preparado) {
                Object aux = ois.readObject();
                if (aux != null) {
                    if (aux instanceof LinkedList) {
                        ejecutar((LinkedList<String>) aux);
                    } else {
                        System.err.println("Se recibió un Objeto desconocido a través del socket");
                    }
                } else {
                    System.err.println("Se recibió un null a través del socket");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventana, "La comunicación con el servidor se ha\n"
                    + "perdido, este chat tendrá que finalizar.\n"
                    + "Esta aplicación se cerrará.");
            System.exit(0);
        }
    }

    /**
     * Procesa los mensajes recibidos del servidor.
     * 
     * @param mensajes Lista de mensajes recibidos.
     */
    public void ejecutar(LinkedList<String> mensajes) {
        String tipo = mensajes.get(0);
        switch (tipo) {
            case "CONEXION_ACEPTADA":
                id = mensajes.get(1);
                ventana.sesionIniciada(id);
                for (int i = 2; i < mensajes.size(); i++) {
                    ventana.addContacto(mensajes.get(i));
                }
                break;
            case "NUEVO_USUARIO_CONECTADO":
                ventana.addContacto(mensajes.get(1));
                break;
            case "USUARIO_DESCONECTADO":
                ventana.eliminarContacto(mensajes.get(1));
                break;                
            case "MENSAJE":
                ventana.addMensaje(mensajes.get(1), mensajes.get(3));
                break;
            default:
                break;
        }
    }

    /**
     * Envía un mensaje a otro cliente a través del servidor.
     * 
     * @param cliente El destinatario del mensaje.
     * @param mensaje El contenido del mensaje.
     */
    public void enviarMensaje(String cliente, String mensaje) {
        LinkedList<String> lista = new LinkedList<>();
        lista.add("MENSAJE");
        lista.add(id);
        lista.add(cliente);
        lista.add(mensaje);
        try {
            oos.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error de lectura y escritura al enviar mensaje al servidor.");
        }
    }

    /**
     * Envía una solicitud de conexión al servidor.
     * 
     * @param id ID del usuario.
     */
    private void enviarSolicitudConexion(String id) {
        LinkedList<String> conexiones = new LinkedList<>();
        conexiones.add("SOLICITUD_CONEXION");
        conexiones.add(id);
        try {
            oos.writeObject(conexiones);
        } catch (IOException ex) {
            System.out.println("Error de lectura y escritura al enviar mensaje al servidor.");
        }
    }

    /**
     * Desconecta al cliente del servidor y cierra los recursos.
     */
    public void disconect() {
        try {
            oos.close();
            ois.close();
            socket.close();  
            preparado = false;
        } catch (Exception e) {
            System.err.println("Error al cerrar los elementos de comunicación del cliente.");
        }
    }

    /**
     * Confirma la desconexión del cliente al servidor.
     */
    void confirmarDesconexion() {
        LinkedList<String> conexiones = new LinkedList<>();
        conexiones.add("SOLICITUD_DESCONEXION");
        conexiones.add(id);
        try {
            oos.writeObject(conexiones);
        } catch (IOException ex) {
            System.out.println("Error de lectura y escritura al enviar mensaje al servidor.");
        }
    }

    /**
     * Método principal del hilo que maneja la conexión con el servidor.
     */
    public void run() {
        try {
            socket = new Socket(host, puerto);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("Conexion exitosa!!!!");
            this.enviarSolicitudConexion(id);
            this.listen();
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(ventana, "Conexión rechazada, servidor desconocido,\n"
                    + "puede que hayas ingresado una ip incorrecta\n"
                    + "o que el servidor no este iniciado.\n"
                    + "Esta aplicación se cerrará.");
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(ventana, "Conexión rechazada, error de Entrada/Salida,\n"
                    + "puede que hayas ingresado una ip o un puerto\n"
                    + "incorrecto, o que el servidor no este iniciado.\n"
                    + "Esta aplicación se cerrará.");
            System.exit(0);
        }
    }

    /**
     * Obtiene el identificador del cliente.
     * 
     * @return ID del cliente.
     */
    public String getIde() {
        return id;
    }
}