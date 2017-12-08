/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;
import model.Puntuacion;


public class Puntuaciones implements Serializable {

    @SerializedName("puntuacion")
    @Expose
    private List<Puntuacion> puntuacion = null;
    private final static long serialVersionUID = 4418342885309929295L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Puntuaciones() {
    }

    /**
     *
     * @param score
     */
    public Puntuaciones(List<Puntuacion> puntuacion) {
        super();
        this.puntuacion = puntuacion;
    }

    public List<Puntuacion> getPuntuacion() {
        return puntuacion;
    }

    public void setConfiguracion(List<Puntuacion> puntuacion) {
        this.puntuacion= puntuacion;
    }

    public Puntuaciones withScore(List<Puntuacion> puntuacion) {
        this.puntuacion = puntuacion;
        return this;
    }

}
