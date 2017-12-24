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
public class ConfiguracionGson implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idConfiguracion;

    private String idUsuario;

    private Integer dificultad;

    private Integer nave;

    private Integer luna;

    public ConfiguracionGson() {
    }

    public ConfiguracionGson(String idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public ConfiguracionGson(String idConfiguracion, String idUsuario) {
        this.idConfiguracion = idConfiguracion;
        this.idUsuario = idUsuario;
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

    public Integer getDificultad() {
        return dificultad;
    }

    public void setDificultad(Integer dificultad) {
        this.dificultad = dificultad;
    }

    public Integer getNave() {
        return nave;
    }

    public void setNave(Integer nave) {
        this.nave = nave;
    }

    public Integer getLuna() {
        return luna;
    }

    public void setLuna(Integer luna) {
        this.luna = luna;
    }
}
