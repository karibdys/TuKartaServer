package ioc.tukartaserver.gestorDB;

/**
 * Clase que se encarga de gestionar la base de datos
 * @author Manu Mora
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
import ioc.tukartaserver.model.Utiles;
import java.sql.PreparedStatement;
import java.util.Date;


public class GestorDB {

//Datos de conexión
private static final String CLASE = "org.postgresql.Driver";
private final String LOCAL_URL = "jdbc:postgresql://localhost:5432/TuKarta";    
private final String USER = "tukarta";
private final String PASS = "TuKartaP4$$"; 
private Connection con;

//tablas
public static final String TABLA_USERS = "usuario";
public static final String TABLA_PROD = "producto";

//sentencias
public static final String SELECT_USER = "SELECT * FROM usuario WHERE email = ?";
public static final String BAJA_USER = "UPDATE usuario SET pwd = null, fecha_baja = ?, gestor =null, trabajadorde = null, salario = 0, rol = null WHERE email = ?";

//útiles para otros requisitos
private static final String BD ="GESTOR BD: ";

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
 * Devuelve el objeto Connection de este gestor
 * @return Connection conexión con la base de datos
 */
public Connection getCon(){
  return this.con;
}

public void setConnection(Connection con){
  this.con=con;
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
        codigoRet = new Codes(Codes.CODIGO_ERR_PK_NOT_FOUND);
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
      statement.close();
    } catch (Exception ex) {
      codigoRet = new Codes(Codes.CODIGO_ERR);
      System.out.println(BD+ex.getMessage());
      System.out.println (BD+"se devuelve el mensaje:\n"+mensajeRes);
    } 
  }    
  return mensajeRes;
}

/**
 * Procesa una petición de tipo select_data_user y devuelve un MensajeRespuesta con los datos del usuario cuyo email corresponde con el pasado como parámetro en caso de cumplirse los requisitos. 
 * @param email String email del Usuario
 * @return MensajeRespuesta con un código de respuesta y, en caso de ser positivo (código 10) los datos del usuario encontrado.
 */
public MensajeRespuesta selectDataUser(String email){
  MensajeRespuesta ret = null;
  String peticion = Mensaje.FUNCION_DATOS_USER;
  if (email==null){
    ret = Utiles.mensajeErrorDatosIncorrectos(peticion);
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
          ret = Utiles.mensajeOK(peticion);
          ret.setData(gestor);
        }else{
          System.out.println(BD+" usuario de tipo empleado encontrado");
          Empleado empleado = (Empleado) createUserFromResult(result, isGestor);
          ret = Utiles.mensajeOK(peticion);
          ret.setData(empleado);
        }       
      }else{
        System.out.println(BD+" usuario no encontrado");
        ret = Utiles.mensajeErrorPKNotFound(peticion);
      }      
    }catch(SQLException ex){
      ret = Utiles.mensajeErrorDB(peticion);
    }   
  }  
  return ret;
}

/**
 * Permite añadir un objeto de cualquier tipo a la base de datos. El propio método se encarga de verificar que los objetos que se les pasan como parámetros son registros válidos en la Base de datos
 * @param dato un objeto que la base de datos acepte como, por ejemplo, un Usuario, Empleado, Producto o Pedido
 * @param peticion Nombre de la petición que hace el cliente al servidor como, por ejemplo, add_empl o add_gestor
 * @return MensajeRespuesta con el código de confirmación o de error del proceso
 */
public MensajeRespuesta addData(Object dato, String peticion){
  MensajeRespuesta ret = null;
  String sentencia = "";
  //primero tenemos que saber qué tipo de dato viene asociado
  if (dato instanceof Usuario){
    //tipo empleado
    sentencia = Utiles.sentenciaUsuarioToInsertSQL((Empleado)dato);   
    System.out.println(BD+"sentencia: "+sentencia); 
  }  
  //si la sentencia creada no está vacía, procedemos a hacer la petición
  if(!sentencia.equals("")){
    try {
      //si el dato es de tipo usuario, habrá que hacer un insert a la tabla Usuario
      Statement statement = con.createStatement();
      if(statement.executeUpdate(sentencia)==1){
        System.out.println(BD+" dato insertado correctamente");
        //si todo ha ido bien (se ha insertado una sentencia) entonces mandamos código OK
        ret = Utiles.mensajeOK(peticion);
      }else{
        //si no ha ido bien se manda un error de BD
        ret = Utiles.mensajeErrorDB(peticion);
        System.out.println(BD+" dato no insertado correctamente");
      };
      statement.close();
    } catch (SQLException ex) {
      //si tenemos una excepción en la base de datos específica, intentamos sacarla
      String sqlstate = ex.getSQLState();
      if(sqlstate.startsWith("230")){
        //si el error es que la clase está duplicada, mandamos ese mensaje
        ret = Utiles.mensajeErrorUserRep(peticion);
      }else{
        //si el error es otro, entonces mandamos un error genérico
        ret = Utiles.mensajeError(peticion);
        System.out.println(ex.getMessage());
      }
    }      
  }else {
    //si llegamos aquí es que no se ha formado bien la sentencia porque no es un tipo de dato soportado (aún)
    ret = Utiles.mensajeErrorFuncionNoSoportada(peticion);
  }
  return ret;
}

