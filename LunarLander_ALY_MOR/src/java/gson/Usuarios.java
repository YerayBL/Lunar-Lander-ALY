/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author tuno
 */
public class Usuarios implements Serializable{
    
   private List<UsuarioGson> usuario = null;
   private final static long serialVersionUID = 4418342885309929295L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Usuarios() {
    }

    /**
     *
     * @param usuario
     */
    public Usuarios(List<UsuarioGson> usuario) {
        super();
        this.usuario = usuario;
    }

    public List<UsuarioGson> getUsuario() {
        return usuario;
    }

    public void setUsuario(List<UsuarioGson> usuario) {
        this.usuario = usuario;
    }

    public Usuarios withUsuario(List<UsuarioGson> usuario) {
        this.usuario = usuario;
        return this;
    }
   
}
