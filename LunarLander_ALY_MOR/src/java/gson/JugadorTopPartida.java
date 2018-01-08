/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import java.io.Serializable;

/**
 *
 * @author tuno
 */
public class JugadorTopPartida implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String idUsuario;
    
    private int numPartida;

    public JugadorTopPartida() {
    }
    
    public JugadorTopPartida(String idUsuario, int numPartida) {
        this.idUsuario = idUsuario;
        this.numPartida = numPartida;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getNumPartida() {
        return numPartida;
    }

    public void setNumPartida(int numPartida) {
        this.numPartida = numPartida;
    }
    
    
    
    
}
