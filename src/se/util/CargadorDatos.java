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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class CargadorDatos {
    public static TablaID3 cargarDesdeCSV(File archivo) throws Exception {
        TablaID3 tabla = new TablaID3();
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            // Leer encabezados
            String linea = br.readLine();
            if (linea != null) {
                Arrays.stream(linea.split(","))
                      .map(String::trim)
                      .forEach(tabla::agregarColumna);
            }
            
            // Leer datos
            while ((linea = br.readLine()) != null) {
                tabla.agregarFila(
                    Arrays.asList(linea.split(","))
                );
            }
        }
        
        return tabla;
    }
}
