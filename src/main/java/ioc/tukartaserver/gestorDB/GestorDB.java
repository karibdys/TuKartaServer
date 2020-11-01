package ioc.tukartaserver.gestorDB;

/**
 * Clase que se encarga de gestionar la base de datos
 * @author Manu
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.Usuario;
import java.util.Date;

public class GestorDB {

//Datos de conexión
private static final String CLASE = "org.postgresql.Driver";
private final String LOCAL_URL = "jdbc:postgresql://localhost:5432/TuKarta";    
private final String USER = "tukarta";
private final String PASS = "TuKartaP4$$"; 
private static final String TABLA_USERS = "usuario";
private static final String BD ="GESTOR BD: ";

private Connection con;

/******************
 * CONSTRUCTOR
 ******************
 */

/**
 * Constructor básico del gestor de la base de datos
 * @throws SQLException en caso de no acceder a la conexión por datos incorrectos
 * @throws ClassNotFoundException si no se ha encontrado la clase que gestiona el driver
 */
public GestorDB() throws SQLException, ClassNotFoundException {
    Class.forName(CLASE);    
    con = DriverManager.getConnection(LOCAL_URL,USER,PASS);     
    System.out.println(BD+"Conexión establecida");  
}

/**
 * Procesa una petición de login del servidor a la base de datos
 * @param mail  email del usuario
 * @param pass  contraseá del usuario
 * @param isGestor  indica si el usuario debe de constar como gestor o no de la aplicación
 * @return MensajeRespuesta que contiene el código que se ha generado y los datos que se le han pedido, tanto si la petición ha tenido éxito como si no. 
 */


/******************
 * MÉTODOS AUXILIARES
 ******************
 */

/**
 * Procesa una petición de login y devuelve un MensajeRespuesta con los datos del usuario en caso de ser correcta. 
 * @param mail String con el email del usuario
 * @param pass String con la contraseña del usuario
 * @param isGestor boolean que indica qué tipo de login se le pide al método (gestor o empleado)
 * @return MensajeRespuesta con 
 */
public MensajeRespuesta loginMens(String mail, String pass, boolean isGestor){
  //creamos los datos necesarios
  MensajeRespuesta mensajeRes = new MensajeRespuesta();
  Codes codigoRet=null;
  Usuario userRes =new Usuario();
  
  //comprobar que los datos son buenos
  if (mail==null || pass==null){
    //devolver un mensaje de error
    mensajeRes.setCode(new Codes (Codes.CODIGO_DATOS_INCORRECTOS));
    if (isGestor){
      mensajeRes.setPeticion(Mensaje.FUNCION_LOGIN_ADMIN);
    }else{
      mensajeRes.setPeticion(Mensaje.FUNCION_LOGIN);
    }    
  }else{
    //preparamos la sentencia en función de si es gestor o no un requisito  
    String sentencia = constructorSentenciaLogin(mail, isGestor);
    System.out.println(BD+" SENTENCIA\n  --> "+sentencia);
    
    //hacemos la petición a la base de datos
    try {              
      Statement statement = con.createStatement();    
      ResultSet result = statement.executeQuery(sentencia);
      if (result.next()) {
        System.out.println(BD+"Usuario encontrado");
        if(pass.equals(result.getString("pwd"))) {
          codigoRet= new Codes(Codes.CODIGO_OK);				 
          System.out.println(BD+"Contraseña correcta");

          //pasamos a procesar el usuario
          userRes.setUsuario(result.getString("usuario"));
          userRes.setNombre(result.getString("nombre"));
          userRes.setApellidos(result.getString("apellidos"));
          userRes.setEmail(result.getString("email"));
          userRes.setIsGestor(result.getBoolean("isGestor"));
        }else {
          System.out.println(BD+"Contraseña incorrecta");
          codigoRet = new Codes(Codes.CODIGO_ERR_PWD);
        }
      }else {
        System.out.println(BD+"El resultset es nulo");
        codigoRet = new Codes(Codes.CODIGO_ERR_USER);
      }

      //preparamos el mensaje con los datos
      mensajeRes= new MensajeRespuesta (codigoRet, null, null, userRes);    
      if (isGestor){
        mensajeRes.setPeticion(Mensaje.FUNCION_LOGIN_ADMIN);
      }else{
        mensajeRes.setPeticion(Mensaje.FUNCION_LOGIN);
        
      }
      result.close();
      statement.close();
      System.out.println (BD+"conexión finalizada");

    } catch (Exception ex) {
      codigoRet = new Codes(Codes.CODIGO_ERR);
      System.out.println(BD+ex.getMessage());
      System.out.println (BD+"se devuelve el mensaje:\n"+mensajeRes);
    } 
  }    
  return mensajeRes;
}

/**
 * Método para construir sentencias de tipo Login
 * @param mail  email del usuario 
 * @param isGestor  indicador de si el usuario quiere hacer login de gestión o no
 * @return 
 */
public static String constructorSentenciaLogin(String mail, boolean isGestor){
  String ret = "select * from "+TABLA_USERS+" where email=\'"+mail+"\'";
  if (isGestor){
    ret+= " and isgestor='true'";
  }    
  return ret;
}

/**
 * Método para construir fechas conrrectas para insertar en la base de datos. 
 * @param uDate Date en formato Java
 * @return Date en formato SQL
 */
 private static java.sql.Date convert(java.util.Date uDate) {
   java.sql.Date sDate = new java.sql.Date(uDate.getTime());
   return sDate;
 }
}
