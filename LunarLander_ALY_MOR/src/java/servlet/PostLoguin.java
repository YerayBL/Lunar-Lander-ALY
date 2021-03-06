/*
 * Sistema de registro básico (usuario,password, cookies)
 */
package servlet;

import gson.ConfiguracionGson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Usuario;
import gson.Configuraciones;
import java.util.ArrayList;
import model.Configuracion;
import pck_utilidades.Utilidades;

/**
 *
 * @author tuno
 */
@WebServlet(name = "PostLoguin", urlPatterns = {"/PostLoguin"})
public class PostLoguin extends HttpServlet {

    String v_id_usuario = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }/*
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }*/

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // processRequest(request, response);
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager entitymanager = emf.createEntityManager();
        // CREAR Y RECUPERAR COOKIE
        String cookieName = "cookie_usuario";
        // lo primero es validar el usuario, o por cookies o por parametros
        //Busco login=... en las cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName())) {
                    v_id_usuario = cookie.getValue();
                }
            }

        } else {
            // no hemos recibido cookies, cogemos los parametros y validamos
            //Pedir parametros
            String id_usuario = request.getParameter("id_usuario");
            String password = request.getParameter("password");
            try {
                // primero validamos q el login es correcto
                Query query = entitymanager.createQuery("SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario and u.password = :password");
                query.setParameter("idUsuario", id_usuario);
                query.setParameter("password", password);
                List<Usuario> list = query.getResultList();

                if (list.size() > 0) {
                    v_id_usuario = id_usuario;
                    // aqui crear la cookie y añadirla al response
                    Cookie userCookie = new Cookie("cookie_usuario", v_id_usuario);
                    userCookie.setMaxAge(60 * 60 * 24 * 365); //Store cookie for 1 year
                    userCookie.setPath("/");
                    response.addCookie(userCookie);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println("{\"error\":\"Error de BD\"}");
            }
        }
        // tanto si nos hemo validado por cookies como por parametros, devolvemos las configuraciones
        // si el v_id_usuario esta informado
        if (v_id_usuario != "") {

            try {
                Configuraciones obj_configuraciones = new Configuraciones();
                // segundo, si se ha validado devolvemos las configuraciones de ese usuario
                //recupero los datos  una vez insertado los datos
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

            } catch (Exception ex) {
                ex.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                PrintWriter pw = response.getWriter();
                pw.println("{\"error\":\"Ha sido imposible guardar los datos\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println("{\"error\":\"Usuario o Password incorrectos\"}");
        }
    }

}
