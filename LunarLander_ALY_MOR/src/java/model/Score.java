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
@Table(name = "score")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Score.findAll", query = "SELECT s FROM Score s")
    , @NamedQuery(name = "Score.findByIdPuntuacion", query = "SELECT s FROM Score s WHERE s.idPuntuacion = :idPuntuacion")
    , @NamedQuery(name = "Score.findByVelocidad", query = "SELECT s FROM Score s WHERE s.velocidad = :velocidad")
    , @NamedQuery(name = "Score.findByFuel", query = "SELECT s FROM Score s WHERE s.fuel = :fuel")
    , @NamedQuery(name = "Score.findByInitTime", query = "SELECT s FROM Score s WHERE s.initTime = :initTime")
    , @NamedQuery(name = "Score.findByEndTime", query = "SELECT s FROM Score s WHERE s.endTime = :endTime")})
public class Score implements Serializable {

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

    public Score() {
    }

    public Score(Integer idPuntuacion) {
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
        if (!(object instanceof Score)) {
            return false;
        }
        Score other = (Score) object;
        if ((this.idPuntuacion == null && other.idPuntuacion != null) || (this.idPuntuacion != null && !this.idPuntuacion.equals(other.idPuntuacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Score[ idPuntuacion=" + idPuntuacion + " ]";
    }
    
}
