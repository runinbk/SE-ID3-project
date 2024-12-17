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
            
            valoresPorAtributo.computeIfAbsent(valorAtrib, k -> new ArrayList<>())
                             .add(valorMeta);
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
    
    // Construye el árbol ID3 - Implementación completa
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

        // Crear nodo raíz con el mejor atributo
        Nodo raiz = new Nodo(tabla.getColumnas().get(mejorAtributo));
        
        // Obtener todos los valores únicos del mejor atributo
        List<String> columnaAtributo = obtenerColumna(tabla, mejorAtributo);
        Set<String> valoresAtributo = new HashSet<>(columnaAtributo);
        
        // Para cada valor posible del atributo
        for (String valor : valoresAtributo) {
            // Crear subconjunto de datos para este valor
            TablaID3 subTabla = obtenerSubtabla(tabla, mejorAtributo, valor);
            
            // Si el subconjunto está vacío
            if (subTabla.getDatos().isEmpty()) {
                raiz.agregarHijo(valor, new Nodo(valor, obtenerValorMasFrecuente(columnaMeta)));
            } else {
                // Crear nueva lista de atributos disponibles excluyendo el actual
                List<Integer> nuevosAtributos = new ArrayList<>(atributosDisponibles);
                nuevosAtributos.remove(Integer.valueOf(mejorAtributo));
                
                // Recursivamente construir el subárbol
                subTabla.setColumnaObjetivo(tabla.getColumnaObjetivo());
                Nodo subArbol = construirArbol(subTabla, nuevosAtributos);
                raiz.agregarHijo(valor, subArbol);
            }
        }
        
        return raiz;
    }
    
    // Métodos auxiliares mejorados
    public static List<String> obtenerColumna(TablaID3 tabla, int columna) {
        if (columna < 0 || columna >= tabla.getColumnas().size()) {
            throw new IllegalArgumentException("Índice de columna fuera de rango");
        }
        
        List<String> resultado = new ArrayList<>();
        for (List<String> fila : tabla.getDatos()) {
            if (columna < fila.size()) {
                resultado.add(fila.get(columna));
            } else {
                throw new IllegalStateException("Inconsistencia en los datos: fila incompleta");
            }
        }
        return resultado;
    }
    
    private static String obtenerValorMasFrecuente(List<String> valores) {
        if (valores == null || valores.isEmpty()) {
            throw new IllegalArgumentException("Lista de valores vacía");
        }
        
        Map<String, Long> frecuencias = valores.stream()
            .filter(Objects::nonNull)
            .collect(HashMap::new, 
                    (m, v) -> m.merge(v, 1L, Long::sum),
                    HashMap::putAll);
        
        if (frecuencias.isEmpty()) {
            throw new IllegalStateException("No hay valores válidos para procesar");
        }
        
        return Collections.max(frecuencias.entrySet(), 
            Map.Entry.comparingByValue()).getKey();
    }
    
    private static int encontrarMejorAtributo(TablaID3 tabla, List<Integer> atributosDisponibles) {
        if (atributosDisponibles.isEmpty()) {
            return -1;
        }

        return atributosDisponibles.stream()
            .max(Comparator.comparingDouble(atributo -> 
                calcularGanancia(tabla, atributo)))
            .orElse(-1);
    }
    
    private static TablaID3 obtenerSubtabla(TablaID3 tabla, int columnaAtributo, String valor) {
        TablaID3 subTabla = new TablaID3();
        
        // Copiar estructura de columnas
        tabla.getColumnas().forEach(subTabla::agregarColumna);
        
        // Filtrar filas que coincidan con el valor del atributo
        for (List<String> fila : tabla.getDatos()) {
            if (fila.get(columnaAtributo).equals(valor)) {
                subTabla.agregarFila(new ArrayList<>(fila));
            }
        }
        
        // Mantener la misma columna objetivo
        subTabla.setColumnaObjetivo(tabla.getColumnaObjetivo());
        
        return subTabla;
    }
}
