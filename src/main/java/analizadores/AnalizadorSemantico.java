/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadores;

import ast.Agente;
import ast.Comando;
import ast.Conector;
import ast.Expresion;
import ast.Programa;
import ast.Referencia;
import ast.Termino;
import ast.Variable;
import ast.enums.TipoTermino;
import java.util.ArrayList;
import java.util.List;
import registro.ErrorSemantico;
import semantico.Simbolo;
import semantico.TablaSimbolos;
import semantico.enums.TipoSimbolo;

/**
 *
 * @author dar333n
 */
public class AnalizadorSemantico {
    
    private TablaSimbolos tabla = new TablaSimbolos();
    private List<ErrorSemantico> errores = new ArrayList<>();
 
    public TablaSimbolos getTabla() {
        return tabla;
    }
 
    public List<ErrorSemantico> getErrores() {
        return errores;
    }
 
    /* 
    INICIO: Analizador semantico 
    */
    public void analizar(Programa programa) {
        
        // ANALIZA LOS AGENTES Y SUS VARIABLES
        for (Agente agente : programa.getAgentes()) {
            analizarAgente(agente);
        }
        
        // ANALIZAR LAS EJECUCIONES
        List<String> ejecutados = validarEjecuciones(programa);
        

        // ANALIZAR LOS ID DEL EXPORT
        validarExportaciones(programa, ejecutados);
    }
 
    
    
    private void analizarAgente(Agente agente){
        
        // Verificar que el agente no este repetido con el metodo de tabla
        boolean declarado = tabla.declararAgente(agente.getNombre(), agente.getLinea());
        if (!declarado){

            Simbolo primero = tabla.buscarAgente(agente.getNombre());
            error("El agente "+agente.getNombre()+" ya existe en la linea "+primero.getLinea(), agente.getLinea(), agente.getColumna());
            return;
        }

        for (Variable variable : agente.getVariables()){ // Obtenemos las variables del agente que estamos abalizando
            
            validarExpresion(variable.getValor(), agente.getNombre(), false);
            
            tabla.declararVariable(agente.getNombre(), variable.getNombre(), 
                    TipoSimbolo.VARIABLE_DECLARADA, variable.getLinea());           
        }
 
        
        for (Comando comando : agente.getComandos()){ // Obtenemos los comandos del agente
            
            boolean esGenerar = comando.getTipoComando().equals("GENERAR");
            validarExpresion(comando.getArgumento(), agente.getNombre(), esGenerar);
            
            for (Conector conector : comando.getConectores()){
                validarExpresion(conector.getArgumento(), agente.getNombre(), false);
            }
            
            // lo que va despues de -> tambien declara una variable
            Referencia destino = comando.getDestino();
            tabla.declararVariable(agente.getNombre(), destino.getNombre(), TipoSimbolo.RESULTADO_COMANDO,
                    destino.getLinea());
        }
    }

    // VAlidar los ID
    private void validarExpresion(Expresion expresion, String nombreAgen, boolean primerIdEsTipo) {
        
        List<Termino> terminos = expresion.getTerminos();        
        for (int i = 0; i < terminos.size(); i++){
            Termino t = terminos.get(i);
            
            // Verificar si es un ID
            if (t.getTipo() != TipoTermino.ID){
                continue;
            }
            
            // Verfica que no sea una variable
            if (primerIdEsTipo && i == 0){
                continue;
            }

            // Verifica que la variabe no exista ya 
            if (!tabla.existeVariable(nombreAgen, t.getValor())){
                error("La variable " + t.getValor() + " no está declarada en el agente "+nombreAgen,
                        t.getLinea(), t.getColumna());
            }
        }
    }


    // Validar las ejecuciones
    private List<String> validarEjecuciones(Programa programa) {
        List<String> ejecutados = new ArrayList<>();
        for (Referencia ejecucion : programa.getEjecuciones()) {
            if (tabla.existeAgente(ejecucion.getNombre())) {
                ejecutados.add(ejecucion.getNombre());
            } else {
                error("El agente " + ejecucion.getNombre() +" no existe",
                        ejecucion.getLinea(), ejecucion.getColumna());
            }
        }
        return ejecutados;
    }
 

    // Validaer las exportaciones 
    private void validarExportaciones(Programa programa, List<String> ejecutados) {
        for (Referencia exportada : programa.getExportaciones()) {
            boolean existe = false;
            for (String agente : ejecutados) {
                if (tabla.existeVariable(agente, exportada.getNombre())) {
                    existe = true;
                }
            }
            
            if (!existe) {
                error("La variable " + exportada.getNombre() + " no existe en ninguno de los agentes ejecutados",
                        exportada.getLinea(), exportada.getColumna());
            }
        }
    }
 
    
    //Registrar nuevo error
    private void error(String descripcion, int linea, int columna) {
        errores.add(new ErrorSemantico(descripcion, linea, columna));
    }
}
