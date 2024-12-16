/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package se.model;

/**
 *
 * @author braya
 */
import java.util.ArrayList;
import java.util.List;

public class TablaID3 {
    private List<String> columnas;
    private List<List<String>> datos;
    private int columnaObjetivo;
    
    public TablaID3() {
        columnas = new ArrayList<>();
        datos = new ArrayList<>();
        columnaObjetivo = -1;
    }
    
    public void agregarColumna(String nombre) {
        columnas.add(nombre);
    }
    
    public void agregarFila(List<String> fila) {
        if (fila.size() == columnas.size()) {
            datos.add(new ArrayList<>(fila));
        }
    }
    
    public void setColumnaObjetivo(int index) {
        if (index >= 0 && index < columnas.size()) {
            columnaObjetivo = index;
        }
    }
    
    // Getters
    public List<String> getColumnas() { return columnas; }
    public List<List<String>> getDatos() { return datos; }
    public int getColumnaObjetivo() { return columnaObjetivo; }
}
