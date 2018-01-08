/*
 * Página de jugadors online (que estan y hayan estado 5 min en linea )
 */
package servlet;

import gson.UsuarioGson;
import gson.Usuarios;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
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
import model.Usuario;
import pck_utilidades.Utilidades;

/**
 *
 * @author tuno
 */
@WebServlet(name = "GetUsuariosOnLine", urlPatterns = {"/GetUsuariosOnLine"})
public class GetUsuariosOnLine extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext ().getAttribute( "emf");
        EntityManager entitymanager = emf.createEntityManager(); 
        try {
            Usuarios obj_usuarios = new Usuarios(); 
            Date v_margen=new Date();
            Calendar calendar = Calendar.getInstance();	
            calendar.setTime(v_margen); 
            calendar.add(Calendar.MINUTE, -5); 
            v_margen= calendar.getTime(); 
            
            Query query = entitymanager.createQuery ("SELECT DISTINCT u FROM Puntuacion p INNER JOIN p.idUsuario u WHERE p.initTime > :p_margen");  //
            query.setParameter("p_margen", v_margen);
            List<Usuario> list = query.getResultList();
            List<UsuarioGson> listGson =new ArrayList<>();
            for (Usuario resultado : list) {
                if (resultado!=null){
                  UsuarioGson v_usuarioGson=new UsuarioGson();
                  v_usuarioGson.setIdUsuario(resultado.getIdUsuario());
                  v_usuarioGson.setApellido(resultado.getApellido());
                  v_usuarioGson.setNombre(resultado.getNombre());
                  v_usuarioGson.setPassword(resultado.getPassword());
                  listGson.add(v_usuarioGson);
                }  
            }             
            obj_usuarios.setUsuario(listGson);          
   
            // devolvemos el resultado por un String de json
            String jsonInString;
            Utilidades operaciones = new Utilidades();
            jsonInString = operaciones.UsuarioToJson_String(obj_usuarios);
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
