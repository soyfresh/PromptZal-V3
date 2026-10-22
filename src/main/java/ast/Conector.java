/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ast;

/**
 *
 * @author dar333n
 */
public class Conector extends Nodo{
    private String tipo;
    private Expresion argumento;

    public Conector(String tipo, Expresion argumento, int linea, int columna) {
        super(linea, columna);
        this.tipo = tipo;
        this.argumento = argumento;
    }

    public String getTipo() {
        return tipo;
    }

    public Expresion getArgumento() {
        return argumento;
    }
    
    
}
