/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package se.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.imageio.ImageIO;
import se.gui.PanelArbol;
import se.model.Nodo;

/**
 *
 * @author braya
 */
public class ExportadorArbol {
    public static void exportarArbolComoImagen(PanelArbol panelArbol, File archivo) throws IOException {
        // Crear una imagen del tamaño del panel
        Dimension size = panelArbol.getPreferredSize();
        BufferedImage image = new BufferedImage(
            size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Configurar renderizado de alta calidad
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
        
        // Pintar el panel en la imagen
        panelArbol.paintComponent(g2d);
        g2d.dispose();
        
        // Guardar la imagen
        String extension = archivo.getName().substring(
            archivo.getName().lastIndexOf('.') + 1);
        ImageIO.write(image, extension, archivo);
    }
    
    public static void exportarArbolComoTexto(Nodo raiz, File archivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(archivo)) {
            exportarNodo(raiz, "", writer);
        }
    }
    
    private static void exportarNodo(Nodo nodo, String prefijo, PrintWriter writer) {
        if (nodo == null) return;
        
        if (nodo.isEsHoja()) {
            writer.println(prefijo + "└─ " + nodo.getClasificacion());
        } else {
            writer.println(prefijo + "├─ " + nodo.getAtributo());
            String nuevoPrefijo = prefijo + "│  ";
            
            for (Map.Entry<String, Nodo> entry : nodo.getHijos().entrySet()) {
                writer.println(nuevoPrefijo + "├─ [" + entry.getKey() + "]");
                exportarNodo(entry.getValue(), nuevoPrefijo + "│  ", writer);
            }
        }
    }
}
