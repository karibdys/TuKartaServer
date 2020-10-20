package ioc.tukartaserver.model;

/**
 *
 * @author Manu
 */
import java.util.HashMap;
import org.json.JSONObject;

public class Codes {

public static final String KEY_CODE = "codigo";
public static final String KEY_MESS = "mensaje";
public static final String CODIGO_OK ="10";

public static final String CODIGO_DATOS_INCORRECTOS = "40";
public static final String CODIGO_FUNCION_ERR = "41";
public static final String CODIGO_NO_USER = "42";
public static final String CODIGO_NO_PASS = "43";
public static final String CODIGO_NO_SESION = "44";

public static final String CODIGO_ERR = "50";
public static final String CODIGO_ERR_USER ="51";
public static final String CODIGO_ERR_PWD ="52";
public static final String CODIGO_USER_REP ="53";

public static final String END_CONNECTION = "90";

private static final String CODES = "GESTOR DE CÓDIGOS: ";

private String code;
private String message;

public static HashMap<String, String> codigos;

public Codes (String code) {
  if (codigos==null) {
    initMap();
  }
  this.code=code;
  this.message = codigos.get(code);		
}

public static void initMap () {
  System.out.println(CODES+"Iniciando listado de códigos");
  codigos = new HashMap<String, String>();
  codigos.put(CODIGO_OK,  "Petición aceptada");
  codigos.put(CODIGO_DATOS_INCORRECTOS, "Los datos enviados no son correctos");
  codigos.put(CODIGO_FUNCION_ERR, "La función no está soportada por el sistema");
  codigos.put(CODIGO_NO_USER, "No se ha indicado un mail de usuario.");
  codigos.put(CODIGO_NO_PASS, "No se ha indicado una contraseña de usuario.");
  codigos.put(CODIGO_NO_SESION, "No hay sesión vinculada a la petición.");
  codigos.put(CODIGO_ERR,  "Error al procesar la petición");
  codigos.put(CODIGO_ERR_USER, "Usuario no encontrado");
  codigos.put(CODIGO_ERR_PWD, "Contraseña Incorrecta");
  codigos.put(CODIGO_USER_REP, "El usuario ya existe en la base de datos.");
  codigos.put(END_CONNECTION, "Finalizar conexión");
  System.out.println(CODES+"Códigos iniciados con éxito");
}

public String getCode() {
  return this.code;
}
public String getMessage() {
  return this.message;
}

public JSONObject parseCode(){
  JSONObject json= new JSONObject();
  json.put(KEY_CODE, code);
  json.put(KEY_MESS, message);  
  return json;
}

public String toString(){
  return "  Codigo: "+this.getCode()+" \n  Mensaje: "+this.getMessage();
}	
}

