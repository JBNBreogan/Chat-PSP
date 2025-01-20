package Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    public void escuchar() {
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

    public void desconectar() {
        try {
            oos.close();
            ois.close();
            socket.close();  
            preparado = false;
        } catch (Exception e) {
            System.err.println("Error al cerrar los elementos de comunicación del cliente.");
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


    String getIde() {
        return id;
    }
}
