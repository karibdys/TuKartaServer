package ioc.tukartaserver.model;

import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Manu Mora
 */
public class Utiles {

public static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

public static Date ParseDate(String fecha){
  Date date = null;
  try {
    date = formato.parse(fecha);
  }
  catch (ParseException ex) 
  {
    System.out.println(ex);
  }
  return date;
}

public static String ParseDate(Date fecha){  
  return formato.format(fecha);
}


/***************
 * CONSTRUCTORES DE MENSAJES RÁPIDOS
***************/

 /**
  * Construyen un mensaje genérico de petición completada con éxito: CODIGO 10
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 10 y la petición a respondida. Necesita añadir los datos
  */
 public static MensajeRespuesta mensajeOK(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_OK), peticion);  
 }
 
  /**
  * construye un mensaje genérico de error de datos incorrectos: CÓDIGO 40
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 40 y la petición indicada como parámetro. 
  */
 public static MensajeRespuesta mensajeErrorDatosIncorrectos (String peticion){
    return new MensajeRespuesta(new Codes(Codes.CODIGO_DATOS_INCORRECTOS), peticion);  
 }
 
   /**
  * construye un mensaje genérico de error de función no soportada: CÓDIGO 41
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 41 y la petición indicada como parámetro. 
  */
 public static MensajeRespuesta mensajeErrorFuncionNoSoportada (String peticion){
    return new MensajeRespuesta(new Codes(Codes.CODIGO_FUNCION_ERR), peticion);  
 }
 
