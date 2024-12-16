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
import java.util.List;

public class CargadorDatos {

    public static TablaID3 cargarDesdeCSV(File archivo) throws Exception {
        TablaID3 tabla = new TablaID3();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            // Leer encabezados
            String linea = br.readLine();
            if (linea == null || linea.trim().isEmpty()) {
                throw new Exception("El archivo está vacío o no contiene encabezados.");
            }

            Arrays.stream(linea.split(","))
                    .map(String::trim)
                    .forEach(tabla::agregarColumna);

            // Leer y validar datos
            int numeroColumnas = tabla.getColumnas().size();
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                if (valores.length != numeroColumnas) {
                    throw new Exception("Inconsistencia en el número de columnas en las filas.");
                }
                tabla.agregarFila(Arrays.asList(valores));
            }

            if (tabla.getDatos().isEmpty()) {
                throw new Exception("El archivo no contiene datos.");
            }
        }

        return tabla;
    }

    public static void validarDatos(TablaID3 tabla) throws Exception {
        // Validar que la columna objetivo está definida
        if (tabla.getColumnaObjetivo() == -1) {
            throw new Exception("La columna objetivo no está definida.");
        }

        // Validar que los valores de la columna objetivo son categóricos
        int columnaObjetivo = tabla.getColumnaObjetivo();
        for (List<String> fila : tabla.getDatos()) {
            if (fila.get(columnaObjetivo).matches("-?\\d+(\\.\\d+)?")) {
                throw new Exception("La columna objetivo contiene valores numéricos. Se requieren valores categóricos.");
            }
        }
    }
}
