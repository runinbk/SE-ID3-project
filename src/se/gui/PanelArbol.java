/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package se.gui;

/**
 *
 * @author braya
 */

import se.model.Nodo;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PanelArbol extends javax.swing.JPanel {
    
    private Nodo raiz;
    private int nodoDiametro = 40;
    private int nivelAltura = 80;
    private int margenHorizontal = 50;
    private Map<Nodo, Point> posicionesNodos;
    private Map<Nodo, Dimension> dimensionesSubarboles;

    public PanelArbol() {
        setBackground(Color.WHITE);
        posicionesNodos = new HashMap<>();
        dimensionesSubarboles = new HashMap<>();
    }

    public void setArbol(Nodo raiz) {
        this.raiz = raiz;
        posicionesNodos.clear();
        dimensionesSubarboles.clear();
        if (raiz != null) {
            calcularDimensiones(raiz);
            calcularPosiciones(raiz, getWidth() / 2, 30, getWidth());
        }
        repaint();
    }

    private Dimension calcularDimensiones(Nodo nodo) {
        if (nodo == null || nodo.isEsHoja()) {
            dimensionesSubarboles.put(nodo, new Dimension(nodoDiametro, nodoDiametro));
            return dimensionesSubarboles.get(nodo);
        }

        int ancho = 0;
        int alto = nodoDiametro;

        for (Nodo hijo : nodo.getHijos().values()) {
            Dimension dim = calcularDimensiones(hijo);
            ancho += dim.width + margenHorizontal;
            alto = Math.max(alto, dim.height);
        }

        alto += nivelAltura;
        dimensionesSubarboles.put(nodo, new Dimension(ancho, alto));
        return dimensionesSubarboles.get(nodo);
    }

    private void calcularPosiciones(Nodo nodo, int x, int y, int espacioDisponible) {
        if (nodo == null) return;

        posicionesNodos.put(nodo, new Point(x, y));

        if (!nodo.isEsHoja()) {
            int numHijos = nodo.getHijos().size();
            int espacioTotal = 0;
            
            // Calcular espacio total necesario
            for (Nodo hijo : nodo.getHijos().values()) {
                espacioTotal += dimensionesSubarboles.get(hijo).width;
            }
            espacioTotal += margenHorizontal * (numHijos - 1);

            // Posicionar hijos
            int xActual = x - espacioTotal / 2;
            for (Map.Entry<String, Nodo> entry : nodo.getHijos().entrySet()) {
                Nodo hijo = entry.getValue();
                int anchoHijo = dimensionesSubarboles.get(hijo).width;
                calcularPosiciones(hijo, xActual + anchoHijo / 2, y + nivelAltura, anchoHijo);
                xActual += anchoHijo + margenHorizontal;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (raiz != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            dibujarArbol(g2d, raiz);
        }
    }

    private void dibujarArbol(Graphics2D g2d, Nodo nodo) {
        if (nodo == null) return;

        Point posicion = posicionesNodos.get(nodo);
        if (posicion == null) return;

        // Dibujar conexiones con los hijos
        g2d.setColor(Color.BLACK);
        for (Map.Entry<String, Nodo> entry : nodo.getHijos().entrySet()) {
            Nodo hijo = entry.getValue();
            Point posHijo = posicionesNodos.get(hijo);
            if (posHijo != null) {
                g2d.drawLine(posicion.x, posicion.y, posHijo.x, posHijo.y);
                
                // Dibujar el valor de la rama
                String valorRama = entry.getKey();
                int xTexto = (posicion.x + posHijo.x) / 2;
                int yTexto = (posicion.y + posHijo.y) / 2;
                g2d.drawString(valorRama, xTexto, yTexto);
            }
        }

        // Dibujar nodo
        g2d.setColor(new Color(200, 220, 255));
        g2d.fillOval(posicion.x - nodoDiametro/2, posicion.y - nodoDiametro/2, 
                     nodoDiametro, nodoDiametro);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(posicion.x - nodoDiametro/2, posicion.y - nodoDiametro/2, 
                     nodoDiametro, nodoDiametro);

        // Dibujar texto del nodo
        String textoNodo = nodo.isEsHoja() ? nodo.getClasificacion() : nodo.getAtributo();
        if (textoNodo != null) {
            FontMetrics fm = g2d.getFontMetrics();
            int textoX = posicion.x - fm.stringWidth(textoNodo) / 2;
            int textoY = posicion.y + fm.getHeight() / 4;
            g2d.drawString(textoNodo, textoX, textoY);
        }

        // Dibujar hijos
        for (Nodo hijo : nodo.getHijos().values()) {
            dibujarArbol(g2d, hijo);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (raiz != null && dimensionesSubarboles.containsKey(raiz)) {
            Dimension dim = dimensionesSubarboles.get(raiz);
            return new Dimension(dim.width + 100, dim.height + 100);
        }
        return new Dimension(300, 200);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
