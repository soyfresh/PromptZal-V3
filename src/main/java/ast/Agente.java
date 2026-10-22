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
public class Agente extends Nodo{

    private String nombre;
    private String contexto;
    private List<Variable> variables;
    private List<Comando> comandos;

    public Agente(String nombre, String contexto, List<Variable> variables, List<Comando> comandos, int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.contexto = contexto;
        this.variables = variables;
        this.comandos = comandos;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContexto() {
        return contexto;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public List<Comando> getComandos() {
        return comandos;
    }

    
}

