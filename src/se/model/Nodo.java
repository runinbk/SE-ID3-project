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

public class Nodo {
    private String valor;
    private List<Nodo> hijos;
    private boolean esFinal;
    
    public Nodo(String valor) {
        this.valor = valor;
        this.hijos = new ArrayList<>();
        this.esFinal = false;
    }
    
    // Getters y setters
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
    public List<Nodo> getHijos() { return hijos; }
    public boolean isEsFinal() { return esFinal; }
    public void setEsFinal(boolean esFinal) { this.esFinal = esFinal; }
    
    public void agregarHijo(Nodo hijo) {
        hijos.add(hijo);
    }
}
