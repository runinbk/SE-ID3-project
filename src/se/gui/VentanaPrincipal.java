/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package se.gui;

/**
 *
 * @author braya
 */

import se.model.Nodo;
import se.model.TablaID3;
import se.util.CargadorDatos;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import se.util.ID3Calculator;


public class VentanaPrincipal extends javax.swing.JFrame {
    // Declaración de componentes
    private JPanel panelPrincipal;
    private PanelTabla panelTabla;
    private PanelArbol panelArbol;
    private JSplitPane splitPane;
    private JButton btnCargar;
    private JButton btnConstruirArbol;
    private JComboBox<String> comboColumnaObjetivo;
    private Nodo arbolID3;

    public VentanaPrincipal() {
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
    }

    private void inicializarComponentes() {
        // Inicializar paneles principales
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelTabla = new PanelTabla();
        panelArbol = new PanelArbol();

        // Inicializar controles
        btnCargar = new JButton("Cargar CSV");
        btnConstruirArbol = new JButton("Construir Árbol");
        comboColumnaObjetivo = new JComboBox<>();

        // Configurar botón construir (inicialmente deshabilitado)
        btnConstruirArbol.setEnabled(false);

        // Crear panel de controles
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelControles.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelControles.add(btnCargar);
        panelControles.add(new JLabel("Columna Objetivo:"));
        panelControles.add(comboColumnaObjetivo);
        panelControles.add(btnConstruirArbol);

        // Configurar paneles con scroll
        JScrollPane scrollTabla = new JScrollPane(panelTabla);
        JScrollPane scrollArbol = new JScrollPane(panelArbol);

        // Configurar split pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTabla, scrollArbol);
        splitPane.setResizeWeight(0.5);

        // Agregar componentes al panel principal
        panelPrincipal.add(panelControles, BorderLayout.NORTH);
        panelPrincipal.add(splitPane, BorderLayout.CENTER);
    }

    private void configurarVentana() {
        setTitle("Implementación ID3 - Cálculos de Entropía");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setContentPane(panelPrincipal);
    }

    private void configurarEventos() {
        btnCargar.addActionListener(e -> cargarArchivo());
        btnConstruirArbol.addActionListener(e -> construirArbol());
        comboColumnaObjetivo.addActionListener(e -> actualizarColumnaObjetivo());
    }

    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                TablaID3 datos = CargadorDatos.cargarDesdeCSV(
                    fileChooser.getSelectedFile()
                );
                panelTabla.cargarDatos(datos);
                actualizarComboColumnas(datos);
                btnConstruirArbol.setEnabled(true);
                arbolID3 = null;
            } catch (Exception ex) {
                mostrarError("Error al cargar archivo: " + ex.getMessage());
                btnConstruirArbol.setEnabled(false);
            }
        }
    }

    private void construirArbol() {
        try {
            TablaID3 datos = panelTabla.getDatosID3();
            if (datos == null) {
                mostrarError("No hay datos cargados.");
                return;
            }

            if (comboColumnaObjetivo.getSelectedIndex() == -1) {
                mostrarError("Seleccione una columna objetivo.");
                return;
            }

            List<Integer> atributosDisponibles = new ArrayList<>();
            for (int i = 0; i < datos.getColumnas().size(); i++) {
                if (i != datos.getColumnaObjetivo()) {
                    atributosDisponibles.add(i);
                }
            }

            arbolID3 = ID3Calculator.construirArbol(datos, atributosDisponibles);
            panelArbol.setArbol(arbolID3);

            JOptionPane.showMessageDialog(this,
                "Árbol de decisión construido exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            mostrarError("Error al construir el árbol: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarComboColumnas(TablaID3 datos) {
        comboColumnaObjetivo.removeAllItems();
        datos.getColumnas().forEach(comboColumnaObjetivo::addItem);
    }

    private void actualizarColumnaObjetivo() {
        if (comboColumnaObjetivo.getSelectedIndex() != -1) {
            try {
                TablaID3 datos = panelTabla.getDatosID3();
                if (datos != null) {
                    datos.setColumnaObjetivo(comboColumnaObjetivo.getSelectedIndex());
                    panelTabla.resaltarColumnaObjetivo(comboColumnaObjetivo.getSelectedIndex());
                }
            } catch (Exception ex) {
                mostrarError("Error al actualizar columna objetivo: " + ex.getMessage());
            }
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE);
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
