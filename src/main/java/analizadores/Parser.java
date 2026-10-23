/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadores;

import ast.Agente;
import ast.Comando;
import ast.Conector;
import ast.Directiva;
import ast.Expresion;
import ast.Programa;
import ast.Referencia;
import ast.Termino;
import ast.Variable;
import ast.enums.TipoTermino;
import expections.ErrorPanico;
import java.util.ArrayList;
import java.util.List;
import registro.ErrorSintactico;
import registro.Token;
import registro.types.TokenType;

/**
 *
 * @author dar333n
 */
public class Parser {
    
    
    private List<Token> tokens;
    private int pos = 0;
    private List<ErrorSintactico> errores = new ArrayList<>();
    
    private boolean modoPanico = false;
    
    private boolean dentroAgente = false;
    
    public Parser(List<Token> tokens) {
        this.tokens = tokens; // el último token siempre es EOF
    }
 
    public List<ErrorSintactico> getErrores() {
        return errores;
    }
 
    /* INICIO: analiza todo el programa y devuelve la raíz del AST */
    public Programa parsear() {
        return programa();
    }
    
    
    
    
    
    
    
    
    // =====================================================================
    // Utilidades
    // =====================================================================
    
    //Token actual
    private Token actual() {
        return tokens.get(pos);
    }
 
    //Confirmar que el token actual es del mismo tipo
    private boolean es(TokenType tipo) {
        return actual().getTipo() == tipo;
    }
 
    //avanzar al siguiente token
    private void avanzar() {
        if (pos < tokens.size() - 1) {
            pos++;
        }
    }
 
    /*
     Si el token actual es el que pide la regla, lo consume y lo devuelve
     Si no, reporta el error y lanza ErrorPanico
     */
    private Token esperar(TokenType tipo) {
        if (es(tipo)) {
            Token t = actual();
            avanzar();
            modoPanico = false;
            return t;
        }
        throw fallar(tipo.name());
    }
 
    /* Registra el error (token encontrado, esperado, fila y columna) y devuelve la excepción */
    private ErrorPanico fallar(String esperado) {
        if (!modoPanico) {
            Token t = actual();
            String encontrado;
            if (t.getTipo() == TokenType.EOF) {
                encontrado = "EOF (fin de archivo)";
            } else {
                encontrado = t.getTipo().name() + " '" + t.getLexema() + "'";
            }
            errores.add(new ErrorSintactico(encontrado, esperado, t.getFila(), t.getColumna()));
        }
        modoPanico = true;
        return new ErrorPanico();
    }
 
    /*  verifica si el token actual es de sincronización? Marca dónde empieza o termina algo */
    private boolean esSincronizacion(boolean conDirectiva) {
        TokenType t = actual().getTipo();
        if (t == TokenType.EOF) {
            return true;
        }
        if (t == TokenType.LLAVE_C) {
            return dentroAgente; // una } suelta fuera de un agente se descarta
        }
        if (conDirectiva && t == TokenType.DIRECTIVA) {
            return true;
        }
        return t == TokenType.AGENTE
                || t == TokenType.COMANDO_IA
                || t == TokenType.VARIABLE
                || t == TokenType.EJECUTAR
                || t == TokenType.EXPORTAR
                || t == TokenType.CONTEXTO;
    }
 
    /* Modo pánico: descarta tokens hasta llegar a uno de sincronización (sin consumirlo) */
    private void sincronizar() {
        while (!esSincronizacion(false)) {
            avanzar();
        }
    }
 
    /* Igual que sincronizar(), pero otra DIRECTIVA también sirve para continuar */
    private void sincronizarDirectiva() {
        while (!esSincronizacion(true)) {
            avanzar();
        }
    }
 
    /* Quita las comillas de un lexema de tipo CADENA */
    private String sinComillas(String lexema) {
        if (lexema.length() >= 2) {
            return lexema.substring(1, lexema.length() - 1);
        }
        return lexema;
    }
 
    private Referencia referencia(Token t) {
        return new Referencia(t.getLexema(), t.getFila(), t.getColumna());
    }
 
    
    
    
    
    
    
    // =====================================================================
    // Gramática: un método por cada no terminal
    // =====================================================================
 
    // <programa> -> <directivas> <agentes> <ejecuciones> <exportar> EOF
    private Programa programa() {
        List<Directiva> directivas = directivas();
        List<Agente> agentes = agentes();
        List<Referencia> ejecuciones = ejecuciones();
        List<Referencia> exportaciones = exportar();
        try {
            esperar(TokenType.EOF); // si sobran tokens se reporta el error y se ignora el resto
        } catch (ErrorPanico e) {
            // fin del análisis
        }
        return new Programa(directivas, agentes, ejecuciones, exportaciones, 1, 1);
    }
 
