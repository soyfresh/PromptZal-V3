/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package semantico;

import java.util.ArrayList;
import java.util.List;
import semantico.enums.TipoSimbolo;

/**
 *
 * @author dar333n
 */
public class TablaSimbolos {
    
    private List<Simbolo> simbolos = new ArrayList<>();

    public List<Simbolo> getSimbolos() {
        return simbolos;
    }

    /*
    METODOS PARA LLENAR LA TABLA
    */
    
    // AGREGAR AGENTE
    public boolean declararAgente(String nombre, int linea) {
        if (existeAgente(nombre)) {
            return false;
        }
        simbolos.add(new Simbolo(nombre, TipoSimbolo.AGENTE, "todo el programa", linea));
        return true;
    }
    
    public boolean existeAgente(String nombre) {
        return buscarAgente(nombre) != null;
    }
    
    public Simbolo buscarAgente(String nombre) {
        for (Simbolo s : simbolos) {
            if (s.getTipo() == TipoSimbolo.AGENTE && s.getNombre().equals(nombre)) {
                return s;
            }
        }
        return null;
    }
    
    
    // AGREGAR VARIABLE
    public boolean declararVariable(String agente, String nombre, TipoSimbolo tipo, int linea) {
        if (existeVariable(agente, nombre)) {
            return false;
        }
        simbolos.add(new Simbolo(nombre, tipo, agente, linea));
        return true;
    }
    
    
    public boolean existeVariable(String agente, String nombre) {
        return buscarVariable(agente, nombre) != null;
    }
    
    
    public Simbolo buscarVariable(String agente, String nombre) {
        for (Simbolo s : simbolos) {
            if (s.getTipo() != TipoSimbolo.AGENTE
                    && s.getAmbito().equals(agente)
                    && s.getNombre().equals(nombre)) {
                return s;
            }
        }
        return null;
    }
}
