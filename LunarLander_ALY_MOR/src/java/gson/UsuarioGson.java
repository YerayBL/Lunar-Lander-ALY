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
public class UsuarioGson implements Serializable {
  
    private static final long serialVersionUID = 1L;
   
    private String idUsuario;
    
    private String nombre;
  
    private String apellido;
   
    private String password;
    

    public UsuarioGson() {
    }

    public UsuarioGson(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public UsuarioGson(String idUsuario, String nombre, String apellido, String password) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
