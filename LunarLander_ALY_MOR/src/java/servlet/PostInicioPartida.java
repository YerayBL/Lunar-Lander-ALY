/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import Utilidades.Utilidades;
import controllers.ScoreJpaController;
import gson.Scores;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Score;

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

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("LunarLander_ALY_MORPU");
        EntityManager entitymanager = emf.createEntityManager();
        try {
            int v_id_puntuacion;

            String v_id_usuario = request.getParameter("id_usuario");
            String v_id_configuracion = request.getParameter("id_configuracion");
            v_id_puntuacion = Integer.parseInt("id_puntuacion");//parseo a String
            String id_puntuacion = request.getParameter("id_puntuacion");

            //Creo una nueva puntuación. L //GUARDAR SCORE CON CONTROLLER     
            Score score = new Score(null); //id null, autoincrement
            score.setIdUsuario(v_id_usuario);
            score.setIdConfiguracion(v_id_configuracion);
            score.setIdPuntuacion(v_id_puntuacion);

            //GUARDAR PUNTUACION CON CONTROLLER        
            ScoreJpaController sjc = new ScoreJpaController(emf);
            sjc.create(score);

            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"mess\":\"La puntuacion ha sido guardada correctamente.\"}");

            //Objeto de la clase Scores. Tiene todas las puntuaciones
            Scores obj_score = new Scores();

            //recupero los datos  una vez insertado los datos
            Query query = entitymanager.createNamedQuery("Score.findByIdPuntuacion");
            query.setParameter("idPuntuacion", v_id_puntuacion);
            List<Score> list = query.getResultList();
            obj_score.setScore(list);
            // devolvemos el resultado por un String de json
            String jsonInString;
            Utilidades operaciones = new Utilidades();
            jsonInString = operaciones.objectToJson_String(obj_score);
            response.setContentType("application/json");
            pw.println(jsonInString);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Error al guardar la puntuacion.\"}");
        }

    }

}
