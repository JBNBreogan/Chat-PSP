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
                confirmConnection(lista.get(1));
                break;
            case "SOLICITUD_DESCONEXION":
                confirmarDesconnection();
                break;                
            case "MENSAJE":
                String destinatario=lista.get(2);
                server.clientes.stream().filter(h -> (destinatario.equals(h.getIde()))).forEach((h) -> h.sendMessage(lista));
                break;
            default:
                break;
        }
    }

    private void confirmConnection(String identificador) {
        Servidor.idClientes++;
        this.id=Servidor.idClientes+" - "+id;
        LinkedList<String> lista=new LinkedList<>();
        lista.add("CONEXION_ACEPTADA");
        lista.add(this.id);
        lista.addAll(server.getUsuarios());
        sendMessage(lista);
        server.addLog("\nNuevo cliente: "+this.id);
        LinkedList<String> auxLista=new LinkedList<>();
        auxLista.add("NUEVO_USUARIO_CONECTADO");
        auxLista.add(this.id);
        server.clientes
                .stream()
                .forEach(cliente -> cliente.sendMessage(auxLista));
        server.clientes.add(this);
    }


    private void confirmarDesconnection() {
        LinkedList<String> auxLista=new LinkedList<>();
        auxLista.add("USUARIO_DESCONECTADO");
        auxLista.add(this.id);
        server.addLog("\nEl cliente \""+this.id+"\" se ha desconectado.");
        this.desconnect();
        for(int i=0;i<server.clientes.size();i++){
            if(server.clientes.get(i).equals(this)){
                server.clientes.remove(i);
                break;
            }
        }
        server.clientes.stream().forEach(h -> h.sendMessage(auxLista));        
    }

    private void sendMessage(LinkedList<String> lista){
        try {
            oos.writeObject(lista);            
        } catch (IOException e) {
            System.err.println("Error al enviar el objeto al cliente.");
        }
    }    

    public void desconnect() {
        try {
            socket.close();
            preparado=false;
        } catch (IOException ex) {
            System.err.println("Error al cerrar el socket de comunicación con el cliente.");
        }
    }

    public void run() {				
        try{
           listen();
        } catch (Exception ex) {
            System.err.println("Error al llamar al método readLine del hilo del cliente.");
        }
        desconnect();
    }
}

