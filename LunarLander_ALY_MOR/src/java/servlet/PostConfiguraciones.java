/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import controllers.ConfiguracionJpaController;
import gson.Configuraciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Configuracion;
import pck_utilidades.Utilidades;

/**
 *
 * @author tuno
 */
@WebServlet(name = "PostConfiguraciones", urlPatterns = {"/PostConfiguraciones"})
public class PostConfiguraciones extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext ().getAttribute( "emf");

        String v_dificultad, v_nave, v_luna, id_configuracion, id_usuario;
        int dificultad, nave, luna;

        id_configuracion = request.getParameter("id_configuracion");
        id_usuario = request.getParameter("id_usuario");

        v_dificultad = request.getParameter("dificultad");
        dificultad = Integer.parseInt(v_dificultad);

        v_nave = request.getParameter("nave");
        nave = Integer.parseInt(v_nave);

        v_luna = request.getParameter("luna");
        luna = Integer.parseInt(v_luna);
        try {
            if (emf != null) {
                //Cogemos objeto de la entidad configuracion
                Configuracion obj_configuracion = new Configuracion(null); // id null, autoincrement
                obj_configuracion.setIdUsuario(id_usuario);
                obj_configuracion.setIdConfiguracion(id_configuracion);
                obj_configuracion.setDificultad(dificultad);
                obj_configuracion.setLuna(luna);
                obj_configuracion.setNave(nave);

                // GUARDAR Configuracion CON CONTROLLER        
                ConfiguracionJpaController obj_UJC = new ConfiguracionJpaController(emf);
                obj_UJC.create(obj_configuracion);

                //Objeto de la clase configuraciones. Tiene todas las configuraciones
                Configuraciones obj_configuraciones = new Configuraciones();

                //recupero los datos  una vez insertado los datos
                EntityManager entitymanager = emf.createEntityManager();
                Query query = entitymanager.createNamedQuery("Configuracion.findByIdUsuario");
                query.setParameter("idUsuario", id_usuario);
                List<Configuracion> list = query.getResultList();
                obj_configuraciones.setConfiguracion(list);
                /*for (Configuracion confi : list) {
                    obj_configuraciones.getConfiguracion().add(confi);
                }*/

                // devolvemos el resultado
                String jsonInString;
                Utilidades operaciones = new Utilidades();
                jsonInString = operaciones.objectToJson_String(obj_configuraciones);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println(jsonInString);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(PostConfiguraciones.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Ha sido imposible guardar los datos\"}");
        }
    }
}