    // <directivas> -> DIRECTIVA CADENA <directivas> | ε
    private List<Directiva> directivas() {
        List<Directiva> lista = new ArrayList<>();
        if (es(TokenType.DIRECTIVA)) {
            Directiva d = directiva();
            if (d != null) {
                lista.add(d);
            }
            lista.addAll(directivas());
        }
        // ε: no hace nada, devuelve la lista vacía
        return lista;
    }
 
    /* DIRECTIVA CADENA (una sola directiva, con su recuperación de errores) */
    private Directiva directiva() {
        try {
            Token d = esperar(TokenType.DIRECTIVA);
            Token valor = esperar(TokenType.CADENA);
            return new Directiva(d.getLexema(), sinComillas(valor.getLexema()), d.getFila(), d.getColumna());
        } catch (ErrorPanico e) {
            sincronizarDirectiva();
            return null;
        }
    }
 
    // <agentes> -> <agente> <mas_agentes>
    private List<Agente> agentes() {
        List<Agente> lista = new ArrayList<>();
        Agente a = agente();
        if (a != null) {
            lista.add(a);
        }
        lista.addAll(masAgentes());
        return lista;
    }
 
    // <mas_agentes> -> <agente> <mas_agentes> | ε
    private List<Agente> masAgentes() {
        List<Agente> lista = new ArrayList<>();
        if (es(TokenType.AGENTE)) { // si no es agente retorna la lista
            Agente a = agente();
            if (a != null) {
                lista.add(a);
            }
            lista.addAll(masAgentes());
        }
        return lista;
    }
 
    // <agente> -> AGENTE ID LLAVE_A <contexto> <variables> <comandos> LLAVE_C
    private Agente agente() {
        Token inicio;
        try {
            inicio = esperar(TokenType.AGENTE);
        } catch (ErrorPanico e) {
            sincronizar();
            return null;
        }
        dentroAgente = true;
 
        String nombre = "";
        try {
            Token id = esperar(TokenType.ID);
            nombre = id.getLexema();
            esperar(TokenType.LLAVE_A);
        } catch (ErrorPanico e) {
            sincronizar();
        }
 
        String contexto = contexto();
        List<Variable> variables = variables();
        List<Comando> comandos = comandos();
 
        try {
            esperar(TokenType.LLAVE_C);
        } catch (ErrorPanico e) {
            sincronizar();
        }
        dentroAgente = false;
 
        return new Agente(nombre, contexto, variables, comandos, inicio.getFila(), inicio.getColumna());
    }
 
    // <contexto> -> CONTEXTO IGUAL CADENA
    private String contexto() {
        try {
            esperar(TokenType.CONTEXTO);
            esperar(TokenType.IGUAL);
            Token cadena = esperar(TokenType.CADENA);
            return sinComillas(cadena.getLexema());
        } catch (ErrorPanico e) {
            sincronizar();
            return "";
        }
    }
 
    // <variables> -> VARIABLE ID IGUAL <expresion> <variables> | ε
    private List<Variable> variables() {
        List<Variable> lista = new ArrayList<>();
        if (es(TokenType.VARIABLE)) {
            Variable v = variable();
            if (v != null) {
                lista.add(v);
            }
            lista.addAll(variables());
        }
        return lista;
    }
 
    /* VARIABLE ID IGUAL <expresion> (una sola declaración, con su recuperación de errores) */
    private Variable variable() {
        try {
            Token inicio = esperar(TokenType.VARIABLE);
            Token id = esperar(TokenType.ID);
            esperar(TokenType.IGUAL);
            Expresion valor = expresion();
            return new Variable(id.getLexema(), valor, inicio.getFila(), inicio.getColumna());
        } catch (ErrorPanico e) {
            sincronizar();
            return null;
        }
    }
 
    // <comandos> -> <comando> <mas_comandos>
    private List<Comando> comandos() {
        List<Comando> lista = new ArrayList<>();
        Comando c = comando();
        if (c != null) {
            lista.add(c);
        }
        lista.addAll(masComandos());
        return lista;
    }
 
    // <mas_comandos> -> <comando> <mas_comandos> | ε
    private List<Comando> masComandos() {
        List<Comando> lista = new ArrayList<>();
        if (es(TokenType.COMANDO_IA)) {
            Comando c = comando();
            if (c != null) {
                lista.add(c);
            }
            lista.addAll(masComandos());
        }
        return lista;
    }
 
    // <comando> -> COMANDO_IA <expresion> <conectores> FLECHA ID
    private Comando comando() {
        try {
            Token inicio = esperar(TokenType.COMANDO_IA);
            Expresion argumento = expresion();
            List<Conector> conectores = conectores();
            esperar(TokenType.FLECHA);
            Token destino = esperar(TokenType.ID);
            return new Comando(inicio.getLexema(), argumento, conectores, referencia(destino),
                    inicio.getFila(), inicio.getColumna());
        } catch (ErrorPanico e) {
            sincronizar();
            return null;
        }
    }
 
