/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package se.util;

/**
 *
 * @author braya
 */
import se.model.TablaID3;
import java.io.*;
import java.util.*;

public class CargadorDatos {
    public static TablaID3 cargarDesdeCSV(File archivo) throws Exception {
        TablaID3 tabla = new TablaID3();
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            // Leer encabezados
            String linea = br.readLine();
            if (linea == null || linea.trim().isEmpty()) {
                throw new Exception("El archivo está vacío o no contiene encabezados.");
            }

            // Procesar encabezados
            String[] encabezados = linea.split(",");
            for (String encabezado : encabezados) {
                tabla.agregarColumna(encabezado.trim());
            }

            // Leer datos
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] valores = linea.split(",");
                    List<String> fila = new ArrayList<>();
                    for (String valor : valores) {
                        fila.add(valor.trim());
                    }
                    tabla.agregarFila(fila);
                }
            }
        }
        return tabla;
    }
}