/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.pruebas;

import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Rol;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.model.Utiles;


/**
 *
 * @author Manu
 */
public class pruebas {
private static final String CLASE = "org.postgresql.Driver";
private static final String LOCAL_URL = "jdbc:postgresql://localhost:5432/TuKarta";    
private static final String USER = "tukarta";
private static final String PASS = "TuKartaP4$$"; 

public static void main (String... args) throws ClassNotFoundException{    
  Usuario user = new Usuario("Marc", "marcPass", "marc@tukarta.com", null, null, true);   
  Empleado emp = new Empleado ("Pepe", "pepePass", "pepe@tukarta.com", null, null, user, Rol.CAMARERO);
  String sentencia = Utiles.sentenciaEmpleadoToInsertSQL(emp);
  System.out.println(sentencia);
    
}
  
}
