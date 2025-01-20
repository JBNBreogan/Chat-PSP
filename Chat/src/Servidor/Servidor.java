package Servidor;
import java.net.ServerSocket;
import java.util.LinkedList;

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
        try {
            sSocket = new ServerSocket(Integer.valueOf(puerto));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
