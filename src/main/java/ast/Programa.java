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
public class Programa extends Nodo {
    
    private List<Directiva> directivas;
    private List<Agente> agentes;
    private List<Referencia> ejecuciones;
    private List<Referencia> exportaciones;

    public Programa(List<Directiva> directivas, List<Agente> agentes, List<Referencia> ejecuciones, List<Referencia> exportaciones, int linea, int columna) {
        super(linea, columna);
        this.directivas = directivas;
        this.agentes = agentes;
        this.ejecuciones = ejecuciones;
        this.exportaciones = exportaciones;
    }

    public List<Directiva> getDirectivas() {
        return directivas;
    }

    public List<Agente> getAgentes() {
        return agentes;
    }

    public List<Referencia> getEjecuciones() {
        return ejecuciones;
    }

    public List<Referencia> getExportaciones() {
        return exportaciones;
    }
    
    
}
