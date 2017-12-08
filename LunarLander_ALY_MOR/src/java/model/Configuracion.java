/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tuno
 */
@Entity
@Table(name = "configuracion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Configuracion.findAll", query = "SELECT c FROM Configuracion c")
    , @NamedQuery(name = "Configuracion.findByIdConfiguracion", query = "SELECT c FROM Configuracion c WHERE c.idConfiguracion = :idConfiguracion")
    , @NamedQuery(name = "Configuracion.findByIdUsuario", query = "SELECT c FROM Configuracion c WHERE c.idUsuario = :idUsuario")
    , @NamedQuery(name = "Configuracion.findByDificultad", query = "SELECT c FROM Configuracion c WHERE c.dificultad = :dificultad")
    , @NamedQuery(name = "Configuracion.findByNave", query = "SELECT c FROM Configuracion c WHERE c.nave = :nave")
    , @NamedQuery(name = "Configuracion.findByLuna", query = "SELECT c FROM Configuracion c WHERE c.luna = :luna")})
public class Configuracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_configuracion")
    private String idConfiguracion;
    @Basic(optional = false)
    @Column(name = "id_usuario")
    private String idUsuario;
    @Column(name = "dificultad")
    private Integer dificultad;
    @Column(name = "nave")
    private Integer nave;
    @Column(name = "luna")
    private Integer luna;

    public Configuracion() {
    }

    public Configuracion(String idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public Configuracion(String idConfiguracion, String idUsuario) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConfiguracion != null ? idConfiguracion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configuracion)) {
            return false;
        }
        Configuracion other = (Configuracion) object;
        if ((this.idConfiguracion == null && other.idConfiguracion != null) || (this.idConfiguracion != null && !this.idConfiguracion.equals(other.idConfiguracion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Configuracion[ idConfiguracion=" + idConfiguracion + " ]";
    }
    
}
