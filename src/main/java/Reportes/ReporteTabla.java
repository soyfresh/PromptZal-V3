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
import semantico.Simbolo;

/**
 *
 * @author dar333n
 */
public class ReporteTabla {
    
    
    public static String generar(DatosCompilacion datos) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n")
            .append("<html lang=\"es\">\n")
            .append("<head>\n")
            .append("    <meta charset=\"UTF-8\">\n")
            .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
            .append("    <title>Reporte de la Tabla de Símbolos - PromptZal</title>\n")
            .append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n")
            .append("</head>\n")
            .append("<body class=\"bg-light\">\n")
            .append("    <div class=\"container my-5\">\n")
            .append("        <div class=\"card shadow-sm border-0\">\n")
            .append("            <div class=\"card-body p-4\">\n")
            .append("                <h1 class=\"h3 mb-4 text-primary fw-bold\">Reporte de la Tabla de Símbolos</h1>\n");

        // la tabla se llena en la fase semántica, que solo corre si no hubo errores léxicos ni sintácticos
        if (datos.getTabla() == null || datos.getTabla().getSimbolos().isEmpty()) {
            html.append("                <div class=\"alert alert-warning mb-3\" role=\"alert\">\n")
                .append("                    <strong>Aviso:</strong> La tabla de símbolos está vacía.\n")
                .append("                </div>\n")
                .append("                <p class=\"text-muted\">Si el programa tiene errores léxicos o sintácticos, la fase semántica no se ejecuta. Corrija los errores y compile de nuevo.</p>\n");
        } else {
            html.append("                <div class=\"alert alert-info mb-4\" role=\"alert\">\n")
                .append("                    Total de símbolos: <strong>").append(datos.getTabla().getSimbolos().size()).append("</strong>\n")
                .append("                </div>\n")
                .append("                <div class=\"table-responsive\">\n")
                .append("                    <table class=\"table table-striped table-hover table-bordered align-middle\">\n")
                .append("                        <thead class=\"table-dark\">\n")
                .append("                            <tr>\n")
                .append("                                <th scope=\"col\">Nombre</th>\n")
                .append("                                <th scope=\"col\">Tipo</th>\n")
                .append("                                <th scope=\"col\">Ámbito</th>\n")
                .append("                                <th scope=\"col\">Línea de declaración</th>\n")
                .append("                            </tr>\n")
                .append("                        </thead>\n")
                .append("                        <tbody>\n");

            for (Simbolo s : datos.getTabla().getSimbolos()) {
                html.append("                            <tr>\n")
                    .append("                                <td><code>").append(escapar(s.getNombre())).append("</code></td>\n")
                    .append("                                <td><span class=\"badge bg-secondary\">").append(escapar(s.getTipo().getDescripcion())).append("</span></td>\n")
                    .append("                                <td>").append(escapar(s.getAmbito())).append("</td>\n")
                    .append("                                <td>").append(s.getLinea()).append("</td>\n")
                    .append("                            </tr>\n");
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


    
    public static File crearArchivo(DatosCompilacion datos) throws IOException {
        File carpeta = new File("reportes_html");
        carpeta.mkdirs();
        File archivo = new File(carpeta, "reporte_simbolos.html");
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
