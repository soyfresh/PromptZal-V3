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
public class Comando extends Nodo{
    private String tipoComando;
    private Expresion argumento;
    private List<Conector> conectores;
    private Referencia destino;

    public Comando(String tipoComando, Expresion argumento, List<Conector> conectores, Referencia destino, int linea, int columna) {
        super(linea, columna);
        this.tipoComando = tipoComando;
        this.argumento = argumento;
        this.conectores = conectores;
        this.destino = destino;
    }

    public String getTipoComando() {
        return tipoComando;
    }

    public Expresion getArgumento() {
        return argumento;
    }

    public List<Conector> getConectores() {
        return conectores;
    }

    public Referencia getDestino() {
        return destino;
    }
}
