/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semantico;

import semantico.enums.TipoSimbolo;

/**
 *
 * @author dar333n
 */
public class Simbolo {
    
    private String nombre;
    private TipoSimbolo tipo;
    private String ambito; 
    private int linea;

    public Simbolo(String nombre, TipoSimbolo tipo, String ambito, int linea) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ambito = ambito;
        this.linea = linea;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoSimbolo getTipo() {
        return tipo;
    }

    public String getAmbito() {
        return ambito;
    }

    public int getLinea() {
        return linea;
    }
    
    
}
