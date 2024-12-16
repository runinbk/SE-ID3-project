/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package se.util;

/**
 *
 * @author braya
 */
import se.model.Nodo;
import se.model.TablaID3;
import java.util.*;

public class ID3Calculator {
    
    // Calcula la entropía para un conjunto de datos
    public static double calcularEntropiaConjunto(List<String> valores) {
        Map<String, Integer> conteo = new HashMap<>();
        int total = valores.size();
        
        // Contar ocurrencias de cada valor
        for (String valor : valores) {
            conteo.merge(valor, 1, Integer::sum);
        }
        
        double entropia = 0.0;
        for (int count : conteo.values()) {
            double proporcion = (double) count / total;
            entropia -= proporcion * MathUtils.log2(proporcion);
        }
        
        return entropia;
    }
    
    // Calcula la ganancia de información para un atributo
    public static double calcularGanancia(TablaID3 tabla, int columnaAtributo) {
        // Validar índices
        if (columnaAtributo < 0 || columnaAtributo >= tabla.getColumnas().size() ||
            tabla.getColumnaObjetivo() < 0 || tabla.getColumnaObjetivo() >= tabla.getColumnas().size()) {
            throw new IllegalArgumentException("Índice de columna inválido");
        }

        List<String> columnaMeta = obtenerColumna(tabla, tabla.getColumnaObjetivo());
        double entropiaTotal = calcularEntropiaConjunto(columnaMeta);
        
        List<String> columnaAtrib = obtenerColumna(tabla, columnaAtributo);
        Map<String, List<String>> valoresPorAtributo = new HashMap<>();
        
        // Agrupar valores meta por valor de atributo
        for (int i = 0; i < columnaAtrib.size(); i++) {
            String valorAtrib = columnaAtrib.get(i);
            String valorMeta = columnaMeta.get(i);
            
            if (!valoresPorAtributo.containsKey(valorAtrib)) {
                valoresPorAtributo.put(valorAtrib, new ArrayList<>());
            }
            valoresPorAtributo.get(valorAtrib).add(valorMeta);
        }
        
        // Calcular entropía ponderada
        double entropiaPonderada = 0.0;
        int totalInstancias = columnaMeta.size();
        
        for (Map.Entry<String, List<String>> entry : valoresPorAtributo.entrySet()) {
            List<String> subconjunto = entry.getValue();
            double peso = (double) subconjunto.size() / totalInstancias;
            double entropiaSubconjunto = calcularEntropiaConjunto(subconjunto);
            entropiaPonderada += peso * entropiaSubconjunto;
        }
        
        return entropiaTotal - entropiaPonderada;
    }
    
    // Construye el árbol ID3
    public static Nodo construirArbol(TablaID3 tabla, List<Integer> atributosDisponibles) {
        // Validaciones iniciales
        if (tabla == null || tabla.getDatos().isEmpty() || atributosDisponibles == null) {
            throw new IllegalArgumentException("Datos inválidos para construir el árbol");
        }

        List<String> columnaMeta = obtenerColumna(tabla, tabla.getColumnaObjetivo());
        
        // Si todos los ejemplos son de la misma clase
        if (new HashSet<>(columnaMeta).size() == 1) {
            return new Nodo(null, columnaMeta.get(0));
        }
        
        // Si no quedan atributos
        if (atributosDisponibles.isEmpty()) {
            return new Nodo(null, obtenerValorMasFrecuente(columnaMeta));
        }
        
        // Encontrar el mejor atributo
        int mejorAtributo = encontrarMejorAtributo(tabla, atributosDisponibles);
        if (mejorAtributo == -1) {
            return new Nodo(null, obtenerValorMasFrecuente(columnaMeta));
        }

        Nodo raiz = new Nodo(tabla.getColumnas().get(mejorAtributo));
        
        // Resto del código igual...
        return raiz;
    }
    
    // Métodos auxiliares
    private static List<String> obtenerColumna(TablaID3 tabla, int columna) {
        List<String> resultado = new ArrayList<>();
        for (List<String> fila : tabla.getDatos()) {
            resultado.add(fila.get(columna));
        }
        return resultado;
    }
    
    private static String obtenerValorMasFrecuente(List<String> valores) {
        Map<String, Long> frecuencias = valores.stream()
            .collect(HashMap::new, 
                    (m, v) -> m.merge(v, 1L, Long::sum),
                    HashMap::putAll);
        
        return Collections.max(frecuencias.entrySet(), 
            Map.Entry.comparingByValue()).getKey();
    }
    
    private static int encontrarMejorAtributo(TablaID3 tabla, List<Integer> atributosDisponibles) {
        if (atributosDisponibles.isEmpty()) {
            return -1;
        }

        int mejorAtributo = atributosDisponibles.get(0);
        double mayorGanancia = -1;

        for (int atributo : atributosDisponibles) {
            double ganancia = calcularGanancia(tabla, atributo);
            if (ganancia > mayorGanancia) {
                mayorGanancia = ganancia;
                mejorAtributo = atributo;
            }
        }

        return mejorAtributo;
    }
    
    private static TablaID3 obtenerSubtabla(TablaID3 tabla, 
                                          int columnaAtributo, 
                                          String valor) {
        TablaID3 subTabla = new TablaID3();
        tabla.getColumnas().forEach(subTabla::agregarColumna);
        
        for (List<String> fila : tabla.getDatos()) {
            if (fila.get(columnaAtributo).equals(valor)) {
                subTabla.agregarFila(new ArrayList<>(fila));
            }
        }
        
        return subTabla;
    }
}
