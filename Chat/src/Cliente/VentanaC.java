package Cliente;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase VentanaC representa la interfaz gráfica del cliente para la comunicación en un chat.
 * Permite la conexión con un servidor y la interacción con otros usuarios.
 */
public class VentanaC extends javax.swing.JFrame {

    /**
     * Constructor de la clase VentanaC. Inicializa la ventana y establece la conexión con el servidor.
     */
    public VentanaC() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String ip_puerto_nombre[]=getIP_Puerto_Nombre();
        String ip=ip_puerto_nombre[0];
        String puerto=ip_puerto_nombre[1];
        String nombre=ip_puerto_nombre[2];
        cliente=new Cliente(this, ip, Integer.valueOf(puerto), nombre);
    }

    /**
     * Inicializa los componentes gráficos de la interfaz.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Código de inicialización de la interfaz
    }

    /**
     * Maneja el evento de clic en el botón enviar.
     * @param evento Evento de acción
     */
    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evento) {
        if(cmbContactos.getSelectedItem()==null){
            JOptionPane.showMessageDialog(this, "Debe escoger un destinatario válido.");        
            return;
        }
        String cliente_receptor=cmbContactos.getSelectedItem().toString();
        String mensaje=txtMensaje.getText();
        cliente.enviarMensaje(cliente_receptor, mensaje);
        txtHistorial.append("## Yo -> "+cliente_receptor+ " ## : \n" + mensaje+"\n");
        txtMensaje.setText("");
    }

    /**
     * Maneja el evento de cierre de la ventana.
     * @param evento Evento de ventana
     */
    private void formWindowClosing(java.awt.event.WindowEvent evento) {
        cliente.confirmarDesconexion();
    }

    /**
     * Método principal para ejecutar la aplicación.
     * @param args Argumentos de la línea de comandos
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaC().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnEnviar;
    private javax.swing.JComboBox cmbContactos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtHistorial;
    private javax.swing.JTextField txtMensaje;

    private final String DEFAULT_PORT="10101";
    private final String DEFAULT_IP="127.0.0.1";
    private final Cliente cliente;

    /**
     * Agrega un nuevo contacto a la lista de contactos.
     * @param nombre Nombre del contacto a agregar
     */
    void addContacto(String nombre) {
        cmbContactos.addItem(nombre);
    }
    
    /**
     * Agrega un mensaje recibido al historial de mensajes.
     * @param emisor Nombre del emisor del mensaje
     * @param mensaje Contenido del mensaje
     */
    void addMensaje(String emisor, String mensaje) {
        txtHistorial.append("##### "+emisor + " ##### : \n" + mensaje+"\n");
    }
    
    /**
     * Establece el título de la ventana con el identificador del usuario.
     * @param id Nombre de usuario
     */
    void sesionIniciada(String id) {
        this.setTitle(" --- "+id+" --- ");
    }
    
    /**
     * Muestra un cuadro de diálogo para obtener la IP, puerto y nombre de usuario.
     * @return Arreglo con IP, puerto y nombre del usuario
     */
    private String[] getIP_Puerto_Nombre() {
        String s[]=new String[3];
        s[0]=DEFAULT_IP;
        s[1]=DEFAULT_PORT;
        JTextField ip = new JTextField(20);
        JTextField puerto = new JTextField(20);
        JTextField usuario = new JTextField(20);
        ip.setText(DEFAULT_IP);
        puerto.setText(DEFAULT_PORT);
        usuario.setText("Usuario");
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(3, 2));
        myPanel.add(new JLabel("IP del Servidor:"));
        myPanel.add(ip);
        myPanel.add(new JLabel("Puerto de la conexión:"));
        myPanel.add(puerto);
        myPanel.add(new JLabel("Escriba su nombre:"));
        myPanel.add(usuario);        
        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                 "Configuraciones de la comunicación", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
                s[0]=ip.getText();
                s[1]=puerto.getText();
                s[2]=usuario.getText();
        }else{
            System.exit(0);
        }
        return s;
    }    
    
    /**
     * Elimina un contacto de la lista de contactos.
     * @param id Identificador del contacto a eliminar
     */
    void eliminarContacto(String id) {
        for (int i = 0; i < cmbContactos.getItemCount(); i++) {
            if(cmbContactos.getItemAt(i).toString().equals(id)){
                cmbContactos.removeItemAt(i);
                return;
            }
        }
    }
}