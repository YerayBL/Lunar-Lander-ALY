/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pck_utilidades;

import com.google.gson.Gson;
import gson.Configuraciones;
import gson.Puntuaciones;

/**
 *
 * @author tuno
 */
public class Utilidades {
    //Convertir objeto java a JSON String usando el método toJson ().
    //funcion que devuelve un String
    public String objectToJson_String(Configuraciones obj_configuraciones){
       try{
            final Gson gson = new Gson();
            //Si volem pasar-ho a string

            String jsonInString = gson.toJson(obj_configuraciones); //a String
            return  jsonInString;
       }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //para pasar los valores desde json de las puntuaciones de objeto a string
    public String PuntuacionToJson_String(Puntuaciones obj_puntuaciones){
       try{
        final Gson gson = new Gson();
        //Si volem pasar-ho a string
        
        String jsonInString = gson.toJson(obj_puntuaciones); //a String
        return  jsonInString;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
