package Servidor;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Clase que ejecuta la parte gráfica del servidor
 * @author Breogan
 */
public class VentanaS  extends JFrame{

    /** Puerto por defecto del servidor */
    private final String DEFAULT_PORT="10101";

    /** Servidor a ejecutar */
    private final Servidor servidor;

    /**
     * Constructor de la ventana
     */
    public VentanaS() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String puerto=getPuerto();
        servidor=new Servidor(puerto, this);
    }

    /**
     * Inicia los componentes gráficos de la ventana
     */
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtClientes = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Servidor");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Log del Servidor"));

        txtClientes.setEditable(false);
        txtClientes.setColumns(20);
        txtClientes.setRows(5);
        jScrollPane1.setViewportView(txtClientes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE).addContainerGap()));

        pack();
    }

    /**
     * Ejecuta toda la lógica de la ventana y del servidor
     * @param args
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaS().setVisible(true);
            }
        });
    }

    /** Panel de scroll */
    private javax.swing.JScrollPane jScrollPane1;
    /** Área de texto de los clientes */
    private javax.swing.JTextArea txtClientes;

    /**
     * Añade un log al servidor, (usado para mostrar los usuarios conectados)
     * @param texto Texto a escribir
     */
    void addLog(String texto) {
        txtClientes.append(texto);
    }

    /**
     * Devuelve el puerto en caso de querer usar uno personalizado, en otro caso
     * devuelve el por defecto
     * @return Puerto a usar
     */
    private String getPuerto() {
        String p=DEFAULT_PORT;
        JTextField puerto = new JTextField(20);
        puerto.setText(p);
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(2, 1));
        myPanel.add(new JLabel("Puerto de la conexión:"));
        myPanel.add(puerto);
        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                 "Configuraciones de la comunicación", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
                p=puerto.getText();
        }else{
            System.exit(0);
        }
        return p;
    }
    void addServer() {
        txtClientes.setText("Inicializando el servidor... [Ok].");
    }        
}
