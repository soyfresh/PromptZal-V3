/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro;

/**
 *
 * @author dar333n
 */
public class ErrorSemantico {
    
    private String descripcion;
    private int fila;
    private int columna;

    public ErrorSemantico(String descripcion, int fila, int columna) {
        this.descripcion = descripcion;
        this.fila = fila;
        this.columna = columna;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
    
    @Override
    public String toString() {
        return "Error semántico (" + fila + ":" + columna + "): " + descripcion;
    }
    
}
