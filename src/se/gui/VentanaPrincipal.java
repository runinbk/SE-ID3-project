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
import se.util.ID3Calculator;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class VentanaPrincipal extends javax.swing.JFrame {
    // Componentes principales
    private JPanel panelPrincipal;
    private PanelTabla panelTabla;
    private PanelArbol panelArbol;
    private JSplitPane splitPane;
    private PanelCalculos panelCalculos;
    private JTabbedPane panelDerecho;
    
    // Barra de herramientas
    private JToolBar toolBar;
    private JButton btnCargar;
    private JButton btnConstruirArbol;
    private JButton btnExportarImagen;  // Declarado aquí
    private JButton btnExportarTexto;   // Declarado aquí
    private JComboBox<String> comboColumnaObjetivo;
    private JButton btnZoomIn;
    private JButton btnZoomOut;
    private JButton btnResetZoom;
    
    // Modelo
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
        panelCalculos = new PanelCalculos();

        // Inicializar barra de herramientas
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        // Inicializar todos los botones
        btnCargar = new JButton("Cargar CSV");
        btnConstruirArbol = new JButton("Construir Árbol");
        btnExportarImagen = new JButton("Exportar Imagen");  // Inicializado aquí
        btnExportarTexto = new JButton("Exportar Texto");    // Inicializado aquí
        comboColumnaObjetivo = new JComboBox<>();
        btnZoomIn = new JButton("+");
        btnZoomOut = new JButton("-");
        btnResetZoom = new JButton("Reset");

        // Configurar botones
        btnConstruirArbol.setEnabled(false);
        btnExportarImagen.setEnabled(false);
        btnExportarTexto.setEnabled(false);
        btnZoomIn.setEnabled(false);
        btnZoomOut.setEnabled(false);
        btnResetZoom.setEnabled(false);

        // Agregar componentes a la barra de herramientas
        toolBar.add(btnCargar);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Columna Objetivo: "));
        toolBar.add(comboColumnaObjetivo);
        toolBar.addSeparator();
        toolBar.add(btnConstruirArbol);
        toolBar.addSeparator();
        toolBar.add(btnExportarImagen);
        toolBar.add(btnExportarTexto);
        toolBar.addSeparator();
        toolBar.add(btnZoomIn);
        toolBar.add(btnZoomOut);
        toolBar.add(btnResetZoom);

        // Crear panel con pestañas para el lado derecho
        panelDerecho = new JTabbedPane();
        panelDerecho.addTab("Árbol", new JScrollPane(panelArbol));
        panelDerecho.addTab("Cálculos", new JScrollPane(panelCalculos));

        // Modificar el splitPane para usar el panel con pestañas
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
            new JScrollPane(panelTabla), 
            panelDerecho);
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerLocation(400);
        
        // Configurar splitPane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
            new JScrollPane(panelTabla), 
            new JScrollPane(panelArbol));
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerLocation(400);

        // Agregar componentes al panel principal
        panelPrincipal.add(toolBar, BorderLayout.NORTH);
        panelPrincipal.add(splitPane, BorderLayout.CENTER);
    }

    private void configurarVentana() {
        setTitle("Implementación ID3 - Cálculos de Entropía");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setContentPane(panelPrincipal);

        // Agregar menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem menuItemCargar = new JMenuItem("Cargar CSV");
        JMenuItem menuItemSalir = new JMenuItem("Salir");
        
        menuArchivo.add(menuItemCargar);
        menuArchivo.addSeparator();
        menuArchivo.add(menuItemSalir);
        menuBar.add(menuArchivo);
        
        // Eventos del menú
        menuItemCargar.addActionListener(e -> cargarArchivo());
        menuItemSalir.addActionListener(e -> System.exit(0));
        
        setJMenuBar(menuBar);
    }

    private void configurarEventos() {
        btnCargar.addActionListener(e -> cargarArchivo());
        btnConstruirArbol.addActionListener(e -> construirArbol());
        btnExportarImagen.addActionListener(e -> exportarArbolComoImagen());
        btnExportarTexto.addActionListener(e -> exportarArbolComoTexto());
        comboColumnaObjetivo.addActionListener(e -> actualizarColumnaObjetivo());
        btnZoomIn.addActionListener(e -> panelArbol.zoomIn());
        btnZoomOut.addActionListener(e -> panelArbol.zoomOut());
        btnResetZoom.addActionListener(e -> panelArbol.resetView());
    }

    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "Archivos CSV (.csv)";
            }
        });

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                TablaID3 datos = CargadorDatos.cargarDesdeCSV(fileChooser.getSelectedFile());
                panelTabla.cargarDatos(datos);
                actualizarComboColumnas(datos);
                btnConstruirArbol.setEnabled(true);
                arbolID3 = null;
                
                // Deshabilitar controles de zoom hasta que haya árbol
                btnZoomIn.setEnabled(false);
                btnZoomOut.setEnabled(false);
                btnResetZoom.setEnabled(false);
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

            if (datos.getColumnaObjetivo() == -1) {
                mostrarError("Seleccione una columna objetivo.");
                return;
            }

            List<Integer> atributosDisponibles = new ArrayList<>();
            for (int i = 0; i < datos.getColumnas().size(); i++) {
                if (i != datos.getColumnaObjetivo()) {
                    atributosDisponibles.add(i);
                }
            }

            // Construir árbol
            arbolID3 = ID3Calculator.construirArbol(datos, atributosDisponibles);
            panelArbol.setArbol(arbolID3);

            // Actualizar panel de cálculos
            panelCalculos.actualizarCalculos(datos, atributosDisponibles);

            // Habilitar controles
            btnZoomIn.setEnabled(true);
            btnZoomOut.setEnabled(true);
            btnResetZoom.setEnabled(true);
            btnExportarImagen.setEnabled(true);
            btnExportarTexto.setEnabled(true);

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
                    arbolID3 = null; // Limpiar árbol al cambiar objetivo
                    panelArbol.setArbol(null);
                    
                    // Deshabilitar controles de zoom
                    btnZoomIn.setEnabled(false);
                    btnZoomOut.setEnabled(false);
                    btnResetZoom.setEnabled(false);
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
    
    // Agregar métodos de exportación
    private void exportarArbolComoImagen() {
        if (arbolID3 == null) {
            mostrarError("No hay árbol para exportar.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getParentFile(), file.getName() + ".png");
                }

                // Crear una imagen del tamaño del panel
                Dimension size = panelArbol.getPreferredSize();
                BufferedImage image = new BufferedImage(
                    size.width, size.height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = image.createGraphics();
                panelArbol.paint(g2d);
                g2d.dispose();

                ImageIO.write(image, "png", file);
                JOptionPane.showMessageDialog(this, "Árbol exportado exitosamente");
            } catch (IOException ex) {
                mostrarError("Error al exportar el árbol: " + ex.getMessage());
            }
        }
    }

    private void exportarArbolComoTexto() {
        if (arbolID3 == null) {
            mostrarError("No hay árbol para exportar.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".txt")) {
                    file = new File(file.getParentFile(), file.getName() + ".txt");
                }

                try (PrintWriter writer = new PrintWriter(file)) {
                    exportarNodoComoTexto(arbolID3, "", writer);
                }
                JOptionPane.showMessageDialog(this, "Árbol exportado exitosamente");
            } catch (IOException ex) {
                mostrarError("Error al exportar el árbol: " + ex.getMessage());
            }
        }
    }

    


    private void exportarNodoComoTexto(Nodo nodo, String prefijo, PrintWriter writer) {
        if (nodo == null) return;
        if (nodo.isEsHoja()) {
            writer.println(prefijo + "└─ " + nodo.getClasificacion());
        } else {
            writer.println(prefijo + "├─ " + nodo.getAtributo());
            Map<String, Nodo> hijos = nodo.getHijos();
            int contador = 0;
            for (Map.Entry<String, Nodo> entry : hijos.entrySet()) {
                contador++;
                boolean esUltimo = contador == hijos.size();
                writer.println(prefijo + "│  " + (esUltimo ? "└" : "├") + "─ " + entry.getKey());
                exportarNodoComoTexto(entry.getValue(), 
                    prefijo + "│  " + (esUltimo ? "    " : "│  "), 
                    writer);
            }
        }
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Error al establecer el Look & Feel del sistema");
        }

        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
