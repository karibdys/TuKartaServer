package ioc.tukartaserver.pruebas;

/**
 *
 * @author Manu
 */

import com.google.gson.Gson;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Estado;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.Mesa;
import ioc.tukartaserver.model.Pedido;
import ioc.tukartaserver.model.Producto;
import ioc.tukartaserver.model.Provincia;
import ioc.tukartaserver.model.Restaurante;
import ioc.tukartaserver.model.Rol;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

public class Cliente {
//accesos local
private int LOCAL_PORT =200;
private final String LOCAL_HOST = "localhost";

private final String CLIENTE="CLIENTE: ";
private Socket cs;
private PrintStream  out;	
private Gson gson;

private TokenSesion tokenUser;

BufferedReader in;

public Cliente() throws IOException{
  gson = new Gson();
  try{ 
    cs = new Socket(LOCAL_HOST, LOCAL_PORT);
  }catch (Exception e) {    
    System.out.println(CLIENTE+"ERROR CREADO EL CLIENTE: "+e.getMessage());   
  }
  System.out.println(CLIENTE+"Cliente creado.");
}

public void startClient() {
  try {
    //creamos los canales de entrada y salida:
    out= new PrintStream(cs.getOutputStream());
    in= new BufferedReader(new InputStreamReader(cs.getInputStream()));
    //leemos
    System.out.println(CLIENTE+"preparado para leer");
    String mensajeString=in.readLine();    
    System.out.println(CLIENTE+"mensaje recibido: \n  -->"+mensajeString);
    MensajeRespuesta mensajeRes = gson.fromJson(mensajeString, MensajeRespuesta.class);    
    Codes code=mensajeRes.getCode();
    System.out.println("SERVER: código "+code.getCode()); 
    
    if (code.getCode().equals(Codes.CODIGO_OK)) {
      
      Usuario user = new Usuario("Marc", "marcPass", "marc@tukarta.com", null, null, true);    
      Empleado emp = new Empleado ("Manu", "manuPass", "manu@tukarta.com", null, null, user, Rol.CAMARERO);
      Empleado pepe = new Empleado ("Pepe", "pepePass", "pepe@tukarta.com", null, null, user, Rol.CAMARERO);
      Empleado empMod = new Empleado ("Manué", "manuPass", "manu@tukarta.com", "Manuel Jesús", null, user, Rol.CAMARERO);
      empMod.setSalario(1700);
      Mesa mesa = new Mesa("mesa1CanMarc", 4);
      Pedido pedido = new Pedido();

      pedido.setEmpleado(emp);
      pedido.setFecha(new Date());
      pedido.setMesa(mesa);
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
      listaEstados.add(Estado.LISTO);
      listaEstados.add(Estado.LISTO);
      listaEstados.add(Estado.LISTO);
      listaEstados.add(Estado.PREPARANDO);
      pedido.setEstado_productos(listaEstados);
      
      //Usuario user = new Usuario("karibdys", "manuPass", "manu@tukarta.com", null, null, false);
      /*******************PRUEBA DE LOGIN************************/          
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de login"); 
      //lo convertimos en JSON
      String userJson = gson.toJson(emp);
      //creamos el mensajeRes      
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LOGIN, userJson, (String) null);
      */
      
      /*******************PRUEBA DE LOGOFF************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de logout");            
      String userJson = gson.toJson(user);
      TokenSesion token = new TokenSesion(user);
      token.setToken("oiYqgNGodc");
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LOGOFF, userJson, token);
      */
     
     
      /*******************PRUEBA DE SELECT DATA USER************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de select_data_user");      
      String userJson = gson.toJson(user);
      TokenSesion token = new TokenSesion(user);
      token.setToken("tGpkzSvAEr");
       Usuario userRet = new Usuario();
      userRet.setEmail("manu@tukarta.com");
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_DATOS_OTRO_USER, userRet, token);   
      */
      
      
      /*******************PRUEBA DE ADD DATA EMP ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de add_empleado");   
      TokenSesion token = new TokenSesion(user);
      token.setToken("tGpkzSvAEr");  
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_ADD_EMP, pepe, token);
      */
      
      /*******************PRUEBA DE ADD DATA GESTOR ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de add_gestor");   
      TokenSesion token = new TokenSesion(user);
      token.setToken("YOZVzhZXeP");  
      Usuario userGestor = new Usuario("Gestor", "gestorPass", "gestor@tukarta.com", null, null, true);    
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_ADD_GESTOR, userGestor, token);
      */
      
      /*******************PRUEBA DE BAJA USER************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de baja_user");   
      TokenSesion token = new TokenSesion(user);
      token.setToken("rKoYsKGIfy");  
      Usuario userGestor = new Usuario();    
      userGestor.setEmail("gestor@tukarta.com");
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_BAJA_USER, userGestor, token);
      */
      
      /*******************PRUEBA DE UPDATE USER************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de update_emp");   
      TokenSesion token = new TokenSesion(user);
      token.setToken("hQffmzTHaf");        
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_UPDATE_EMP, empMod, token);
      */
      
      /*******************PRUEBA DE LIST USER************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de list_data_users");   
      TokenSesion token = new TokenSesion(user);
      token.setToken("REzEklFEEQ");      
      Restaurante rest = new Restaurante ("res1Marc", "CanMarc");
      //MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LIST_USERS_FROM_GESTOR, null, token);
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LIST_USERS_FROM_REST, rest, token);
      */      
      
      /*******************PRUEBA DE ADD DATA PEDIDO ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de add_empleado");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("vZRESMJPTH");  
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_ADD_PEDIDO, pedido, token);
      */
      
      /*******************PRUEBA DE LIST PEDIDO ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de list_pedido_from_user");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("SBOtoUqGRU");  
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LIST_PEDIDO_FROM_USER, null, token);        
      */
       
      /*******************PRUEBA DE LIST PEDIDO COMPLETO ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de list_pedido_completo_from_user");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("AuhGiSKJUa");  
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LIST_PEDIDO_COMPLETO_FROM_USER, emp, token);        
      */
      
             
      /*******************PRUEBA DE LIST PEDIDO COMPLETO FROM ID************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de list_pedido_completo_from_user");   
      TokenSesion token = new TokenSesion(emp);
      Pedido idPedido = new Pedido();
      idPedido.setId("pedido1T");
      token.setToken("UYqlpCxkoN");  
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LIST_PEDIDO_COMPLETO_FROM_ID, idPedido, token);        
      */
      
      /*******************PRUEBA DE DELETE DATA PEDIDO ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de delete_pedido");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("nsafoOfcuu");  
      pedido = new Pedido();
      pedido.setId("pedido3M");
      
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_DELETE_PEDIDO, pedido, token);
      */
      
      /*******************PRUEBA DE ADD PRODUCTO TO ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de add_pedido_to");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("YiWUWcSngI");  
      String[] datos = {"B001", "pedido1M", null};
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_ADD_PRODUCTO_TO, datos, token);
      */
      
       /*******************PRUEBA DE UPDATE PEDIDO************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de update_pedido");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("iQucZTDsTe");        
      Pedido pedidoNuevo = new Pedido();
      pedidoNuevo.setId("pedido1M");
      pedidoNuevo.setPrecio_final(100);
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_UPDATE_PEDIDO, pedidoNuevo, token);
      */
      
       /*******************PRUEBA DE DELETE FROM  PEDIDO************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de delete_producto_from sin id registro");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("LbmuOOSErv");  
      String[] datos = {"B001", "pedido1M", null};
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_DELETE_PRODUCTO_FROM, datos, token);
      */
      
      /*******************PRUEBA DE DELETE FROM  PEDIDO************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de delete_producto_from sin id registro");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("EUvsWxuAUA");  
      String[] datos = {"B001", "pedido1M", null};
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_DELETE_PRODUCTO_FROM, datos, token);
      */
      
      /*******************PRUEBA DE UPDATE FROM  PEDIDO con id************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de update_pedido_from con id registro");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("EUvsWxuAUA");  
      String[] datos = {"pedido1M-3", "listo"};
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_UPDATE_PRODUCTO_FROM, datos, token);
      */
      
      /*******************PRUEBA DE UPDATE FROM  PEDIDO sin id************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de update_pedido_from sin id registro");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("mbWlcWkxRI");  
      String[] datos = {"B001", "pedido1M", "preparando", "listo"};
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_UPDATE_PRODUCTO_FROM, datos, token);
      */
      
      /*******************PRUEBA DE UPDATE FROM  PEDIDO sin id************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de list_pedidos_pendientes");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("pmOLcVcTmU");        
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LIST_PRODUCTOS_PENDIENTES, null, token);
      */
      
      /*******************PRUEBA DE ADD DATA REST ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de add_empleado");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("aZYVvPivsu");  
      Restaurante rest = new Restaurante("res4Pepe", "Cam Pepe", Provincia.HUELVA, "Calle de Huelva mu bonita");
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_ADD_RESTAURANTE, rest, token);
      */
     
      /*******************PRUEBA DE ADD DATA MESA ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de add_empleado");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("CVEyrJXncn");  
      Mesa mesaNva = new Mesa();
      mesaNva.setId("mesa8CanMarc");
      mesaNva.setNum_comensales(8);
      mesaNva.setIdRestaurante("res1Marc");      
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_ADD_MESA, mesaNva, token);
      */
       /*******************PRUEBA DE ADD DATA PRODUCTO ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de add_empleado");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("KvNBWhNPdb");  
      Producto producto = new Producto();
      producto.setId("B003");
      producto.setNombre("Pepsi");
      producto.setPrecio(1.20f);
      producto.setDisponibles(500);
      producto.setTiempo_elaboracion(0);   
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_ADD_PRODUCTO, producto, token);
      */
      
      /*******************PRUEBA DE LIST PRODUCTOS ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de add_empleado");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("kfFcDEWjKE");        
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LIST_PRODUCTOS, null,  token);
      */
      
      /*******************PRUEBA DE LIST MESAS LIBRES ************************/
      /*
      System.out.println(CLIENTE+"Procediendo a hacer petición de add_empleado");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("JZaaSDkUut");        
      Restaurante rest = new Restaurante();
      rest.setId("res1TuKarta");
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_LIST_MESAS_LIBRES, rest,  token);            
      */
      
      
      /*******************PRUEBA DE GET EMPLEADO ************************/
      
      System.out.println(CLIENTE+"Procediendo a hacer petición de get_empleado_from_id");   
      TokenSesion token = new TokenSesion(emp);
      token.setToken("ZGvEEsWQzF");                    
      MensajeSolicitud mensajeOut = new MensajeSolicitud(Mensaje.FUNCION_GET_EMPLEADO_FROM_ID, null,  token);                        
      
      
      /*******************FINAL DE PETICIÓN ************************/
      String mensajeOutJson = gson.toJson(mensajeOut);         
      //convertimos el mensajeRes en JSON     
      System.out.println(CLIENTE+" enviando JSON\n  -->"+mensajeOutJson);
      out.println(mensajeOutJson);          
      out.flush();          
    }
    System.out.println(CLIENTE+"esperando al server..."); 
    String codigo="";
    do{                
      mensajeString = in.readLine();  
      System.out.println(CLIENTE+"Mensaje en formato Mensaje:");
      mensajeRes = gson.fromJson(mensajeString, MensajeRespuesta.class);
      System.out.println(mensajeRes);
      codigo = mensajeRes.getCode().getCode();
      tokenUser = gson.fromJson(mensajeRes.getData(), TokenSesion.class);
      System.out.println(CLIENTE+"SACANDO TOKEN: "+tokenUser.getToken());
      if (codigo.equals(Codes.END_CONNECTION)){
        System.out.println(CLIENTE+"Cerrando conexión con el servidor");				
        closeClient();
        break;
      }else {				
        System.out.println(CLIENTE+"enviando respuesta OK");
        sendCode(new Codes(Codes.CODIGO_OK));        
      }      
    }while (codigo!=Codes.END_CONNECTION);     
    
  }catch (Exception e) {   
  }
  
  
  
}

public void closeClient() throws IOException {

  cs.close();
}

public String getCodeFromJSON(String mensaje){
  JSONObject json = new JSONObject(mensaje);
  return json.getString("codigo");
}

public void sendCode(Codes codigo){  
  JSONObject jsonCode= codigo.parseCode();
  out.println(jsonCode);
  out.flush();  
}
}

