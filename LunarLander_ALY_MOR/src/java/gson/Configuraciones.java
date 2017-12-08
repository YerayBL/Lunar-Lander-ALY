/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import model.Configuracion;

public class Configuraciones implements Serializable {

    @SerializedName("configuracion")
    @Expose
    private List<Configuracion> configuracion = null;
    private final static long serialVersionUID = 4418342885309929295L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Configuraciones() {
    }

    /**
     *
     * @param configuracion
     */
    public Configuraciones(List<Configuracion> configuracion) {
        super();
        this.configuracion = configuracion;
    }

    public List<Configuracion> getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(List<Configuracion> configuracion) {
        this.configuracion = configuracion;
    }

    public Configuraciones withConfiguracion(List<Configuracion> configuracion) {
        this.configuracion = configuracion;
        return this;
    }

}
