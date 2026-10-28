/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reportes;

import compilador.DatosCompilacion;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import registro.ErrorLexico;
import registro.ErrorSemantico;
import registro.ErrorSintactico;

/**
 *
 * @author dar333n
 */
public class ReporteErrores {
    
    
    public static String generar(DatosCompilacion datos) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n")
            .append("<html lang=\"es\">\n")
            .append("<head>\n")
            .append("    <meta charset=\"UTF-8\">\n")
            .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
            .append("    <title>Reporte de Errores - PromptZal</title>\n")
                
            .append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n")
            .append("</head>\n")
            .append("<body class=\"bg-light\">\n")
            .append("    <div class=\"container my-5\">\n")
            .append("        <div class=\"card shadow-sm border-0\">\n")
            .append("            <div class=\"card-body p-4\">\n")
            .append("                <h1 class=\"h3 mb-4 text-primary fw-bold\">Reporte de Errores</h1>\n");
 
        // primer if indica que no existen erroes y no muestra nada
        if (datos.totalErrores() == 0) {
            html.append("                <div class=\"alert alert-success d-flex align-items-center\" role=\"alert\">\n")
                .append("                    <div>La compilación no tuvo errores léxicos, sintácticos ni semánticos. &mdash; <strong>BUILD SUCCESS</strong></div>\n")
                .append("                </div>\n");
        } else {
            html.append("                <div class=\"alert alert-danger mb-4\" role=\"alert\">\n")
                .append("                    Total: <strong>").append(datos.totalErrores()).append("</strong> error(es) &mdash; ")
                .append(datos.getErroresLexicos().size()).append(" léxico(s), ")
                .append(datos.getErroresSintacticos().size()).append(" sintáctico(s) y ")
                .append(datos.getErroresSemanticos().size()).append(" semántico(s).\n")
                .append("                </div>\n")
                .append("                <div class=\"table-responsive\">\n")
                .append("                    <table class=\"table table-striped table-hover table-bordered align-middle\">\n")
                .append("                        <thead class=\"table-dark\">\n")
                .append("                            <tr>\n")
                .append("                                <th scope=\"col\">#</th>\n")
                .append("                                <th scope=\"col\">Tipo</th>\n")
                .append("                                <th scope=\"col\">Descripción</th>\n")
                .append("                                <th scope=\"col\">Fila</th>\n")
                .append("                                <th scope=\"col\">Columna</th>\n")
                .append("                                <th scope=\"col\">Token encontrado</th>\n")
                .append("                                <th scope=\"col\">Token esperado</th>\n")
                .append("                            </tr>\n")
                .append("                        </thead>\n")
                .append("                        <tbody>\n");
 
            int numero = 1;
 
            for (ErrorLexico e : datos.getErroresLexicos()) {
                html.append(fila(numero, "Léxico", e.getDescripcion() + ": " + e.getLexema(),
                        e.getFila(), e.getColumna(), "", ""));
                numero++;
            }
 
            for (ErrorSintactico e : datos.getErroresSintacticos()) {
                html.append(fila(numero, "Sintáctico", "El orden de los tokens no es válido",
                        e.getFila(), e.getColumna(), e.getEncontrado(), e.getEsperado()));
                numero++;
            }
 
            for (ErrorSemantico e : datos.getErroresSemanticos()) {
                html.append(fila(numero, "Semántico", e.getDescripcion(),
                        e.getFila(), e.getColumna(), "", ""));
                numero++;
            }
 
            html.append("                        </tbody>\n")
                .append("                    </table>\n")
                .append("                </div>\n");
        }
 
        html.append("            </div>\n")
            .append("        </div>\n")
            .append("    </div>\n")
            .append("    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\"></script>\n")
            .append("</body>\n</html>");
            
        return html.toString();
    }
 

    private static String fila(int numero, String tipo, String descripcion, int fila, int columna,
            String encontrado, String esperado) {
        String badgeClass = "bg-secondary";
        if (tipo.equals("Léxico")) badgeClass = "bg-warning text-dark";
        else if (tipo.equals("Sintáctico")) badgeClass = "bg-danger";
        else if (tipo.equals("Semántico")) badgeClass = "bg-info text-dark";

        return "<tr>"
                + "<th scope=\"row\">" + numero + "</th>"
                + "<td><span class=\"badge " + badgeClass + "\">" + tipo + "</span></td>"
                + "<td>" + escapar(descripcion) + "</td>"
                + "<td>" + fila + "</td>"
                + "<td>" + columna + "</td>"
                + "<td><code>" + escapar(encontrado) + "</code></td>"
                + "<td><code>" + escapar(esperado) + "</code></td>"
                + "</tr>\n";
    }
 

    public static File crearArchivo(DatosCompilacion datos) throws IOException {
        File carpeta = new File("reportes_html");
        carpeta.mkdirs();
        File archivo = new File(carpeta, "reporte_errores.html");
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
