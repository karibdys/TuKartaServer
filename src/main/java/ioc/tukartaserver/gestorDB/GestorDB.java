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
import ioc.tukartaserver.model.Usuario;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GestorDB {
private static final String clase = "org.postgresql.Driver";

private final String LOCAL_URL = "jdbc:postgresql://localhost:5432/TuKarta";    
//private final String NGROK_DIR ="2.tcp.ngrok.io:18135";
//private String NGROK_URL = "jdbc:postgresql://"+NGROK_DIR+"/TuKarta"; 
//private String urlAmazamon ="jdbc:postgresql://tukarta.ciq1mt081nsj.eu-west-3.rds.amazonaws.com:5432/tukarta";
private String user = "tukarta";
private String pass = "TuKartaP4$$"; 
private static final String TABLA_USERS = "usuario";
private static final String BD ="GESTOR BD: ";

private Connection con;


public GestorDB() throws SQLException, ClassNotFoundException {
    Class.forName(clase);    
    //conectar en local    
    con = DriverManager.getConnection(LOCAL_URL,user,pass);    
    //conectar en remoto
    //con = DriverManager.getConnection(NGROK_URL,user,pass);    
    System.out.println(BD+"Conexión establecida");  
}

public Codes login (String mail, String pass, boolean isGestor) {
   Codes ret=null;
   String sentencia = "select * from  "+TABLA_USERS+" where email=\'"+mail+"\'";
   if (isGestor){
     sentencia+= "and isGestor='true'";
   }
   System.out.println(BD+" SENTENCIA\n  --> "+sentencia);
  try {              
    Statement statement = con.createStatement();    
    ResultSet result = statement.executeQuery(sentencia);
    if (result.next()) {
      System.out.println(BD+"Usuario encontrado");
      if(pass.equals(result.getString("pwd"))) {
        ret= new Codes(Codes.CODIGO_OK);				 
        System.out.println(BD+"Contraseña correcta");
      }else {
        System.out.println(BD+"Contraseña incorrecta");
        ret = new Codes(Codes.CODIGO_ERR_PWD);
      }
    }else {
      System.out.println(BD+"El resultset es nulo");
      ret = new Codes(Codes.CODIGO_ERR_USER);
    }
    System.out.println (BD+"se devuelve el código:\n"+ret);
    
    result.close();
    statement.close();
    System.out.println (BD+"conexión finalizada");
    
  } catch (Exception ex) {
    ret = new Codes(Codes.CODIGO_ERR);
    System.out.println(BD+ex.getMessage());
  } 
  return ret;
}

/*
public Codes signIn(String mail, String pass, String userName, String realName, String realCognom) throws SQLException{
  Codes ret =null;
  
  Statement statement = con.createStatement();
  //comprobamos que el usuario existe:
  String sentencia = "SELECT COUNT(email) FROM "+TABLA_USERS+" WHERE email='"+mail+"'";  
  System.out.println(BD+"SENTENCIA\n  --> "+sentencia);
  ResultSet result = statement.executeQuery(sentencia);
  result.next();
  if (result.getInt(1)!=0){   
    ret = new Codes(Codes.CODIGO_USER_REP);    
  }else{
    //si no está repetido, procedemos a crear un usuario
    System.out.println(BD+"usuario no encontrado. Se procede a darle de alta");
    Date date = new Date();
    java.sql.Date sqlDate = convert(date);
    sentencia = "INSERT INTO "+TABLA_USERS+" VALUES ('"+userName+"', '"+pass+"', '"+mail+"', '"+realName+"', '"+realCognom+"', null, '"+sqlDate+"', '"+sqlDate+"')";
    System.out.println(BD+"SENTENCIA\n  --> "+sentencia);
    if (statement.executeUpdate(sentencia)==0){
      ret = new Codes(Codes.CODIGO_ERR);      
    }else{
      ret = new Codes(Codes.CODIGO_OK);
    }    
  }
  return ret ;
}
*/
/*
public Codes modify(String email, String param, String value) throws SQLException{
  Codes ret = null;
  Statement statement = con.createStatement();
  String sentencia = "UPDATE "+TABLA_USERS+" SET "+param+" = '"+value+"' WHERE "+Usuario.EMAIL+" = '"+email+"'";
  System.out.println(BD+"SENTENCIA\n  --> "+sentencia);
  if(statement.executeUpdate(sentencia)!=0){
    //cambiar la fecha de modificación
    java.sql.Date sqlDate = convert(new Date());
    sentencia = "UPDATE "+TABLA_USERS+" SET "+Usuario.FECHA_MOD+" = '"+sqlDate+"' WHERE "+Usuario.EMAIL+" = '"+email+"'";
    statement.executeUpdate(sentencia);
    ret = new Codes(Codes.CODIGO_OK);
  }else{
    ret = new Codes(Codes.CODIGO_ERR);
  }  
  return ret;
}
*/

 private static java.sql.Date convert(java.util.Date uDate) {
   java.sql.Date sDate = new java.sql.Date(uDate.getTime());
   return sDate;
 }
}
