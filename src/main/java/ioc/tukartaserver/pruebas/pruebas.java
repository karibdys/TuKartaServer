/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.pruebas;

import ioc.tukartaserver.model.Codigo;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.MensajeRespuesta2;
import ioc.tukartaserver.model.Rol;
import ioc.tukartaserver.model.Usuario;

/**
 *
 * @author Manu
 */
public class pruebas {

public static void main (String... args){  
  
  MensajeRespuesta2 mensaje = new MensajeRespuesta2();
  mensaje.setCode(Codigo.CODIGO_ERR);
  mensaje.setData("Hola");
  System.out.println(mensaje);
}
  
}
