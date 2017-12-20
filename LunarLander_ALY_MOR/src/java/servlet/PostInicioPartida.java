/*
 * Registro de partidas jugadas (data inici)
 */
package servlet;

import controllers.PuntuacionJpaController;
import gson.PuntuacionGson;
import gson.Puntuaciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Puntuacion;
import model.Usuario;
import pck_utilidades.Utilidades;

/**
 *
 * @author tuno
 */
@WebServlet(name = "PostInicioPartida", urlPatterns = {"/PostInicioPartida"})
public class PostInicioPartida extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);

        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager entitymanager = emf.createEntityManager();
        try {
            String v_id_usuario = request.getParameter("id_usuario");
            //calculamos el proximo id            
            Query query = entitymanager.createQuery("Select MAX(p.idPuntuacion) from Puntuacion p");
            List<Integer> list_res_puntuacion = query.getResultList();
            int v_id_puntuacion = 0;
            for (Integer resultado : list_res_puntuacion) {
                if (resultado != null) {
                    v_id_puntuacion = resultado;
                }
            }
            //int v_id_puntuacion = (int) query.getSingleResult();           
            v_id_puntuacion = v_id_puntuacion + 1;// avanzamos con el id de puntuacion--> cogemos la máxima y le sumamos 1 
            // recuperar el usuario(objeto) con el id_usuario recibido por parametro

            Query confQuery = emf.createEntityManager().createNamedQuery("Usuario.findByIdUsuario");
            confQuery.setParameter("idUsuario", v_id_usuario);
            Usuario v_usuario = (Usuario) confQuery.getSingleResult();
            //Creo una nueva puntuación.   
            Puntuacion obj_puntuacion = new Puntuacion(v_id_puntuacion);
            obj_puntuacion.setIdUsuario(v_usuario);
            Date v_date = new Date();
            obj_puntuacion.setInitTime(v_date);

            //GUARDAR PUNTUACION CON CONTROLLER        
            PuntuacionJpaController pjc = new PuntuacionJpaController(emf);
            pjc.create(obj_puntuacion);

            //Objeto de la clase puntuaciones. Tiene todas las puntuaciones
            Puntuaciones obj_puntuaciones = new Puntuaciones();

            //recuperar los datos de las puntuaciones
            query = entitymanager.createNamedQuery("Puntuacion.findByIdPuntuacion");
            query.setParameter("idPuntuacion", v_id_puntuacion);
            List<Puntuacion> list = query.getResultList();
            List<PuntuacionGson> listGson = new ArrayList<>();
            for (Puntuacion resultado : list) {
                if (resultado != null) {
                    PuntuacionGson v_puntuacionGson = new PuntuacionGson();
                    v_puntuacionGson.setIdPuntuacion(resultado.getIdPuntuacion());
                    v_puntuacionGson.setIdUsuario(resultado.getIdUsuario().getIdUsuario());
                    listGson.add(v_puntuacionGson);
                }
            }
            obj_puntuaciones.setPuntuacion(listGson);
            // devolvemos el resultado por un String de json
            String jsonInString;
            Utilidades operaciones = new Utilidades();
            jsonInString = operaciones.PuntuacionToJson_String(obj_puntuaciones);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(jsonInString);

        } catch (Exception e) {
            e.getMessage();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Error al guardar la puntuacion.\"}");
        }
    }
}
