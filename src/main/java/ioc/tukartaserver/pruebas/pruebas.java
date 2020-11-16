/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.pruebas;

import ioc.tukartaserver.gestorDB.GestorDB;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Estado;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.Mesa;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.model.Pedido;
import ioc.tukartaserver.model.Producto;
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


public static void main (String... args) throws SQLException, ClassNotFoundException {   
  Usuario usuarioCorrecto = new Usuario ("karibdys", "manuPass", "manu@tukarta.com", "manu", "mora", false);
  Empleado emp = new Empleado (usuarioCorrecto);
  Mesa mesa = new Mesa("mesa1", 4);
  Class.forName(CLASE);    
  Connection con = DriverManager.getConnection(LOCAL_URL,USER,PASS); 
  Pedido pedido = new Pedido();
  pedido.setId("MCANMARC_2");
  pedido.setEmpleado(emp);
  pedido.setMesa(mesa);
  pedido.setFecha(new Date());
  pedido.setActivo(true);
  ArrayList<Producto> listaProd = new ArrayList<>();
  Producto prod1 = new Producto();
  prod1.setId("P002");
  listaProd.add(prod1);
  listaProd.add(prod1);
  listaProd.add(prod1);
  Producto prod2 = new Producto();
  prod2.setId("B001");
  listaProd.add(prod2);
  pedido.setLista_productos(listaProd);
  
  ArrayList<Estado> listaEstados = new ArrayList<>();
  listaEstados.add(Estado.PREPARADO);
  listaEstados.add(Estado.PREPARADO);
  listaEstados.add(Estado.PREPARADO);
  listaEstados.add(Estado.EN_PREPARACIÓN);
  pedido.setEstado_productos(listaEstados);
  
  ArrayList<Producto> productos = pedido.getLista_productos();
  ArrayList<Estado> estados = pedido.getEstado_productos();
  System.out.println(Utiles.sentenciaPedidoToInsertSQL(pedido));
  int pos =0;
  PreparedStatement pstm = null;
  pstm = con.prepareStatement("SELECT count(id) FROM pedido_estado");
  ResultSet res = pstm.executeQuery();
  int idInicial=0;
  if (res.next()){
    idInicial=res.getInt(1);
  }
  
   do{
      pstm = con.prepareStatement(GestorDB.INSERT_PEDIDO_ESTADO);
      idInicial++;
      //introducimos los datos
      pstm.setInt(1, idInicial);
      pstm.setString(2, pedido.getId());
      pstm.setString(3, productos.get(pos).getId());
      pstm.setString(4, estados.get(pos).getEstado());
      System.out.println(pstm);
      pos++;      
    }while (pos<productos.size());        
  
  /*
   * Pruebas de conexion 
   */  
  /*
  Class.forName(CLASE);    
  Connection con = DriverManager.getConnection(LOCAL_URL,USER,PASS); 
  MensajeRespuesta ret = null;
  ArrayList<Empleado> listado = new ArrayList<>();
  String id = "marc@tukarta.com";
  try {
    PreparedStatement stm =null;
    stm = con.prepareStatement(LIST_USERS_FROM_GESTOR);
    stm.setString(1, id);
    System.out.println(" sentencia --> "+stm);
    ResultSet res = stm.executeQuery();
    while (res.next()){
      Usuario user = Utiles.createEmpleadoFromResultSet(res, false);
      System.out.println(user.getEmail());
      listado.add((Empleado)user);
      System.out.println("\n"+user+"\n");
    }
    System.out.println("Total usuarios: "+listado.size());
    Gson gson = new Gson();
    String arrayJSON = gson.toJson(listado);
    ret = Utiles.mensajeOK("listusers");
    ret.setData(arrayJSON);
    System.out.println("*****************\n"+ret);
  
  } catch (SQLException ex) {
    System.err.println(ex.getMessage());
  }
  */
  
  
}
  
}
