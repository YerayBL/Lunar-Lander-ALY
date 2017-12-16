/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tuno
 */
@Entity
@Table(name = "puntuacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Puntuacion.findAll", query = "SELECT p FROM Puntuacion p")
    , @NamedQuery(name = "Puntuacion.findByIdPuntuacion", query = "SELECT p FROM Puntuacion p WHERE p.idPuntuacion = :idPuntuacion")
    , @NamedQuery(name = "Puntuacion.findByVelocidad", query = "SELECT p FROM Puntuacion p WHERE p.velocidad = :velocidad")
    , @NamedQuery(name = "Puntuacion.findByFuel", query = "SELECT p FROM Puntuacion p WHERE p.fuel = :fuel")
    , @NamedQuery(name = "Puntuacion.findByInitTime", query = "SELECT p FROM Puntuacion p WHERE p.initTime = :initTime")
    , @NamedQuery(name = "Puntuacion.findByEndTime", query = "SELECT p FROM Puntuacion p WHERE p.endTime = :endTime")})
public class Puntuacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_puntuacion")
    private Integer idPuntuacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "velocidad")
    private BigDecimal velocidad;
    @Column(name = "fuel")
    private Integer fuel;
    @Column(name = "init_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date initTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @JoinColumn(name = "id_configuracion", referencedColumnName = "id_configuracion")
    @ManyToOne(optional = false)
    private Configuracion idConfiguracion;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public Puntuacion() {
    }

    public Puntuacion(Integer idPuntuacion) {
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

    public Configuracion getIdConfiguracion() {
        return idConfiguracion;
    }

    public void setIdConfiguracion(Configuracion idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPuntuacion != null ? idPuntuacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Puntuacion)) {
            return false;
        }
        Puntuacion other = (Puntuacion) object;
        if ((this.idPuntuacion == null && other.idPuntuacion != null) || (this.idPuntuacion != null && !this.idPuntuacion.equals(other.idPuntuacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Puntuacion[ idPuntuacion=" + idPuntuacion + " ]";
    }
    
}
