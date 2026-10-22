/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ast;

import java.util.List;

/**
 *
 * @author dar333n
 */
public class Expresion extends Nodo{
    private List<Termino> terminos;

    public Expresion(List<Termino> terminos, int linea, int columna) {
        super(linea, columna);
        this.terminos = terminos;
    }

    public List<Termino> getTerminos() {
        return terminos;
    }

    
}
