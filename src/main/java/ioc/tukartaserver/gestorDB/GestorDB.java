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
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Gestor;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.Rol;
import ioc.tukartaserver.model.Usuario;
import java.sql.PreparedStatement;


public class GestorDB {

//Datos de conexión
private static final String CLASE = "org.postgresql.Driver";
private final String LOCAL_URL = "jdbc:postgresql://localhost:5432/TuKarta";    
private final String USER = "tukarta";
private final String PASS = "TuKartaP4$$"; 

//tablas
public static final String TABLA_USERS = "usuario";

//sentencias
public static final String SELECT_USER = "SELECT * FROM usuario WHERE email = ?";
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


public Connection getCon(){
  return this.con;
}

/******************
 * MÉTODOS DE PETICIONES
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
 * Devuelve los datos del usuario cuyo email corresponde con el pasado como parámetro.
 * @param email String email del Usuario
 * @return MensajeRespuesta con un código de respuesta y, en caso de ser positivo (código 10) los datos del usuario encontrado.
 */
public MensajeRespuesta selectDataUser(String email){
  MensajeRespuesta ret = null;
  String peticion = Mensaje.FUNCION_DATOS_USER;
  if (email==null){
    ret = mensajeErrorDatosIncorrectos(peticion);
  }else{
    //si son todos correctos, entonces empezamos el proceso
    try{
      PreparedStatement stm = con.prepareStatement(SELECT_USER);
      stm.setString(1, email);
      System.out.println(BD+" sentencia --> "+stm);
      ResultSet result = stm.executeQuery();
      if(result.next()){        
        boolean isGestor = result.getBoolean("isgestor");       
        if (isGestor){
          System.out.println(BD+" usuario de tipo gestor encontrado");
          Gestor gestor = new Gestor(createUserFromResult(result, isGestor));
          ret = mensajeOK(peticion);
          ret.setData(gestor);
        }else{
          System.out.println(BD+" usuario de tipo empleado encontrado");
          Empleado empleado = (Empleado) createUserFromResult(result, isGestor);
          ret = mensajeOK(peticion);
          ret.setData(empleado);
        }       
      }else{
        System.out.println(BD+" usuario no encontrado");
        ret = mensajeErrorNoUser(peticion);
      }      
    }catch(SQLException ex){
      ret = mensajeErrorDB(peticion);
    }   
  }  
  return ret;
}

/******************
 * MÉTODOS AUXILIARES
 ******************
 */

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
 private static java.sql.Date convertDateJavaToSQL (java.util.Date uDate) {
   java.sql.Date sDate = new java.sql.Date(uDate.getTime());
   return sDate;
 }

  /**
  * Método para construir fechas correctas en Java a partir de un date de SQL
  * @param uDate una fecha en formato SQL
  * @return Date con la fecha del SQL
  */
 private static java.util.Date convertDateSQLtoJava(java.sql.Date uDate) {
  java.util.Date javaDate = new java.util.Date(uDate.getTime());   
   return javaDate;
 }
 
 /**
  * Crea un usuario a partir de un ResultSet
  * @param result ResultSet que contiene el dato del Usuario
  * @param isGestor boolean que indica si el usuario es de tipo gestor o empleado
  * @return Usuario que puede ser Empleado o Gestor
  * @throws SQLException Al acceder a la base de datos
  */
 public static Usuario createUserFromResult(ResultSet result, Boolean isGestor) throws SQLException{
   Usuario user = new Usuario();   
   user.setUsuario(result.getString("usuario"));
   user.setEmail(result.getString("email"));
   user.setNombre(result.getString("nombre"));
   user.setApellidos(result.getString("apellidos"));
   user.setFecha_alta(convertDateSQLtoJava(result.getDate("fecha_alta")));
   user.setFecha_modificacion(convertDateSQLtoJava(result.getDate("fecha_modificacion")));
   if (result.getDate("fecha_baja")!=null){
     user.setFecha_baja(convertDateSQLtoJava(result.getDate("fecha_baja")));
   }
   //si el usuario es de tipo gestor, creamos un gestor
   if (isGestor){          
     user.setIsGestor(true);  
     return user;
   }else{
     Empleado empleado = new Empleado(user);          
     empleado.setSalario(result.getFloat("salario"));     
     if (result.getString("rol")!=null){
       String rol = result.getString("rol");
       switch (rol){
         case ("camarero"):
           empleado.setRol(Rol.CAMARERO);
           break;                 
         case ("cocinero"):
           empleado.setRol(Rol.COCINERO);
         default:           
           break;           
       } 
     }    
     return empleado;
   }    
   
 }
 
/******************
 * CONSTRUCTORES DE MENSAJES DE RESPUESTA
 ******************
 */

 /**
  * construye un mensaje genérico de error de datos incorrectos
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 40 y la petición indicada como parámetro. 
  */
 private static MensajeRespuesta mensajeErrorDatosIncorrectos (String peticion){
    return new MensajeRespuesta(new Codes(Codes.CODIGO_DATOS_INCORRECTOS), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de error de usuario no encontrado
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 42 y la petición a respondida
  */ 
 private static MensajeRespuesta mensajeErrorNoUser (String peticion){
    return new MensajeRespuesta(new Codes(Codes.CODIGO_NO_USER), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de error producido en la Base de Datos
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 60 y la petición a respondida
  */
 private static MensajeRespuesta mensajeErrorDB(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR_BD), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de petición completada con éxito
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 10 y la petición a respondida. Necesita añadir los datos
  */
 private static MensajeRespuesta mensajeOK(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_OK), peticion);  
 }
}
