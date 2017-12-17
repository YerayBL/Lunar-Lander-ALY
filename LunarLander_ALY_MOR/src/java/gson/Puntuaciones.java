/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import java.io.Serializable;
import java.util.List;

public class Puntuaciones implements Serializable {

   
    private List<PuntuacionGson> puntuacion = null;
    private final static long serialVersionUID = 4418342885309929295L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Puntuaciones() {
    }

    /**
     *
     * @param puntuacion
     */
    public Puntuaciones(List<PuntuacionGson> puntuacion) {
        super();
        this.puntuacion = puntuacion;
    }

    public List<PuntuacionGson> getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(List<PuntuacionGson> puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Puntuaciones withPuntuacion(List<PuntuacionGson> puntuacion) {
        this.puntuacion = puntuacion;
        return this;
    }

}
