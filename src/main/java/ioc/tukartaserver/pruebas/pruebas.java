/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.pruebas;

import com.google.gson.Gson;
import ioc.tukartaserver.gestorDB.GestorDB;
import ioc.tukartaserver.model.Alergeno;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Estado;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.Mesa;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.model.Pedido;
import ioc.tukartaserver.model.Producto;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Utiles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;


/**
 *
 * @author Manu
 */
public class pruebas {
private static final String CLASE = "org.postgresql.Driver";
private static final String LOCAL_URL = "jdbc:postgresql://localhost:5432/TuKarta";    
private static final String USER = "tukarta";
private static final String PASS = "TuKartaP4$$"; 
public static final String LIST_PEDIDO_FROM_USER = "SELECT* from PEDIDO WHERE empleado = ? AND activo = true";



public static void main (String... args) throws SQLException, ClassNotFoundException {   
  /*
  Class.forName(CLASE);    
  Connection con = DriverManager.getConnection(LOCAL_URL,USER,PASS); 
  MensajeRespuesta ret = null;
  ArrayList<Pedido> listado = new ArrayList<>();
  String id = "manu@tukarta.com";
  try {
    PreparedStatement stm =null;
    stm = con.prepareStatement("SELECT * FROM PRODUCTO WHERE id = 'M001'");
    System.out.println(" sentencia --> "+stm);
    ResultSet res = stm.executeQuery();
    while (res.next()){
      Producto producto = Utiles.createProductoFromResultSet(res, new GestorDB());
      System.out.println(producto);
      
    }
   
    System.out.println("*****************\n"+ret);
  
  } catch (SQLException ex) {
    System.err.println(ex.getMessage());
  }*/
MensajeRespuesta res = Utiles.mensajeOK("HOL");
ArrayList<Usuario> users = new ArrayList<>();
users.add(new Usuario("id1"));
users.add(new Usuario("id2"));
users.add(new Usuario("id3"));
Gson gson = new Gson();
String arrayJSON = gson.toJson(users);
res.setData(arrayJSON);

String mensJSON = gson.toJson(res);

MensajeRespuesta ret = gson.fromJson(mensJSON, MensajeRespuesta.class);
String data = ret.getData();
System.out.println(data);

String[] datos = gson.fromJson(data, String[].class);
System.out.println(datos.length);


}

public static int devolver(String[] prod){
  return prod.length;
}
  
}
