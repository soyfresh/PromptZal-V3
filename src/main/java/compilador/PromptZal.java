/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dar333n
 */
public class PromptZal {

    public static String generar(Programa programa) {
        String texto = "# Prompt generado con PromptZal\n";

        // Buscar las directivas agregadas
        String modelo = buscarDirectiva(programa, "@modelo");
        String rol = buscarDirectiva(programa, "@rol");
        String formato = buscarDirectiva(programa, "@formato");
        if (modelo != null) {
            texto = texto + "Modelo destino: " + modelo + "\n";
        }
        if (rol != null) {
            texto = texto + "Rol general: " + rol + "\n";
        }
        if (formato != null) {
            texto = texto + "Formato de respuesta: " + formato + "\n";
        }

        // Procesar cada agente que si se ejecuto por separado en el for
        List<String> yaIncluidos = new ArrayList<>();
        for (Referencia ejecucion : programa.getEjecuciones()){
            Agente agente = buscarAgente(programa, ejecucion.getNombre());

            // Verificar de agentes
            if (agente == null) {
                continue; // el agente no se ejecuto, no se toma encuenta y ya debe salir marcado como error
            }
            if (yaIncluidos.contains(agente.getNombre())) {
                continue; // si se ejecuta dos veces, se incluye una sola vez
            }

            // Si pasa la veriifcacion se agrega, a la lista y al texto
            yaIncluidos.add(agente.getNombre());
            texto = texto + generarAgente(agente);
        }

        // 
        texto = texto + "\n## Resultado solicitado\n";
        List<String> yaPedidos = new ArrayList<>();
        for (Referencia exportada : programa.getExportaciones()) {
            if (!yaPedidos.contains(exportada.getNombre())) {
                yaPedidos.add(exportada.getNombre());
                texto = texto + "- " + exportada.getNombre() + "\n";
            }
        }
        return texto;
    }
 
    
    private static String buscarDirectiva(Programa programa, String nombre) {
        String valor = null;
        for (Directiva directiva : programa.getDirectivas()) {
            if (directiva.getTipo().equals(nombre)) {
                valor = directiva.getValor();
            }
        }
        return valor;
    }
    
    private static Agente buscarAgente(Programa programa, String nombre) {
        for (Agente agente : programa.getAgentes()) {
            if (agente.getNombre().equals(nombre)) {
                return agente;
            }
        }
        return null;
    }

    
    
    /*
    AGENTE VERIFICADO     
    */
    private static String generarAgente(Agente agente) {
        String texto = "\n## Agente: " + agente.getNombre() + "\n";
        texto = texto + "Contexto: " + agente.getContexto() + "\n";
 
        // variables declaradas, con el origen de su valor
        if (!agente.getVariables().isEmpty()){
            texto = texto + "Variables disponibles:\n";
            for (Variable variable : agente.getVariables()){
                
                // origenVariable() METODO PARA OBTENER los datos de la variable
                texto = texto + "- " + variable.getNombre() + ": " + origenVariable(variable.getValor()) + "\n";
            }
        }
 
        // una instrucción por comando, numeradas, en el orden en que aparecen
        texto = texto + "Instrucciones:\n";
        int numero = 1;
        for (Comando comando : agente.getComandos()){
            texto = texto + numero + ". " + instruccion(comando) + "\n";
            numero++;
        }
        return texto;
    }
 
