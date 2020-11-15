/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.pruebas;

import static ioc.tukartaserver.gestorDB.GestorDB.LIST_USERS_FROM_GESTOR;
import static ioc.tukartaserver.gestorDB.GestorDB.LIST_USERS_FROM_REST;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.Rol;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.model.Utiles;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author Manu
 */
public class pruebas {
private static final String CLASE = "org.postgresql.Driver";
private static final String LOCAL_URL = "jdbc:postgresql://localhost:5432/TuKarta";    
private static final String USER = "tukarta";
private static final String PASS = "TuKartaP4$$"; 


public static void main (String... args) throws ClassNotFoundException, SQLException{    
  /*
   * Pruebas de conexion 
   */  
  
  Class.forName(CLASE);    
  Connection con = DriverManager.getConnection(LOCAL_URL,USER,PASS); 
  MensajeRespuesta ret = null;
  ArrayList<Empleado> listado = new ArrayList<>();
  String id = "res1TuKarta";
  try {
    PreparedStatement stm =null;
    stm = con.prepareStatement(LIST_USERS_FROM_REST);
    stm.setString(1, id);
    System.out.println(" sentencia --> "+stm);
    ResultSet res = stm.executeQuery();
    while (res.next()){
      System.out.println(res.getString("email"));
    }
  
  } catch (SQLException ex) {
    System.err.println(ex.getMessage());
  }
}
  
}
