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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nodo {
    private String atributo;          // nombre del atributo de decisión
    private String valorAtributo;     // valor que lleva a este nodo
    private String clasificacion;     // valor de clasificación (solo para hojas)
    private Map<String, Nodo> hijos;  // nodos hijos por valor de atributo
    private boolean esHoja;

    public Nodo(String atributo) {
        this.atributo = atributo;
        this.hijos = new HashMap<>();
        this.esHoja = false;
    }

    // Constructor para nodos hoja
    public Nodo(String valorAtributo, String clasificacion) {
        this.valorAtributo = valorAtributo;
        this.clasificacion = clasificacion;
        this.hijos = new HashMap<>();
        this.esHoja = true;
    }

    public void agregarHijo(String valor, Nodo hijo) {
        hijos.put(valor, hijo);
    }

    // Getters y setters
    public String getAtributo() { return atributo; }
    public String getValorAtributo() { return valorAtributo; }
    public String getClasificacion() { return clasificacion; }
    public Map<String, Nodo> getHijos() { return hijos; }
    public boolean isEsHoja() { return esHoja; }
}
