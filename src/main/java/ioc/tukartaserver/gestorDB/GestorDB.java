package ioc.tukartaserver.gestorDB;

/**
 *
 * @author Manu
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.Usuario;
import java.util.Date;



public class GestorDB {
private static final String CLASE = "org.postgresql.Driver";

private final String LOCAL_URL = "jdbc:postgresql://localhost:5432/TuKarta";    
private final String USER = "tukarta";
private final String PASS = "TuKartaP4$$"; 
private static final String TABLA_USERS = "usuario";
private static final String BD ="GESTOR BD: ";

private Connection con;

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
public MensajeRespuesta loginMens(String mail, String pass, boolean isGestor){
  //creamos los datos necesarios
  MensajeRespuesta mensajeRes = new MensajeRespuesta();
  Codes codigoRet=null;
  Usuario userRes =new Usuario();
  
  //preparamos la sentencia en función de si es gestor o no un requisito  
  String sentencia = "select * from  "+TABLA_USERS+" where email=\'"+mail+"\'";
  if (isGestor){
    sentencia+= "and isGestor='true'";
  }
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
        userRes.setApellido(result.getString("apellidos"));
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
    mensajeRes= new MensajeRespuesta (codigoRet, "login", null, userRes);    
    result.close();
    statement.close();
    System.out.println (BD+"conexión finalizada");
    
  } catch (Exception ex) {
    codigoRet = new Codes(Codes.CODIGO_ERR);
    System.out.println(BD+ex.getMessage());
    System.out.println (BD+"se devuelve el mensaje:\n"+mensajeRes);
  } 
  return mensajeRes;
}

/*
public Codes signIn(String mail, String pass, String userName, String realName, String realCognom) throws SQLException{
  Codes codigoRet =null;
  
  Statement statement = con.createStatement();
  //comprobamos que el usuario existe:
  String sentencia = "SELECT COUNT(email) FROM "+TABLA_USERS+" WHERE email='"+mail+"'";  
  System.out.println(BD+"SENTENCIA\n  --> "+sentencia);
  ResultSet result = statement.executeQuery(sentencia);
  result.next();
  if (result.getInt(1)!=0){   
    codigoRet = new Codes(Codes.CODIGO_USER_REP);    
  }else{
    //si no está repetido, procedemos a crear un usuario
    System.out.println(BD+"usuario no encontrado. Se procede a darle de alta");
    Date date = new Date();
    java.sql.Date sqlDate = convert(date);
    sentencia = "INSERT INTO "+TABLA_USERS+" VALUES ('"+userName+"', '"+pass+"', '"+mail+"', '"+realName+"', '"+realCognom+"', null, '"+sqlDate+"', '"+sqlDate+"')";
    System.out.println(BD+"SENTENCIA\n  --> "+sentencia);
    if (statement.executeUpdate(sentencia)==0){
      codigoRet = new Codes(Codes.CODIGO_ERR);      
    }else{
      codigoRet = new Codes(Codes.CODIGO_OK);
    }    
  }
  return codigoRet ;
}
*/
/*
public Codes modify(String email, String param, String value) throws SQLException{
  Codes codigoRet = null;
  Statement statement = con.createStatement();
  String sentencia = "UPDATE "+TABLA_USERS+" SET "+param+" = '"+value+"' WHERE "+Usuario.EMAIL+" = '"+email+"'";
  System.out.println(BD+"SENTENCIA\n  --> "+sentencia);
  if(statement.executeUpdate(sentencia)!=0){
    //cambiar la fecha de modificación
    java.sql.Date sqlDate = convert(new Date());
    sentencia = "UPDATE "+TABLA_USERS+" SET "+Usuario.FECHA_MOD+" = '"+sqlDate+"' WHERE "+Usuario.EMAIL+" = '"+email+"'";
    statement.executeUpdate(sentencia);
    codigoRet = new Codes(Codes.CODIGO_OK);
  }else{
    codigoRet = new Codes(Codes.CODIGO_ERR);
  }  
  return codigoRet;
}
*/

 private static java.sql.Date convert(java.util.Date uDate) {
   java.sql.Date sDate = new java.sql.Date(uDate.getTime());
   return sDate;
 }
}
