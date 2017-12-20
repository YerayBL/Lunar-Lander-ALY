/*
 * Registro de partidas jugadas ( data fi de cada partida)
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import model.Configuracion;
import model.Puntuacion;

/**
 *
 * @author tuno
 */
@WebServlet(name = "PostFinPartida", urlPatterns = {"/PostFinPartida"})
public class PostFinPartida extends HttpServlet {

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
            String v_string_id_puntuacion = request.getParameter("id_puntuacion");
            int v_id_puntuacion = Integer.parseInt(v_string_id_puntuacion);
            String v_id_configuracion = request.getParameter("id_configuracion");
            String v_string_velocidad = request.getParameter("velocidad");
            BigDecimal v_velocidad = new BigDecimal(v_string_velocidad);
            v_velocidad = v_velocidad.setScale(2, RoundingMode.CEILING);
            String v_string_fuel = request.getParameter("fuel");
            int v_fuel = Integer.parseInt(v_string_fuel);
            entitymanager.getTransaction().begin();

            Puntuacion obj_puntuacion = entitymanager.find(Puntuacion.class, v_id_puntuacion);
            Date v_date = new Date();

            Query confQuery = emf.createEntityManager().createNamedQuery("Configuracion.findByIdConfiguracion");
            confQuery.setParameter("idConfiguracion", v_id_configuracion);
            Configuracion v_configuracion = null;
            List<Configuracion> list_res_configuracion = confQuery.getResultList();
            for (Configuracion resultado : list_res_configuracion) {
                if (resultado != null) {
                    v_configuracion = resultado;
                }
            }
            obj_puntuacion.setEndTime(v_date);
            obj_puntuacion.setFuel(v_fuel);
            obj_puntuacion.setVelocidad(v_velocidad);
            obj_puntuacion.setIdConfiguracion(v_configuracion);

            // aseguro los datos en la base de datos
            entitymanager.getTransaction().commit();

            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"mensaje\":\"Los datos se han guardado correctamente.\"}");

        } catch (Exception e) {
            e.getMessage();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Error al guardar la puntuacion.\"}");
        }
    }

}
