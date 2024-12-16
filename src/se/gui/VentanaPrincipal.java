/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package se.gui;

/**
 *
 * @author braya
 */

import se.util.MathUtils;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import se.model.TablaID3;
import se.util.CargadorDatos;

public class VentanaPrincipal extends javax.swing.JFrame {
    // Declaración de componentes
    private JTextField txtTotal;
    private JTextField txtFavorables;
    private JLabel lblResultado;
    private JButton btnCalcular;
    private JPanel panelPrincipal;
    private JPanel panelEntropiaBasica;
    private PanelTabla panelTabla;
    private JButton btnCargar;
    private JComboBox<String> comboColumnaObjetivo;
    
    public VentanaPrincipal() {
        initComponents();
        configurarVentana();
        configurarPanelEntropiaBasica();
        configurarEventos();
        agregarComponentesTabla();
    }
    
    private void configurarVentana() {
        setTitle("Implementación ID3 - Cálculos de Entropía");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(panelPrincipal);
    }
    
    private void configurarPanelEntropiaBasica() {
        panelEntropiaBasica = new JPanel(new GridBagLayout());
        panelEntropiaBasica.setBorder(
            BorderFactory.createTitledBorder("Cálculo de Entropía Binaria")
        );
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Etiqueta y campo para total de casos
        gbc.gridx = 0; gbc.gridy = 0;
        panelEntropiaBasica.add(new JLabel("Total de casos (n):"), gbc);
        
        gbc.gridx = 1;
        txtTotal = new JTextField(10);
        panelEntropiaBasica.add(txtTotal, gbc);
        
        // Etiqueta y campo para casos favorables
        gbc.gridx = 0; gbc.gridy = 1;
        panelEntropiaBasica.add(new JLabel("Casos favorables:"), gbc);
        
        gbc.gridx = 1;
        txtFavorables = new JTextField(10);
        panelEntropiaBasica.add(txtFavorables, gbc);
        
        // Botón calcular
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        btnCalcular = new JButton("Calcular Entropía");
        panelEntropiaBasica.add(btnCalcular, gbc);
        
        // Etiqueta resultado
        gbc.gridy = 3;
        lblResultado = new JLabel("Resultado: ");
        lblResultado.setFont(new Font("Dialog", Font.BOLD, 14));
        panelEntropiaBasica.add(lblResultado, gbc);
        
        panelPrincipal.add(panelEntropiaBasica, BorderLayout.CENTER);
    }
    
    private void configurarEventos() {
        btnCalcular.addActionListener(e -> calcularEntropia());
    }
    
    private void calcularEntropia() {
        try {
            int total = Integer.parseInt(txtTotal.getText().trim());
            int favorables = Integer.parseInt(txtFavorables.getText().trim());
            
            if (total <= 0) {
                mostrarError("El total de casos debe ser mayor a 0");
                return;
            }
            
            if (favorables > total) {
                mostrarError("Los casos favorables no pueden ser mayores que el total");
                return;
            }
            
            if (favorables < 0) {
                mostrarError("Los casos favorables no pueden ser negativos");
                return;
            }
            
            double entropia = MathUtils.calcularEntropiaBinaria(total, favorables);
            lblResultado.setText(String.format("Resultado: E = %.4f", entropia));
            
        } catch (NumberFormatException ex) {
            mostrarError("Por favor ingrese números enteros válidos");
        }
    }
    
    private void agregarComponentesTabla() {
        // Panel para controles
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnCargar = new JButton("Cargar CSV");
        comboColumnaObjetivo = new JComboBox<>();

        panelControles.add(btnCargar);
        panelControles.add(new JLabel("Columna Objetivo:"));
        panelControles.add(comboColumnaObjetivo);

        // Panel tabla
        panelTabla = new PanelTabla();

        // Agregar al panel principal
        panelPrincipal.add(panelControles, BorderLayout.NORTH);
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);

        // Configurar eventos
        btnCargar.addActionListener(e -> cargarArchivo());
        comboColumnaObjetivo.addActionListener(e -> actualizarColumnaObjetivo());
    }

    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                // Cargar datos
                File archivoSeleccionado = fileChooser.getSelectedFile();
                TablaID3 datos = CargadorDatos.cargarDesdeCSV(archivoSeleccionado);

                // Validar datos
                CargadorDatos.validarDatos(datos);

                // Mostrar datos en la tabla
                panelTabla.cargarDatos(datos);
                actualizarComboColumnas(datos);

                JOptionPane.showMessageDialog(this, "Archivo cargado y validado correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar archivo: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarComboColumnas(TablaID3 datos) {
        comboColumnaObjetivo.removeAllItems();
        datos.getColumnas().forEach(comboColumnaObjetivo::addItem);
    }
    
    private void actualizarColumnaObjetivo() {
        if (comboColumnaObjetivo.getSelectedIndex() != -1) {
            try {
                int indiceSeleccionado = comboColumnaObjetivo.getSelectedIndex();
                TablaID3 datos = panelTabla.getDatosID3(); // Necesitamos agregar este getter en PanelTabla
                if (datos != null) {
                    datos.setColumnaObjetivo(indiceSeleccionado);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al actualizar columna objetivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, 
            "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