/**
 * Permite establecer los parámetros de un registro de Usuarios como "dado de baja", lo que implica que su pass, gestor, restaurante y otros parámetros serán null y su salario será "0".
 * @param email El email del Usuario a dar de baja de la base de datos
 * @return MensajeRespuesta con el código de confirmación o error del proceso
 */
public MensajeRespuesta bajaUser(String email){
  MensajeRespuesta ret = null;
  //comprobamos que el email es válido
  if (email==null){
    ret = Utiles.mensajeErrorDatosIncorrectos(Mensaje.FUNCION_BAJA_USER);
  }else{   
    String peticion = Mensaje.FUNCION_BAJA_USER;
    //para dar de baja necesito la fecha de hoy
    java.sql.Date fecha_baja = Utiles.convertDateJavaToSQL(new Date());    
    PreparedStatement stm;
    try {
      stm = con.prepareStatement(BAJA_USER);
      stm.setDate(1, fecha_baja);
      stm.setString(2, email);
      System.out.println(BD+" sentencia --> "+stm);
      int rows = stm.executeUpdate();
      if (rows ==1){
        ret = Utiles.mensajeOK(peticion);
      }else if (rows==0){
        ret = Utiles.mensajeErrorNoUser(peticion);      
      }else{
        throw new SQLException();
      }
    } catch (SQLException ex) {            
        ret = Utiles.mensajeErrorDB(peticion);
    }      
  }    
  return ret;      
}

/**
 * Permite actualizar un registro de una tabla. Necesita, como mínimo, el campo Primary Key de dicho objeto. 
 * @param dato un objeto que pueda aceptar la base de datos como registro
 * @param peticion petición específica que hace el cliente al Servidor
 * @return MensajeRespuesta con el código de confirmación o error del proceso. 
 */
public MensajeRespuesta updateData(Object dato, String peticion){
  MensajeRespuesta ret = null;
  String sentencia = "";
  //primero tenemos que saber qué tipo de dato viene asociado
  if (dato instanceof Empleado){
    //tipo empleado
    sentencia = Utiles.sentenciaUsuarioToUpdateSQL((Empleado)dato);   
    System.out.println(BD+"sentencia: "+sentencia);
  }else if (dato instanceof Usuario){
    //tipo gestor/usuario
    sentencia = Utiles.sentenciaUsuarioToInsertSQL((Usuario)dato);   
  }  
  if(!sentencia.equals("")){
    try {
      //si el dato es de tipo usuario, habrá que hacer un insert a la tabla Usuario
      Statement statement = con.createStatement();
      if(statement.executeUpdate(sentencia)!=0){
        System.out.println(BD+" dato actualizado con éxito");
        //si todo ha ido bien (se ha insertado una sentencia) entonces mandamos código OK
        ret = Utiles.mensajeOK(peticion);
      }else{
        //si no ha ido bien se manda un error de BD
        ret = Utiles.mensajeErrorDB(peticion);
        System.out.println(BD+" dato no actualizado correctamente");
      };
      statement.close();
    } catch (SQLException ex) {
      //si tenemos una excepción en la base de datos específica, intentamos sacarla
      ret = Utiles.mensajeErrorDB(peticion);
      System.out.println(ex.getMessage());
      
    }      
  }else {
    //si llegamos aquí es que no se ha formado bien la sentencia porque no es un tipo de dato soportado (aún)
    ret = Utiles.mensajeErrorFuncionNoSoportada(peticion);
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
  * Crea un usuario a partir de un ResultSet
  * @param result ResultSet que contiene el dato del Usuario
  * @param isGestor boolean que indica si el usuario es de tipo gestor o empleado
  * @return Usuario que puede ser Empleado o Gestor
  * @throws SQLException Al acceder a la base de datos
  */
 public static Usuario createUserFromResult(ResultSet result, Boolean isGestor) throws SQLException{
   Usuario user = new Usuario();   
   if (user ==null){
     //si el usuario es nulo, devolvemos un nulo
     return user = null;
   }else{
     //si no es nulo, empezamos a crear el usuario a partir de los datos del ResultSet:
     user.setUsuario(result.getString("usuario"));
     user.setEmail(result.getString("email"));
     user.setNombre(result.getString("nombre"));
     user.setApellidos(result.getString("apellidos"));
     System.out.println("FECHA: "+Utiles.convertDateSQLtoJava(result.getDate("fecha_alta")));
     user.setFecha_alta(Utiles.convertDateSQLtoJava(result.getDate("fecha_alta")));
     user.setFecha_modificacion(Utiles.convertDateSQLtoJava(result.getDate("fecha_modificacion")));
     if (result.getDate("fecha_baja")!=null){
       user.setFecha_baja(Utiles.convertDateSQLtoJava(result.getDate("fecha_baja")));
     }
     //si el usuario es de tipo gestor, creamos un gestor
     if (isGestor){          
       user.setIsGestor(true);  
       System.out.println("El usuario es Empleado");
       //devolvemos el usuario (gestor)
       return user;
     //si el usuario es de tipo Empleado, creamos un Empleado
     }else{
       Empleado empleado = new Empleado(user);     
       System.out.println(empleado);
       if (result.getString("salario")!=null) {
         empleado.setSalario(result.getFloat("salario"));                  
       }       
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
 }
 
 
 public void closeConnection(){
   try{ 
     if(con!=null && !con.isClosed()){
       con.close();
     }
   }catch (SQLException e){
    System.out.println(e.getMessage());
   }
 }
 
}
