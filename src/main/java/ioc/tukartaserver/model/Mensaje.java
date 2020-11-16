package ioc.tukartaserver.model;

/**
 * Interfaz con mensajes importantes para ser usados en las aplicaciones.
 * @author Manu Mora
 */
public interface  Mensaje {

//gestión de Usuarios
public static final String FUNCION_LOGIN = "login";
public static final String FUNCION_LOGIN_ADMIN = "loginAdmin";
public static final String FUNCION_SIGNIN = "signIn";
public static final String FUNCION_ALTER = "modify";
public static final String FUNCION_LOGOFF = "logout";
public static final String FUNCION_FIN_SERVER = "cerrar_server";
public static final String FUNCION_DATOS_USER = "get_datos_user";
public static final String FUNCION_DATOS_OTRO_USER = "get_data_other_user";
public static final String FUNCION_ADD_EMP = "add_empl";
public static final String FUNCION_ADD_GESTOR = "add_gestor";
public static final String FUNCION_BAJA_USER = "baja_user";
public static final String FUNCION_UPDATE_EMP ="update_user_empleado";
public static final String FUNCION_UPDATE_GESTOR ="update_user_gestor";
public static final String FUNCION_LIST_USERS_FROM_GESTOR = "list_users_from_gestor";
public static final String FUNCION_LIST_USERS_FROM_REST = "list_users_from_restaurant";

//gestión de Pedidos
public static final String FUNCION_ADD_PEDIDO = "add_pedido";

//gestión de la conexión
public static final String FUNCION_FIN_CON = "fin_conexion";

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
