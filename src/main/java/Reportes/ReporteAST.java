/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reportes;

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
import compilador.DatosCompilacion;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 *
 * @author dar333n
 */
public class ReporteAST {
    
    
    public static String generar(DatosCompilacion datos) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n")
            .append("<html lang=\"es\">\n")
            .append("<head>\n")
            .append("    <meta charset=\"UTF-8\">\n")
            .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
            .append("    <title>Reporte del AST - PromptZal</title>\n")
            .append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n")
            .append("</head>\n")
            .append("<body class=\"bg-light\">\n")
            .append("    <div class=\"container my-5\">\n")
            .append("        <div class=\"card shadow-sm border-0\">\n")
            .append("            <div class=\"card-body p-4\">\n")
            .append("                <h1 class=\"h3 mb-4 text-primary fw-bold\">Reporte del AST</h1>\n");

        if (datos.getPrograma() == null) {
            html.append("                <div class=\"alert alert-warning\" role=\"alert\">\n")
                .append("                    No hay un árbol para mostrar.\n")
                .append("                </div>\n");
        } else {
            // con errores léxicos o sintácticos el parser se recupera y el árbol puede quedar incompleto
            if (!datos.getErroresLexicos().isEmpty() || !datos.getErroresSintacticos().isEmpty()) {
                html.append("                <div class=\"alert alert-warning mb-4\" role=\"alert\">\n")
                    .append("                    <strong>Aviso:</strong> El programa tiene errores léxicos o sintácticos: el árbol puede estar incompleto.\n")
                    .append("                </div>\n");
            }

            html.append("                <div class=\"bg-white border p-3 rounded shadow-sm\">\n")
                .append("                    <pre class=\"mb-0 font-monospace\" style=\"font-size: 14px;\">").append(escapar(arbol(datos.getPrograma()))).append("</pre>\n")
                .append("                </div>\n");
        }

        html.append("            </div>\n")
            .append("        </div>\n")
            .append("    </div>\n")
            .append("    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\"></script>\n")
            .append("</body>\n</html>");
            
        return html.toString();
    }


    
    
    private static String arbol(Programa programa) {
        String texto = "Programa\n";

        for (Directiva d : programa.getDirectivas()) {
            texto = texto + linea(1, "Directiva " + d.getTipo() + " = \"" + d.getValor() + "\"   (línea " + d.getLinea() + ")");
        }

        for (Agente a : programa.getAgentes()) {
            texto = texto + linea(1, "Agente " + a.getNombre() + "   (línea " + a.getLinea() + ")");
            texto = texto + linea(2, "Contexto: \"" + a.getContexto() + "\"");

            for (Variable v : a.getVariables()) {
                texto = texto + linea(2, "Variable " + v.getNombre() + "   (línea " + v.getLinea() + ")");
                texto = texto + linea(3, "Valor: " + expresion(v.getValor()));
            }

            for (Comando c : a.getComandos()) {
                texto = texto + linea(2, "Comando " + c.getTipoComando() + "   (línea " + c.getLinea() + ")");
                texto = texto + linea(3, "Argumento: " + expresion(c.getArgumento()));
                for (Conector k : c.getConectores()) {
                    texto = texto + linea(3, "Conector " + k.getTipo() + ": " + expresion(k.getArgumento()));
                }
                texto = texto + linea(3, "Destino: " + c.getDestino().getNombre());
            }
        }

        for (Referencia r : programa.getEjecuciones()) {
            texto = texto + linea(1, "Ejecutar " + r.getNombre() + "   (línea " + r.getLinea() + ")");
        }

        for (Referencia r : programa.getExportaciones()) {
            texto = texto + linea(1, "Exportar " + r.getNombre() + "   (línea " + r.getLinea() + ")");
        }
        return texto;
    }


    
    
    
    
    private static String linea(int nivel, String contenido) {
        String espacios = "";
        for (int i = 0; i < nivel; i++) {
            espacios = espacios + "  ";
        }
        return espacios + contenido + "\n";
    }

    
    
    
    
    private static String expresion(Expresion e) {
        String texto = "";
        for (int i = 0; i < e.getTerminos().size(); i++) {
            Termino t = e.getTerminos().get(i);
            if (i > 0) {
                texto = texto + " + ";
            }
            if (t.getTipo() == TipoTermino.CADENA) {
                texto = texto + "\"" + t.getValor() + "\"";
            } else if (t.getTipo() == TipoTermino.CARGAR) {
                texto = texto + "CARGAR(\"" + t.getValor() + "\")";
            } else if (t.getTipo() == TipoTermino.NUMERO && t.getUnidad() != null) {
                texto = texto + t.getValor() + " " + t.getUnidad();
            } else {
                texto = texto + t.getValor(); // ID o número sin unidad
            }
        }
        return texto;
    }


    
    public static File crearArchivo(DatosCompilacion datos) throws IOException {
        File carpeta = new File("reportes_html");
        carpeta.mkdirs();
        File archivo = new File(carpeta, "reporte_ast.html");
        Files.writeString(archivo.toPath(), generar(datos), StandardCharsets.UTF_8);
        return archivo;
    }


    private static String escapar(String texto) {
        if (texto == null) return "";
        texto = texto.replace("&", "&amp;");
        texto = texto.replace("<", "&lt;");
        texto = texto.replace(">", "&gt;");
        texto = texto.replace("\"", "&quot;");
        return texto;
    }
}
