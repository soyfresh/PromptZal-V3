/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package registro.types;

/**
 *
 * @author dar333n
 */
public enum TokenType {
    DIRECTIVA,      // @modelo, @rol, @formato
    AGENTE,         // AGENTE
    CONTEXTO,       // contexto
    VARIABLE,       // variable
    EJECUTAR,       // EJECUTAR
    EXPORTAR,       // EXPORTAR
    COMANDO_IA,     // PREGUNTAR, GENERAR, RESUMIR, ANALIZAR, TRADUCIR, CLASIFICAR, EXTRAER
    CARGAR,         // CARGAR (NO es COMANDO_IA)
    CONECTOR,       // SOBRE, DESDE, EN, COMO
    FLECHA,         // ->  (NO es CONECTOR)
    IGUAL,          // =
    MAS,            // +
    LLAVE_A,        // {
    LLAVE_C,        // }
    PAR_A,          // (
    PAR_C,          // )
    COMA,           // ,
    ID,             // identificadores
    CADENA,         // "texto"
    NUMERO,         // 10, 0.7
    EOF             // fin de archivo
}
