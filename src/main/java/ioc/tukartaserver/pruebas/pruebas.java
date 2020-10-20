/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.pruebas;

import com.google.gson.Gson;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.Usuario;

/**
 *
 * @author Manu
 */
public class pruebas {
public static void main(String...args){
  Usuario user = new Usuario ("karibdys", "manuPass", "manu@manu.com", "Manu", "Mora");
  Gson gson = new Gson();
  String userJson = gson.toJson(user);
  System.out.println("USUARIO: \n  -->"+userJson);
  MensajeSolicitud mensaje = new MensajeSolicitud(Mensaje.FUNCION_LOGIN, userJson);
  String mensajeJson = gson.toJson(mensaje);
  System.out.println("MENSAJE: \n  -->"+mensajeJson);
  
    Usuario userFromUserJson = gson.fromJson(userJson, Usuario.class);
  System.out.println("USUARIO RECUPERADO DEL JSON INICIAL:\n"+ userFromUserJson);
  MensajeSolicitud mensajeFromMensajeJson = gson.fromJson(mensajeJson, MensajeSolicitud.class);
  System.out.println("MENSAJE RECUPERADO DEL JSON INICIAL: \n"+mensajeFromMensajeJson);
  String userFromMensajeFromMensajeJsjon = (String)mensajeFromMensajeJson.getData();
  System.out.println("USUARIO (STRING) RECUPERADO DEL MENSAJE: \n"+userFromMensajeFromMensajeJsjon);
  Usuario userFromStringRecuperado = gson.fromJson(userFromMensajeFromMensajeJsjon, Usuario.class);
  System.out.println("USUARIO (USUARIO) RECUPERADO DEL MENSAJE: \n"+userFromStringRecuperado);

  
}
  
}
