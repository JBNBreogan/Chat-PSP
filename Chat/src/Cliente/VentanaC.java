package Cliente;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * VentanaC es la ventana principal de la aplicación cliente en una interfaz gráfica que permite la comunicación entre usuarios.
 * La ventana permite enviar mensajes, seleccionar destinatarios y mostrar el historial de mensajes.
 */
public class VentanaC extends javax.swing.JFrame {

    /**
     * Crea una nueva instancia de la ventana, inicializando los componentes gráficos y configurando la conexión con el servidor.
     */
    public VentanaC() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String ip_puerto_nombre[] = getIP_Puerto_Nombre();
        String ip = ip_puerto_nombre[0];
        String puerto = ip_puerto_nombre[1];
        String nombre = ip_puerto_nombre[2];
        cliente = new Cliente(this, ip, Integer.valueOf(puerto), nombre);
    }

    /**
     * Método generado automáticamente para inicializar los componentes de la interfaz gráfica.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Inicialización de los componentes gráficos
        jScrollPane1 = new javax.swing.JScrollPane();
        txtHistorial = new javax.swing.JTextArea();
        txtMensaje = new javax.swing.JTextField();
        cmbContactos = new javax.swing.JComboBox();
        btnEnviar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtHistorial.setEditable(false);
        txtHistorial.setColumns(20);
        txtHistorial.setRows(5);
        jScrollPane1.setViewportView(txtHistorial);

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        jLabel1.setText("Destinatario:");

        // Configuración del layout y organización de los componentes
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtMensaje)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEnviar))
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbContactos, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbContactos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEnviar))
                .addContainerGap())
        );

        pack();
    }

    /**
     * Acción del botón "Enviar". Envía un mensaje al contacto seleccionado.
     * Muestra un mensaje de error si no se selecciona un contacto.
     * 
     * @param evt el evento del botón "Enviar"
     */
    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {
        if (cmbContactos.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe escoger un destinatario válido, si no \n"
                    + "hay uno, espere a que otro usuario se conecte\n"
                    + "para poder chatear con él.");
            return;
        }
        String cliente_receptor = cmbContactos.getSelectedItem().toString();
        String mensaje = txtMensaje.getText();
        cliente.enviarMensaje(cliente_receptor, mensaje);
        txtHistorial.append("## Yo -> " + cliente_receptor + " ## : \n" + mensaje + "\n");
        txtMensaje.setText("");
    }

    /**
     * Método que se ejecuta cuando la ventana se cierra, confirmando la desconexión del cliente.
     * 
     * @param evt el evento de cierre de la ventana
     */
    private void formWindowClosed(java.awt.event.WindowEvent evt) {
    }

    /**
     * Método que se ejecuta cuando la ventana está a punto de cerrarse, enviando la confirmación de desconexión.
     * 
     * @param evt el evento de cierre de la ventana
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        cliente.confirmarDesconexion();
    }

    /**
     * Método principal que inicializa la ventana de la aplicación.
     * 
     * @param args los argumentos de línea de comandos
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
            java.util.logging.Logger.getLogger(VentanaC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

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

    private final String DEFAULT_PORT = "10101";
    private final String DEFAULT_IP = "127.0.0.1";
    private final Cliente cliente;

    /**
     * Añade un contacto al ComboBox de contactos.
     * 
     * @param contacto el nombre del contacto a añadir
     */
    void addContacto(String contacto) {
        cmbContactos.addItem(contacto);
    }

    /**
     * Añade un mensaje recibido al historial de mensajes.
     * 
     * @param emisor el emisor del mensaje
     * @param mensaje el contenido del mensaje
     */
    void addMensaje(String emisor, String mensaje) {
        txtHistorial.append("##### " + emisor + " ##### : \n" + mensaje + "\n");
    }

    /**
     * Muestra el nombre de usuario en el título de la ventana cuando se ha iniciado la sesión.
     * 
     * @param identificador el nombre del usuario
     */
    void sesionIniciada(String identificador) {
        this.setTitle(" --- " + identificador + " --- ");
    }

    /**
     * Obtiene la dirección IP, puerto y nombre de usuario desde un cuadro de diálogo de configuración.
     * Si el usuario cancela la configuración, el programa se cierra.
     * 
     * @return un arreglo con la IP, el puerto y el nombre de usuario
     */
    private String[] getIP_Puerto_Nombre() {
        String s[] = new String[3];
        s[0] = DEFAULT_IP;
        s[1] = DEFAULT_PORT;
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
            s[0] = ip.getText();
            s[1] = puerto.getText();
            s[2] = usuario.getText();
        } else {
            System.exit(0);
        }
        return s;
    }

    /**
     * Elimina un contacto de la lista de contactos.
     * 
     * @param identificador el nombre del contacto a eliminar
     */
    void eliminarContacto(String identificador) {
        for (int i = 0; i < cmbContactos.getItemCount(); i++) {
            if (cmbContactos.getItemAt(i).toString().equals(identificador)) {
                cmbContactos.removeItemAt(i);
                return;
            }
        }
    }
}
