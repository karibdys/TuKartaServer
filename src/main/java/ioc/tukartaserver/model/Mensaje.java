package ioc.tukartaserver.model;

/**
 * Interfaz con mensajes importantes para ser usados en las aplicaciones.
 * @author Manu Mora
 */
public interface  Mensaje {

public static final String FUNCION_LOGIN = "login";
public static final String FUNCION_LOGIN_ADMIN = "loginAdmin";
public static final String FUNCION_SIGNIN = "signIn";
public static final String FUNCION_ALTER = "modify";
public static final String FUNCION_LOGOFF = "logout";
public static final String FUNCION_FIN_CON = "fin_conexion";
public static final String FUNCION_FIN_SERVER = "cerrar_server";

public static final String ATT_PETICION = "peticion";
public static final String ATT_TOKEN = "token";
public static final String ATT_PARAM = "parametro";
public static final String ATT_VALOR = "valorParam";

public static final String PARAM_USER = "usuario";
public static final String PARAM_EMAIL = "email";
public static final String PARAM_PASS = "password";
public static final String PARAM_NOMBRE = "nombre";
public static final String PARAM_APELLIDO = "apellido";

}
