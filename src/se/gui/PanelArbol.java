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
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
public class PanelArbol extends javax.swing.JPanel {
    
    private Nodo raiz;
    private int nodoDiametro = 60;  // Aumentado para mejor visibilidad
    private int nivelAltura = 100;   // Aumentado para más espacio vertical
    private int margenHorizontal = 50;
    private Map<Nodo, Point> posicionesNodos;
    private Map<Nodo, Dimension> dimensionesSubarboles;
    private double escala = 1.0;
    private Point offset = new Point(0, 0);
    private Point lastDrag;
    private boolean dragging = false;
    
    public PanelArbol() {
        setBackground(Color.WHITE);
        posicionesNodos = new HashMap<>();
        dimensionesSubarboles = new HashMap<>();
        
        // Agregar soporte para zoom con la rueda del mouse
        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            double factor = Math.pow(1.1, -notches);
            double newEscala = escala * factor;
            
            // Limitar el zoom entre 0.1 y 3.0
            if (newEscala >= 0.1 && newEscala <= 3.0) {
                Point mouse = e.getPoint();
                escala = newEscala;
                repaint();
            }
        });
        
        // Agregar soporte para arrastrar el árbol
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastDrag = e.getPoint();
                dragging = true;
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    int dx = e.getX() - lastDrag.x;
                    int dy = e.getY() - lastDrag.y;
                    offset.translate(dx, dy);
                    lastDrag = e.getPoint();
                    repaint();
                }
            }
        });
    }

    public void setArbol(Nodo raiz) {
        this.raiz = raiz;
        posicionesNodos.clear();
        dimensionesSubarboles.clear();
        escala = 1.0;
        offset = new Point(0, 0);
        
        if (raiz != null) {
            calcularDimensiones(raiz);
            calcularPosiciones(raiz, getWidth() / 2, 50, getWidth());
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (raiz != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Aplicar transformaciones
            g2d.translate(offset.x, offset.y);
            g2d.scale(escala, escala);
            
            dibujarArbol(g2d, raiz);
        }
    }

    private void dibujarArbol(Graphics2D g2d, Nodo nodo) {
        if (nodo == null) return;

        Point posicion = posicionesNodos.get(nodo);
        if (posicion == null) return;

        // Dibujar conexiones con los hijos
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f));
        
        for (Map.Entry<String, Nodo> entry : nodo.getHijos().entrySet()) {
            Nodo hijo = entry.getValue();
            Point posHijo = posicionesNodos.get(hijo);
            
            if (posHijo != null) {
                // Dibujar línea
                g2d.drawLine(posicion.x, posicion.y, posHijo.x, posHijo.y);
                
                // Dibujar valor de la rama
                String valorRama = entry.getKey();
                Point puntoMedio = new Point(
                    (posicion.x + posHijo.x) / 2,
                    (posicion.y + posHijo.y) / 2
                );
                
                // Fondo blanco para el texto
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(valorRama);
                int textHeight = fm.getHeight();
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillRect(puntoMedio.x - textWidth/2 - 2,
                           puntoMedio.y - textHeight/2,
                           textWidth + 4,
                           textHeight);
                
                // Dibujar el texto
                g2d.setColor(Color.BLACK);
                g2d.drawString(valorRama,
                             puntoMedio.x - textWidth/2,
                             puntoMedio.y + textHeight/4);
            }
        }

        // Dibujar nodo
        int radio = nodoDiametro / 2;
        
        // Sombra
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(posicion.x - radio + 2, 
                     posicion.y - radio + 2,
                     nodoDiametro, nodoDiametro);
                     
        // Gradiente para el nodo
        GradientPaint gradient = new GradientPaint(
            posicion.x - radio, posicion.y - radio,
            new Color(230, 240, 255),
            posicion.x + radio, posicion.y + radio,
            new Color(180, 200, 255)
        );
        g2d.setPaint(gradient);
        g2d.fillOval(posicion.x - radio, posicion.y - radio,
                     nodoDiametro, nodoDiametro);
                     
        // Borde del nodo
        g2d.setColor(new Color(100, 150, 255));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawOval(posicion.x - radio, posicion.y - radio,
                     nodoDiametro, nodoDiametro);

        // Dibujar texto del nodo
        String textoNodo = nodo.isEsHoja() ? nodo.getClasificacion() : nodo.getAtributo();
        if (textoNodo != null) {
            // Texto con sombra
            Font font = new Font("Dialog", Font.BOLD, 12);
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();
            int textoX = posicion.x - fm.stringWidth(textoNodo) / 2;
            int textoY = posicion.y + fm.getHeight() / 4;
            
            // Sombra del texto
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.drawString(textoNodo, textoX + 1, textoY + 1);
            
            // Texto principal
            g2d.setColor(Color.BLACK);
            g2d.drawString(textoNodo, textoX, textoY);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (raiz != null && dimensionesSubarboles.containsKey(raiz)) {
            Dimension dim = dimensionesSubarboles.get(raiz);
            return new Dimension(
                (int)(dim.width * escala) + 100,
                (int)(dim.height * escala) + 100
            );
        }
        return new Dimension(300, 200);
    }
    
    // Métodos para control de zoom y vista
    public void resetView() {
        escala = 1.0;
        offset = new Point(0, 0);
        repaint();
    }
    
    public void zoomIn() {
        if (escala < 3.0) {
            escala *= 1.1;
            repaint();
        }
    }
    
    public void zoomOut() {
        if (escala > 0.1) {
            escala /= 1.1;
            repaint();
        }
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
