/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 *
 * @author tuno
 */
public class PuntuacionGson implements Serializable {
 
    private static final long serialVersionUID = 1L;
    
    private Integer idPuntuacion;
    
    private BigDecimal velocidad;
    private Integer fuel;
    
    private Date initTime;
   
    private Date endTime;
   
    private String idConfiguracion;
   
    private String idUsuario;

    public PuntuacionGson() {
    }

    public PuntuacionGson(Integer idPuntuacion) {
        this.idPuntuacion = idPuntuacion;
    }

    public Integer getIdPuntuacion() {
        return idPuntuacion;
    }

    public void setIdPuntuacion(Integer idPuntuacion) {
        this.idPuntuacion = idPuntuacion;
    }

    public BigDecimal getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(BigDecimal velocidad) {
        this.velocidad = velocidad;
    }

    public Integer getFuel() {
        return fuel;
    }

    public void setFuel(Integer fuel) {
        this.fuel = fuel;
    }

    public Date getInitTime() {
        return initTime;
    }

    public void setInitTime(Date initTime) {
        this.initTime = initTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getIdConfiguracion() {
        return idConfiguracion;
    }

    public void setIdConfiguracion(String idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }


    
}

