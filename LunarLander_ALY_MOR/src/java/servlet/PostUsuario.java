/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import controllers.UsuarioJpaController;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Usuario;

/**
 *
 * @author tuno
 */
@WebServlet(name = "PostUsuario", urlPatterns = {"/PostUsuario"})
public class PostUsuario extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
       EntityManagerFactory emf = (EntityManagerFactory) getServletContext ().getAttribute( "emf");
        try {
            String id_usuario = request.getParameter("id_usuario");
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String password = request.getParameter("password");

            //Cogemos objeto de la entidad usuario 
            Usuario obj_usuario = new Usuario(null); // id null, autoincrement
            obj_usuario.setIdUsuario(id_usuario);
            obj_usuario.setNombre(nombre);
            obj_usuario.setApellido(apellido);
            obj_usuario.setPassword(password);

            // GUARDAR USUARIO CON CONTROLLER        
            UsuarioJpaController obj_UJC = new UsuarioJpaController(emf);
            obj_UJC.create(obj_usuario);

            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"mess\":\"El usuario ha sido guardado correctamente.\"}");

        } catch (Exception e) {
            String mensaje_error = "{\"error\":\"Ha sido imposible guardar los datos\"}";
            if (e.getMessage().contains("Duplicate entry")) {
                mensaje_error = "{\"error\":\"Ya existe un usuario registrado con ese id\"}";
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(mensaje_error);
        }
    }
}
