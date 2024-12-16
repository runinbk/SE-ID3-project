/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package se.util;

/**
 *
 * @author braya
 */
public class MathUtils {
    public static double log2(double x) {
        return x == 0 ? 0 : Math.log(x) / Math.log(2);
    }
    
    public static double calcularEntropiaBinaria(int total, int favorables) {
        if (total == 0) return 0;
        
        double p = (double)favorables/total;
        double n = (double)(total-favorables)/total;
        
        return -(p * log2(p) + n * log2(n));
    }
}
