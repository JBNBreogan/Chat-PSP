package Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Cliente extends Thread{

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final VentanaC ventana;    
    private String id;
    private boolean preparado;
    private final String host;
    private final int puerto;

    Cliente(VentanaC ventana, String host, Integer puerto, String nombre) {
        this.ventana = ventana;        
        this.host = host;
        this.puerto = puerto;
        this.id = nombre;
        preparado = true;
        this.start();
    }

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

    public void ejecutar(LinkedList<String> lista) {
        String tipo = lista.get(0);
        switch (tipo) {
            case "CONEXION_ACEPTADA":
                id = lista.get(1);
                ventana.sesionIniciada(id);
                for (int i = 2; i < lista.size(); i++) {
                    ventana.addContacto(lista.get(i));
                }
                break;
            case "NUEVO_USUARIO_CONECTADO":
                ventana.addContacto(lista.get(1));
                break;
            case "USUARIO_DESCONECTADO":
                ventana.eliminarContacto(lista.get(1));
                break;                
            case "MENSAJE":
                ventana.addMensaje(lista.get(1), lista.get(3));
                break;
            default:
                break;
        }
    }

    public void enviarMensaje(String cliente_receptor, String mensaje) {
        LinkedList<String> lista = new LinkedList<>();
        lista.add("MENSAJE");
        lista.add(id);
        lista.add(cliente_receptor);
        lista.add(mensaje);
        try {
            oos.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error de lectura y escritura al enviar mensaje al servidor.");
        }
    }

    private void enviarSolicitudConexion(String identificador) {
        LinkedList<String> conexiones = new LinkedList<>();
        conexiones.add("SOLICITUD_CONEXION");
        conexiones.add(identificador);
        try {
            oos.writeObject(conexiones);
        } catch (IOException ex) {
            System.out.println("Error de lectura y escritura al enviar mensaje al servidor.");
        }
    }

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

    public void run() {
        try {
            socket = new Socket(host, puerto);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("Conexion exitosa!!!!");
            this.enviarSolicitudConexion(id);
            this.listen();
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(ventana, "Conexión rehusada, servidor desconocido,\n"
                    + "puede que haya ingresado una ip incorrecta\n"
                    + "o que el servidor no este corriendo.\n"
                    + "Esta aplicación se cerrará.");
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(ventana, "Conexión rehusada, error de Entrada/Salida,\n"
                    + "puede que haya ingresado una ip o un puerto\n"
                    + "incorrecto, o que el servidor no este corriendo.\n"
                    + "Esta aplicación se cerrará.");
            System.exit(0);
        }
    }

    String getIde() {
        return id;
    }
}
