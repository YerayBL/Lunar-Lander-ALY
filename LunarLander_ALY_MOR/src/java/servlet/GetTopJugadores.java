/*
 * Página de listado de jugadores con más partidas .
 */
package servlet;

import gson.JugadorTopPartida;
import gson.JugadoresTopPartidas;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pck_utilidades.Utilidades;

/**
 *
 * @author tuno
 */
@WebServlet(name = "GetTopJugadores", urlPatterns = {"/GetTopJugadores"})
public class GetTopJugadores extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager entitymanager = emf.createEntityManager();
        try {
            JugadoresTopPartidas obj_partidas = new JugadoresTopPartidas();

            Query query = entitymanager.createQuery("SELECT u.idUsuario, COUNT(p.idPuntuacion) FROM Puntuacion p INNER JOIN p.idUsuario u GROUP BY u.idUsuario").setMaxResults(10);  //
            List<JugadorTopPartida> list = query.getResultList();

            obj_partidas.setJugadoresTopPartidas(list);

            // devolvemos el resultado por un String de json
            String jsonInString;
            Utilidades operaciones = new Utilidades();
            jsonInString = operaciones.JugadoresTopPartidasToJson_String(obj_partidas);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(jsonInString);
        } catch (Exception e) {
            e.getMessage();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Error al guardar la lista de usuarios online.\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
