/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author tuno
 */
public class JugadoresTopPartidas implements Serializable{
  
   private List<JugadorTopPartida> touser = null;
   private final static long serialVersionUID = 4418342885309929295L;

    /**
     * No args constructor for use in serialization
     *
     */
    public JugadoresTopPartidas() {
    }

    /**
     *
     * @param touser
     */
    public JugadoresTopPartidas(List<JugadorTopPartida> touser) {
        super();
        this.touser = touser;
    }

    public List<JugadorTopPartida> getJugadoresTopPartidas() {
        return touser;
    }

    public void setJugadoresTopPartidas(List<JugadorTopPartida> touser) {
        this.touser = touser;
    }

    public JugadoresTopPartidas withJugadoresTopPartidas(List<JugadorTopPartida> touser) {
        this.touser = touser;
        return this;
    }  
}
