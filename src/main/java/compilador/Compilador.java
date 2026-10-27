/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

import analizadores.AnalizadorSemantico;
import analizadores.Lexer;
import analizadores.Parser;
import ast.Programa;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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
public class Compilador {
 
    public DatosCompilacion compilar(String codigo) {
        // lexer
        Lexer lexer = new Lexer(new StringReader(codigo));
        List<Token> tokens = new ArrayList<>();
        try {
            tokens = lexer.tokenizar();
        } catch (IOException e) {
            System.out.println("Error leyendo el codigo fuente: " + e.getMessage());
        }
        List<ErrorLexico> erroresLexicos = lexer.getErrores();
 
        // Parser
        Parser parser = new Parser(tokens);
        Programa programa = parser.parsear();
        List<ErrorSintactico> erroresSintacticos = parser.getErrores();
 
        // Analizador semantico y promptZAl
        TablaSimbolos tabla = new TablaSimbolos();
        List<ErrorSemantico> erroresSemanticos = new ArrayList<>();
        String promptzal = null;
 
        if (erroresLexicos.isEmpty() && erroresSintacticos.isEmpty()) {
            AnalizadorSemantico semantico = new AnalizadorSemantico();
            semantico.analizar(programa);
            tabla = semantico.getTabla();
            erroresSemanticos = semantico.getErrores();
 
            promptzal = PromptZal.generar(programa);
        }
 
        return new DatosCompilacion(tokens, erroresLexicos, erroresSintacticos,
                erroresSemanticos, programa, tabla, promptzal);
    }
}

