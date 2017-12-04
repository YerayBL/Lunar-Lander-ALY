/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

/**
 *
 * @author tuno
 */
import java.util.List;
import model.Configuracion;

public class Configuraciones {

    public List<Configuracion> configuracion = null;

    
    public Configuraciones() {
    }

   
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
