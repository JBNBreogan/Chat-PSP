package Servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class HiloCliente extends Thread{
    private final Socket socket;   
    private final Servidor server;
    private String id; 
    private ObjectInputStream ois;            
    private ObjectOutputStream oos;
    private boolean preparado;

    public String getIde() {
        return id;
    }

    public HiloCliente(Socket socket,Servidor server) {
        this.server=server;
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println("Error en la inicialización.");
        }
    }

    public void listen(){        
        preparado=true;
        while(preparado){
            try {
                Object aux=ois.readObject();
                if(aux instanceof LinkedList){
                    ejecute((LinkedList<String>)aux);
                }
            } catch (Exception e) {                    
                System.err.println("Error al leer lo enviado por el cliente.");
            }
        }
    }

    public void ejecute(LinkedList<String> lista){
        String tipo=lista.get(0);
        switch (tipo) {
            case "SOLICITUD_CONEXION":
               
                break;
            case "SOLICITUD_DESCONEXION":
               
                break;                
            case "MENSAJE":
            
                break;
            default:
                break;
        }
    }

   
}