/**
  * Construyen un mensaje genérico de error de usuario no encontrado: CÓDIGO 42
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 42 y la petición a respondida
  */ 
 public static MensajeRespuesta mensajeErrorNoUser (String peticion){
    return new MensajeRespuesta(new Codes(Codes.CODIGO_NO_USER), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de error cuando la sesión no está en el listado de sesiones: CÓDIGO 44
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 44 y la petición a respondida
  */
 
  public static MensajeRespuesta mensajeNoSesion(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_NO_SESION), peticion);  
 }
  
  /**
  * Construyen un mensaje de error cuando el usuario que intenta acceder ha sido dado de baja: CÓDIGO 45
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 45 y la petición a respondida
  */
   public static MensajeRespuesta mensajeErrorBaja(String peticion) {
    return new MensajeRespuesta(new Codes(Codes.CODIGO_ERROR_USER_BAJA), peticion);  
  }

  /**
  * Construyen un mensaje genérico de error : CÓDIGO 50
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 50 y la petición a respondida
  */
 public static MensajeRespuesta mensajeError(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de error producido en la Base de Datos CÓDIGO 60
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 60 y la petición a respondida
  */
 public static MensajeRespuesta mensajeErrorDB(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR_BD), peticion);  
 }
  
 /**
  * Construyen un mensaje genérico de error producido en la Base de Datos al no coincidir la contraseña con el usuari introducido CÓDIGO 62
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 62 y la petición a respondida
  */
  public static MensajeRespuesta mensajeErrorPWD(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR_PWD), peticion);  
 }
  
  /**
  * Construyen un mensaje genérico de error producido en la Base de Datos al no coincidir la contraseña con el usuari introducido CÓDIGO 62
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 62 y la petición a respondida
  */
  public static MensajeRespuesta mensajeErrorUserRep(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERROR_DATA_REP), peticion);  
 }
 
  /**
  * Construyen un mensaje genérico de fin de conexión: CÓDIGO 90
  * @return MensajeRespuesta con el código 90 y la petición "fin de conexión"
  */
  public static MensajeRespuesta mensajeEndConnection(){
   return new MensajeRespuesta(new Codes(Codes.END_CONNECTION), "fin de conexion");  
 }
 
 /***********************
  * CONVERSORES A SQL
  ********************/

  public static String sentenciaEmpleadoToInsertSQL (Empleado user){
    StringBuilder builder = new StringBuilder();
    builder.append("INSERT INTO usuario (");
    //primero ponemos los campos obligatorios
    builder.append("\"usuario\", \"pwd\", \"email\", \"fecha_alta\", \"fecha_modificacion\"");
    //ahora comprobamos que los campos tienen datos para introducirlos
    if (user.getNombre()!=null){
      builder.append(", \"nombre\"");   
    }
    if (user.getApellidos()!=null){
      builder.append(", \"apellidos\"");   
    }    
    if (user.getFecha_baja()!=null){
      builder.append(", \"fecha_baja\"");      
    }
    if (user.getGestor() !=null){         
      builder.append(", \"gestor\"");
    }
    if (user.getTrabajadorDe()!=null){
      builder.append(", \"trabajadorde\"");
    }
    builder.append(",\"salario\"");
    if (user.getRol()!=null){
      builder.append(", \"rol\"");      
    }
    
    builder.append(") VALUES (");
    //hasta aquí construimos la primera parte de la sentencia.    
    //ahora toca meter los datos
    builder.append("'"+ user.getUsuario()+"'");
    builder.append(", '"+ user.getPwd()+"'");
    builder.append(", '"+ user.getEmail()+"'");
    builder.append(", '"+ convertDateJavaToSQL(user.getFecha_alta())+"'");
    builder.append(", '"+ convertDateJavaToSQL(user.getFecha_modificacion())+"'");
    //pasamos a comprobar los campos aleatorios:
    if(user.getNombre()!=null){
      builder.append(", '"+ user.getNombre()+"'");
    }
    if (user.getApellidos()!=null){
      builder.append(", '"+ user.getApellidos()+"'");
    }    
    if (user.getFecha_baja()!=null){
      builder.append(", '"+ convertDateJavaToSQL(user.getFecha_baja())+"'");
    }  
    if (user.getGestor() !=null){         
      builder.append(", '"+ user.getGestor().getEmail()+"'");
    }
    if (user.getTrabajadorDe()!=null){
      builder.append(", '"+ user.getTrabajadorDe().getId()+"'");
    }
    //salario
    builder.append(", "+ user.getSalario()+"");
    if (user.getRol()!=null){
      builder.append(",'"+ user.getRol().getNombreRol()+"'");
    }    
        
    builder.append(")");
    System.out.println("GESTOR BASE DATOS: sentencia\n--> "+builder.toString());
    return builder.toString();
  }
  
  public static String sentenciaUsuarioToInsertSQL (Usuario user){
    StringBuilder builder = new StringBuilder();
    builder.append("INSERT INTO usuario (");
    //primero ponemos los campos obligatorios
    builder.append("\"usuario\", \"pwd\", \"email\", \"fecha_alta\", \"fecha_modificacion\", \"isgestor\"");
    //ahora comprobamos que los campos tienen datos para introducirlos
    if (user.getNombre()!=null){
      builder.append(", \"nombre\"");   
    }
    if (user.getApellidos()!=null){
      builder.append(", \"apellidos\"");   
    }    
    if (user.getFecha_baja()!=null){
      builder.append(", \"fecha_baja\"");      
    }      
    builder.append(") VALUES (");
    //hasta aquí construimos la primera parte de la sentencia.    
    //ahora toca meter los datos
    builder.append("'"+ user.getUsuario()+"'");
    builder.append(", '"+ user.getPwd()+"'");
    builder.append(", '"+ user.getEmail()+"'");
    builder.append(", '"+ convertDateJavaToSQL(user.getFecha_alta())+"'");
    builder.append(", '"+ convertDateJavaToSQL(user.getFecha_modificacion())+"'");
    builder.append(", '"+user.getIsGestor()+"'");
    //pasamos a comprobar los campos aleatorios:
    if(user.getNombre()!=null){
      builder.append(", '"+ user.getNombre()+"'");
    }
    if (user.getApellidos()!=null){
      builder.append(", '"+ user.getApellidos()+"'");
    }    
    if (user.getFecha_baja()!=null){
      builder.append(", '"+ convertDateJavaToSQL(user.getFecha_baja())+"'");
    }     
    builder.append(")");
    System.out.println("GESTOR BASE DATOS: sentencia\n--> "+builder.toString());
    return builder.toString();
  }
 
  
  /**
 * Método para construir fechas conrrectas para insertar en la base de datos. 
 * @param uDate Date en formato Java
 * @return Date en formato SQL
 */
 public static java.sql.Date convertDateJavaToSQL (java.util.Date uDate) {
   java.sql.Date sDate = new java.sql.Date(uDate.getTime());
   return sDate;
 }

  /**
  * Método para construir fechas correctas en Java a partir de un date de SQL
  * @param uDate una fecha en formato SQL
  * @return Date con la fecha del SQL
  */
 public static java.util.Date convertDateSQLtoJava(java.sql.Date uDate) {
  java.util.Date javaDate = new java.util.Date(uDate.getTime());   
   return javaDate;
 }
 
  
}
