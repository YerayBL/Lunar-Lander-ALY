/*
 * Clase utilidades se encuentran los metodos para parsear de objeto a json
 */
package pck_utilidades;

import com.google.gson.Gson;
import gson.Configuraciones;
import gson.JugadoresTopPartidas;
import gson.Puntuaciones;
import gson.Usuarios;

/**
 *
 * @author tuno
 */
public class Utilidades {

    //Convertir objeto java a JSON String usando el método toJson ().
    //funcion que devuelve un String
    public String objectToJson_String(Configuraciones obj_configuraciones){
        Gson gson = new Gson();
        //Si volem pasar-ho a string
        String jsonInString = gson.toJson(obj_configuraciones); //a String
        return jsonInString;
    }

    //para pasar los valores desde json de las puntuaciones de objeto a string
    public String PuntuacionToJson_String(Puntuaciones obj_puntuaciones){
        Gson gson = new Gson();
        //Si volem pasar-ho a string
        String jsonInString = gson.toJson(obj_puntuaciones); //a String
        return jsonInString;
    }

    //para pasar los valores desde json de los usuarios de objeto a string
    public String UsuarioToJson_String(Usuarios obj_usuarios) {
        Gson gson = new Gson();
        //Si volem pasar-ho a string
        String jsonInString = gson.toJson(obj_usuarios); //a String
        return jsonInString;
    }

    //para pasar los valores desde json de los usuarios con más partidas de objeto a string
    public String JugadoresTopPartidasToJson_String(JugadoresTopPartidas obj_usuarios_partidas){
        Gson gson = new Gson();
        //Si volem pasar-ho a string
        String jsonInString = gson.toJson(obj_usuarios_partidas); //a String
        return jsonInString;
    }

}
