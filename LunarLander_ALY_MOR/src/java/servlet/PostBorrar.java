/*
 * Borrar configuraciones
 */
package servlet;

import controllers.ConfiguracionJpaController;
import gson.ConfiguracionGson;
import gson.Configuraciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
@WebServlet(name = "PostBorrar", urlPatterns = {"/PostBorrar"})
public class PostBorrar extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");

        String v_id_configuracion = request.getParameter("id_configuracion");
        String v_id_usuario = request.getParameter("id_usuario");
        try {
            if (emf != null) {
                /* Query query = entitymanager.createQuery("Delete FROM Configuracion c WHERE c.idConfiguracion = :idConfiguracion");
                query.setParameter("idConfiguracion",v_id_configuracion);
                query.executeUpdate();*/
                Configuracion obj_confi = new Configuracion(null); //id null, autoincrement
                obj_confi.setIdConfiguracion(v_id_configuracion);
                obj_confi.setIdUsuario(v_id_usuario);

                //BORRAR CONFIGURACION CON CONTROLLER        
                ConfiguracionJpaController cjc = new ConfiguracionJpaController(emf);
                cjc.destroy(v_id_configuracion);

                // pw.println("{\"mess\":\"Guardado correctamente\"}");
                //Objeto de la clase configuraciones. Tiene todas las configuraciones
                Configuraciones obj_configuraciones = new Configuraciones();

                //recupero los datos  una vez insertado los datos
                EntityManager entitymanager = emf.createEntityManager();
                Query query = entitymanager.createNamedQuery("Configuracion.findByIdUsuario");
                query.setParameter("idUsuario", v_id_usuario);
                List<Configuracion> list = query.getResultList();
                List<ConfiguracionGson> listGson = new ArrayList<>();
                for (Configuracion resultado : list) {
                    if (resultado != null) {
                        ConfiguracionGson v_configuracionGson = new ConfiguracionGson();
                        v_configuracionGson.setIdUsuario(resultado.getIdUsuario());
                        v_configuracionGson.setIdConfiguracion(resultado.getIdConfiguracion());
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
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Ha sido imposible borrar la configuración.\"}");
        }

    }
}
