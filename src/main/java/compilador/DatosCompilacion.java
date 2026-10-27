/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

import ast.Programa;
import java.util.List;
import registro.ErrorLexico;
import registro.ErrorSemantico;
import registro.ErrorSintactico;
import registro.Token;
import semantico.TablaSimbolos;

/**
 *
 * @author dar333n
 */
public class DatosCompilacion {

 
    private List<Token> tokens;
    private List<ErrorLexico> erroresLexicos;
    private List<ErrorSintactico> erroresSintacticos;
    private List<ErrorSemantico> erroresSemanticos;
    private Programa programa;
    private TablaSimbolos tabla;
    private String promptzal; 

    public DatosCompilacion(List<Token> tokens, List<ErrorLexico> erroresLexicos, List<ErrorSintactico> erroresSintacticos, List<ErrorSemantico> erroresSemanticos, Programa programa, TablaSimbolos tabla, String promptzal) {
        this.tokens = tokens;
        this.erroresLexicos = erroresLexicos;
        this.erroresSintacticos = erroresSintacticos;
        this.erroresSemanticos = erroresSemanticos;
        this.programa = programa;
        this.tabla = tabla;
        this.promptzal = promptzal;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<ErrorLexico> getErroresLexicos() {
        return erroresLexicos;
    }

    public List<ErrorSintactico> getErroresSintacticos() {
        return erroresSintacticos;
    }

    public List<ErrorSemantico> getErroresSemanticos() {
        return erroresSemanticos;
    }

    public Programa getPrograma() {
        return programa;
    }

    public TablaSimbolos getTabla() {
        return tabla;
    }

    public String getPromptzal() {
        return promptzal;
    }
 
    
    public int totalErrores() {
        return erroresLexicos.size() + erroresSintacticos.size() + erroresSemanticos.size();
    }
}
