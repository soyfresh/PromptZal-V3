/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ast;

import ast.enums.TipoTermino;

/**
 *
 * @author dar333n
 */
 public class Termino extends Nodo{
    private TipoTermino tipo;
    private String valor;
    private String unidad;

    public Termino(TipoTermino tipo, String valor, String unidad, int linea, int columna) {
        super(linea, columna);
        this.tipo = tipo;
        this.valor = valor;
        this.unidad = unidad;
    }

    public TipoTermino getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public String getUnidad() {
        return unidad;
    }
    
    
}
