/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package semantico.enums;

/**
 *
 * @author dar333n
 */
public enum TipoSimbolo {
    AGENTE("agente"),
    VARIABLE_DECLARADA("variable declarada"),
    RESULTADO_COMANDO("resultado de comando");
 
    private String descripcion;
 
    TipoSimbolo(String descripcion) {
        this.descripcion = descripcion;
    }
 
    public String getDescripcion() {
        return descripcion;
    }
}
