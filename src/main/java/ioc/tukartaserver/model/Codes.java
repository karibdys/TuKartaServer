package ioc.tukartaserver.model;

/**
 * Clase que se encarga de gestionar los códigos que genera el servidor como respuesta a las peticiones de las aplicaciones cliente. 
 * @author Manu Mora
 */
import java.util.HashMap;
import org.json.JSONObject;

public class Codes {

//constantes necesarias
public static final String KEY_CODE = "codigo";
public static final String KEY_MESS = "mensaje";
//petición completada con éxito
public static final String CODIGO_OK ="10";
//errores de input
public static final String CODIGO_DATOS_INCORRECTOS = "40";
public static final String CODIGO_FUNCION_ERR = "41";
public static final String CODIGO_NO_USER = "42";
public static final String CODIGO_NO_PASS = "43";
public static final String CODIGO_NO_SESION = "44";
public static final String CODIGO_ERROR_USER_BAJA = "45";

//error genérico
public static final String CODIGO_ERR = "50";
//error en la base de datos
public static final String CODIGO_ERR_BD ="60";
public static final String CODIGO_ERR_USER ="61";
public static final String CODIGO_ERR_PWD ="62";
public static final String CODIGO_USER_REP ="63";
//fin de conexión
public static final String END_CONNECTION = "90";
public static final String END_SERVER = "91";

private static final String CODES = "GESTOR DE CÓDIGOS: ";

private String code;
private String message;

public static HashMap<String, String> codigos;

/**
 * Constructor básico. Genera un objeto Codes a partir de un String indicando el código a enviar.
 * @param code String que indica el código a enviar.
 */
public Codes (String code) {
  if (codigos==null) {
    initMap();
  }
  this.code=code;
  this.message = codigos.get(code);		
}

/**
 * Genera un HashMap con la combinación código:mensaje completa. Solo se inicia una vez al iniciarse el servidor. 
 * Permite crear los códigos de una forma más fácil. 
 */
public static void initMap () {
  System.out.println(CODES+"Iniciando listado de códigos");
  codigos = new HashMap<String, String>();
  codigos.put(CODIGO_OK,  "Petición completada con éxito");
  //errores de input
  codigos.put(CODIGO_DATOS_INCORRECTOS, "Los datos enviados no son correctos o están incompletos");
  codigos.put(CODIGO_FUNCION_ERR, "La petición solicitada no está soportada por el sistema");
  codigos.put(CODIGO_NO_USER, "No se ha indicado un mail de usuario.");
  codigos.put(CODIGO_NO_PASS, "No se ha indicado una contraseña de usuario.");  
  codigos.put(CODIGO_NO_SESION, "No hay sesión vinculada a la petición.");
  codigos.put(CODIGO_ERROR_USER_BAJA, "El usuario ha sido dado de baja en la base de datos");
  //error genérico
  codigos.put(CODIGO_ERR,  "Error al procesar la petición");
  //error en la base de datos
  codigos.put(CODIGO_ERR_BD, "Error al acceder a la base de datos.");
  codigos.put(CODIGO_ERR_USER, "Usuario no encontrado");
  codigos.put(CODIGO_ERR_PWD, "Contraseña Incorrecta");
  codigos.put(CODIGO_USER_REP, "El usuario ya existe en la base de datos.");
  //fin de conexión
  codigos.put(END_CONNECTION, "Finalizar conexión");
  codigos.put(END_SERVER, "Finalizar el servidor");
  System.out.println(CODES+"Códigos iniciados con éxito");
}

/**
 * devuelve el código que se corresponde a este objeto
 * @return String: código del mensaje
 */
public String getCode() {
  return this.code;
}
/**
 * devuelve el mensaje que se corresponde a este objeto
 * @return String: mensaje que detalla el error o éxito de la operación
 */
public String getMessage() {
  return this.message;
}

/**
 * Genera un String que permite ver el código y el mensaje de una forma sencilla y clara
 * @return String: pequeño esquema con el código y el mensaje de este objeto
 */
public String toString(){
  return "  Codigo: "+this.getCode()+" \n  Mensaje: "+this.getMessage();
}	

//MÉTODO USADO SOLO PARA PRUEBAS, ES TEMPORAL
public JSONObject parseCode(){
  JSONObject json= new JSONObject();
  json.put(KEY_CODE, code);
  json.put(KEY_MESS, message);  
  return json;
}
}

