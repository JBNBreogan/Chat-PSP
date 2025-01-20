package Servidor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Servidor extends Thread{
    private ServerSocket sSocket;

    LinkedList<HiloCliente> clientes;

    private final VentanaS ventana;

    private final String puerto;

    static int idClientes;

    public Servidor (String puerto, VentanaS ventana){
        idClientes = 0;
        this.puerto = puerto;
        this.ventana = ventana;
        clientes = new LinkedList<>();
        this.start();
    }

    @Override
    public void run() {
        boolean exit = true;
        try {
            sSocket = new ServerSocket(Integer.valueOf(puerto));
            ventana.addServer();
            while (exit) {
                HiloCliente hCl;
                Socket socket;
                socket = sSocket.accept();
                System.out.println("Conexion aceptada: " + socket);
                hCl = new HiloCliente(socket, this);
                hCl.start();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventana, "Conexion fallida");
            System.exit(0);
        }
    }

    public LinkedList<String> getUsuarios() {
        LinkedList<String>users = new LinkedList<>();
        clientes.stream().forEach(u -> users.add(u.getIde()));
        return users;
    }

    public void addLog(String log){
        ventana.addLog(log);
    } 
}
