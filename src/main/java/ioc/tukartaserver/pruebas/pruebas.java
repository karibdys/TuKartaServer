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
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author Manu
 */
public class pruebas {
private static final String CLASE = "org.postgresql.Driver";
private final String LOCAL_URL = "jdbc:postgresql://localhost:5432/TuKarta";    
private final String USER = "tukarta";
private final String PASS = "TuKartaP4$$"; 


public static void main (String... args) throws ClassNotFoundException, SQLException{    
    Usuario usuarioCorrecto = new Usuario ("karibdys", "manuPass", "manu@tukarta.com", "manu", "mora", false);
    Empleado emp = new Empleado(usuarioCorrecto);
    emp.setSalario(9000);
    String sentencia = Utiles.sentenciaUsuarioToUpdateSQL(emp);
    System.out.println(sentencia);

    
}
  
}