    // <conectores> -> CONECTOR <expresion> <conectores> | ε
    private List<Conector> conectores() {
        List<Conector> lista = new ArrayList<>();
        if (es(TokenType.CONECTOR)) {
            Token c = esperar(TokenType.CONECTOR);
            Expresion argumento = expresion();
            lista.add(new Conector(c.getLexema(), argumento, c.getFila(), c.getColumna()));
            lista.addAll(conectores());
        }
        return lista;
    }
 
    // <expresion> -> <termino> <mas_terminos>
    private Expresion expresion() {
        List<Termino> terminos = new ArrayList<>();
        Termino primero = termino();
        terminos.add(primero);
        terminos.addAll(masTerminos());
        return new Expresion(terminos, primero.getLinea(), primero.getColumna());
    }
 
    // <mas_terminos> -> MAS <termino> <mas_terminos> | ε
    private List<Termino> masTerminos() {
        List<Termino> lista = new ArrayList<>();
        if (es(TokenType.MAS)) {
            esperar(TokenType.MAS);
            lista.add(termino());
            lista.addAll(masTerminos());
        }
        return lista;
    }
 
    // <termino> -> CADENA | ID | NUMERO <unidad> | <cargar>
    private Termino termino() {
        Token t = actual();
        if (es(TokenType.CADENA)) {
            esperar(TokenType.CADENA);
            return new Termino(TipoTermino.CADENA, sinComillas(t.getLexema()), null, t.getFila(), t.getColumna());
        }
        if (es(TokenType.ID)) {
            esperar(TokenType.ID);
            return new Termino(TipoTermino.ID, t.getLexema(), null, t.getFila(), t.getColumna());
        }
        if (es(TokenType.NUMERO)) {
            esperar(TokenType.NUMERO);
            String unidad = unidad();
            return new Termino(TipoTermino.NUMERO, t.getLexema(), unidad, t.getFila(), t.getColumna());
        }
        if (es(TokenType.CARGAR)) {
            return cargar();
        }
        throw fallar("<expresion> (CADENA, ID, NUMERO o CARGAR)");
    }
 
    // <unidad> -> ID | ε   
    private String unidad() {
        if (es(TokenType.ID)) {
            Token id = esperar(TokenType.ID);
            return id.getLexema();
        }
        return null; // ε
    }
 
    // <cargar> -> CARGAR PAR_A CADENA PAR_C
    private Termino cargar() {
        Token inicio = esperar(TokenType.CARGAR);
        esperar(TokenType.PAR_A);
        Token archivo = esperar(TokenType.CADENA);
        esperar(TokenType.PAR_C);
        return new Termino(TipoTermino.CARGAR, sinComillas(archivo.getLexema()), null,
                inicio.getFila(), inicio.getColumna());
    }
 
    // <ejecuciones> -> EJECUTAR ID <mas_ejecuciones>
    private List<Referencia> ejecuciones() {
        List<Referencia> lista = new ArrayList<>();
        Referencia r = ejecucion();
        if (r != null) {
            lista.add(r);
        }
        lista.addAll(masEjecuciones());
        return lista;
    }
 
    // <mas_ejecuciones> -> EJECUTAR ID <mas_ejecuciones> | ε
    private List<Referencia> masEjecuciones() {
        List<Referencia> lista = new ArrayList<>();
        if (es(TokenType.EJECUTAR)) {
            Referencia r = ejecucion();
            if (r != null) {
                lista.add(r);
            }
            lista.addAll(masEjecuciones());
        }
        return lista;
    }
 
    /* EJECUTAR ID (una sola ejecución, con su recuperación de errores) */
    private Referencia ejecucion() {
        try {
            esperar(TokenType.EJECUTAR);
            Token id = esperar(TokenType.ID);
            return referencia(id);
        } catch (ErrorPanico e) {
            sincronizar();
            return null;
        }
    }
 
    // <exportar> -> EXPORTAR ID <mas_ids>
    private List<Referencia> exportar() {
        List<Referencia> lista = new ArrayList<>();
        try {
            esperar(TokenType.EXPORTAR);
            Token id = esperar(TokenType.ID);
            lista.add(referencia(id));
            masIds(lista);
        } catch (ErrorPanico e) {
            sincronizar();
        }
        return lista;
    }
 
    // <mas_ids> -> COMA ID <mas_ids> | ε
    // Recibe la lista y le va agregando los ids (así no se pierden los ya leídos si hay un error)
    private void masIds(List<Referencia> lista) {
        if (es(TokenType.COMA)) {
            esperar(TokenType.COMA);
            Token id = esperar(TokenType.ID);
            lista.add(referencia(id));
            masIds(lista);
        }
    }
}
 

