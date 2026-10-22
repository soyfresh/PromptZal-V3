/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ast;

/**
 *
 * @author dar333n
 */
public class Variable extends Nodo{
    private String nombre;
    private Expresion valor;

    public Variable(String nombre, Expresion valor, int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public Expresion getValor() {
        return valor;
    }

}
