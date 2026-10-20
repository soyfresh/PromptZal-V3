/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package registro;

import registro.types.TokenType;

/**
 *
 * @author dar333n
 */
public class Token {
    private TokenType tipo;
    private String lexema;
    private int fila;
    private int columna;

    public Token(TokenType tipo, String lexema, int fila, int columna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.fila = fila;
        this.columna = columna;
    }

    public TokenType getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString(){
        return tipo + " '" + lexema + "' (" + fila + ":" + columna + ")";
    }
}

