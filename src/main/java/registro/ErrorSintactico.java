/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro;

/**
 *
 * @author dar333n
 */
public class ErrorSintactico {
    
    private String encontrado;
    private String esperado;
    private int fila;
    private int columna;
 
    public ErrorSintactico(String encontrado, String esperado, int fila, int columna) {
        this.encontrado = encontrado;
        this.esperado = esperado;
        this.fila = fila;
        this.columna = columna;
    }
 
    public String getEncontrado() {
        return encontrado;
    }
 
    public String getEsperado() {
        return esperado;
    }
 
    public int getFila() {
        return fila;
    }
 
    public int getColumna() {
        return columna;
    }
 
    @Override
    public String toString() {
        return "Error sintáctico (" + fila + ":" + columna + "): se esperaba " + esperado
                + ", llegó " + encontrado;
    }
}
