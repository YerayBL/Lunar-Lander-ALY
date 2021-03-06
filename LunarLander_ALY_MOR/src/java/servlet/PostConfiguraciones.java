/*
 * Guarda las configuraciones
 */
package servlet;

import controllers.ConfiguracionJpaController;
import gson.ConfiguracionGson;
import gson.Configuraciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");

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
                List<ConfiguracionGson> listGson = new ArrayList<>();
                for (Configuracion resultado : list) {
                    if (resultado != null) {
                        ConfiguracionGson v_configuracionGson = new ConfiguracionGson();
                        v_configuracionGson.setIdConfiguracion(resultado.getIdConfiguracion());
                        v_configuracionGson.setIdUsuario(resultado.getIdUsuario());
                        v_configuracionGson.setDificultad(resultado.getDificultad());
                        v_configuracionGson.setLuna(resultado.getLuna());
                        v_configuracionGson.setNave(resultado.getNave());
                        listGson.add(v_configuracionGson);
                    }
                }
                obj_configuraciones.setConfiguracion(listGson);
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
            //gestion de errores 
            String mensaje="{\"error\":\"Ha sido imposible guardar los datos.\"}";
            if (ex.getMessage().contains("already exists")){
                mensaje="{\"error\":\"Esta configuración ya existe.\"}";
            }       
            Logger.getLogger(PostConfiguraciones.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(mensaje);
        }
    }
}