    private static String origenVariable(Expresion expresion) {
        String texto = "";
        List<Termino> terminos = expresion.getTerminos();
        for (int i = 0; i < terminos.size(); i++) {
            Termino t = terminos.get(i);
            if (i > 0) {
                texto = texto + " + ";
            }
            if (t.getTipo() == TipoTermino.CARGAR) {
                texto = texto + "contenido del archivo \"" + t.getValor() + "\" (adjuntar el archivo)";
            } else if (t.getTipo() == TipoTermino.CADENA) {
                texto = texto + "el texto \"" + t.getValor() + "\"";
            } else if (t.getTipo() == TipoTermino.NUMERO) {
                texto = texto + "el valor " + textoTermino(t);
            } else {
                texto = texto + "el valor de " + t.getValor(); // ID
            }
        }
        return texto;
    }
 
    
    
    
    /*
    COMANDOS - INSTRUCCIONES
    */
    private static String instruccion(Comando comando){
        
        String tipo = comando.getTipoComando();
        String x = textoExpresion(comando.getArgumento());
        String destino = comando.getDestino().getNombre();
 
        // el contenido de cada conector (o null si el comando no lo trae)
        Expresion sobre = buscarConector(comando, "SOBRE");
        Expresion desde = buscarConector(comando, "DESDE");
        Expresion en = buscarConector(comando, "EN");
        Expresion como = buscarConector(comando, "COMO");
 
        String frase;
        String guardar = "Guarda el resultado como " + destino + ".";
 
        if (tipo.equals("PREGUNTAR")) {
            // PREGUNTAR p SOBRE x -> y
            if (sobre != null) {
                frase = "Basándote en " + textoExpresion(sobre) + ", responde: " + x;
            } else {
                frase = "Responde: " + x;
            }
            guardar = "Guarda la respuesta como " + destino + ".";
        } else if (tipo.equals("GENERAR")) {
            // GENERAR t DESDE e -> y
            frase = "Genera " + x;
            if (desde != null) {
                frase = frase + " a partir de: " + textoExpresion(desde);
            }
        } else if (tipo.equals("RESUMIR")) {
            // RESUMIR x EN n palabras -> y
            frase = "Resume " + x;
            if (en != null) {
                frase = frase + " en un máximo de " + limitePalabras(en);
            }
        } else if (tipo.equals("ANALIZAR")) {
            // ANALIZAR x SOBRE c -> y
            frase = "Analiza " + x;
            if (sobre != null) {
                frase = frase + " considerando: " + textoExpresion(sobre);
            }
        } else if (tipo.equals("TRADUCIR")) {
            // TRADUCIR x EN i -> y
            frase = "Traduce " + x;
            if (en != null) {
                frase = frase + " al idioma " + textoExpresion(en);
            }
        } else if (tipo.equals("CLASIFICAR")) {
            // CLASIFICAR x EN c -> y
            frase = "Clasifica " + x;
            if (en != null) {
                frase = frase + " en las categorías: " + textoExpresion(en);
            }
        } else if (tipo.equals("EXTRAER")) {
            // EXTRAER e DESDE x -> y
            frase = "Extrae " + x;
            if (desde != null) {
                frase = frase + " a partir de " + textoExpresion(desde);
            }
        } else {
            // comando nuevo agregado en clase (por ejemplo CODIFICAR): plantilla genérica
            frase = "Realiza la acción " + tipo + " con: " + x;
            for (Conector conector : comando.getConectores()) {
                if (!conector.getTipo().equals("COMO")) {
                    frase = frase + ", " + conector.getTipo().toLowerCase() + ": "
                            + textoExpresion(conector.getArgumento());
                }
            }
        }
 
        String resultado = cerrar(frase) + " " + guardar;
 
        // COMO f (en cualquier comando): se agrega al final
        if (como != null) {
            resultado = resultado + " Entrega el resultado en formato " + textoExpresion(como) + ".";
        }
        return resultado;
    }
 
   
    
    private static Expresion buscarConector(Comando comando, String tipo) {
        for (Conector conector : comando.getConectores()){
            if (conector.getTipo().equals(tipo)){
                return conector.getArgumento();
            }
        }
        return null;
    }
 
    private static String textoExpresion(Expresion expresion) {
        String texto = "";
        for (Termino t : expresion.getTerminos()) {
            texto = texto + textoTermino(t);
        }
        return texto;
    }
 
    private static String textoTermino(Termino t) {
        if (t.getTipo() == TipoTermino.NUMERO) {
            if (t.getUnidad() != null) {
                return t.getValor() + " " + t.getUnidad();
            }
            return t.getValor();
        }
        if (t.getTipo() == TipoTermino.CARGAR) {
            return "el contenido del archivo \"" + t.getValor() + "\"";
        }
        return t.getValor(); // CADENA o ID
    }
 
    private static String limitePalabras(Expresion en) {
        Termino primero = en.getTerminos().get(0);
        if (primero.getTipo() == TipoTermino.NUMERO && primero.getUnidad() == null) {
            return textoExpresion(en) + " palabras";
        }
        return textoExpresion(en);
    }
 
    private static String cerrar(String frase) {
        char ultimo = frase.charAt(frase.length() - 1);
        if (ultimo == '.' || ultimo == '?' || ultimo == '!') {
            return frase;
        }
        return frase + ".";
    }
}

