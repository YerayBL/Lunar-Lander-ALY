/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import java.io.Serializable;
import java.util.List;

public class Configuraciones implements Serializable {

   
    private List<ConfiguracionGson> configuracion = null;
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
    public Configuraciones(List<ConfiguracionGson> configuracion) {
        super();
        this.configuracion = configuracion;
    }

    public List<ConfiguracionGson> getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(List<ConfiguracionGson> configuracion) {
        this.configuracion = configuracion;
    }

    public Configuraciones withConfiguracion(List<ConfiguracionGson> configuracion) {
        this.configuracion = configuracion;
        return this;
    }

}
