/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package se.gui;

/**
 *
 * @author braya
 */
import se.model.TablaID3;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelTabla extends JPanel {
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private TablaID3 datosID3;
    
    public PanelTabla() {
        setLayout(new BorderLayout());
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla = new JTable(modeloTabla);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void cargarDatos(TablaID3 datos) {
        this.datosID3 = datos;
        actualizarTabla();
    }
    
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);
        
        // Agregar columnas
        datosID3.getColumnas().forEach(modeloTabla::addColumn);
        
        // Agregar filas
        datosID3.getDatos().forEach(fila -> 
            modeloTabla.addRow(fila.toArray())
        );
    }
}
