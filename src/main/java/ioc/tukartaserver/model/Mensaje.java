package ioc.tukartaserver.model;

/**
 * Interfaz con mensajes importantes para ser usados en las aplicaciones.
 * @author Manu Mora
 */
public interface  Mensaje {

public static final int MENSAJE_RESPUESTA =1;
public static final int MENSAJE_SOLICITUD =2;

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
public static final String FUNCION_GET_EMPLEADO_FROM_ID = "get_empleado_from_id";

//gestión de Pedidos
public static final String FUNCION_ADD_PEDIDO = "add_pedido";
public static final String FUNCION_LIST_PEDIDO_FROM_USER = "list_pedido_from_user";
public static final String FUNCION_LIST_PRODUCTOS_FROM_PEDIDO_ID = "list_producto_estado_from_pedido";
public static final String FUNCION_LIST_PEDIDO_FROM_OTHER_USER = "list_pedido_from_other_user";
public static final String FUNCION_LIST_PEDIDO_COMPLETO_FROM_ID = "list_pedido_completo_from_id";
public static final String FUNCION_LIST_PEDIDO_COMPLETO_FROM_USER = "list_pedido_completo_from_user";
public static final String FUNCION_DELETE_PEDIDO = "delete_pedido";
public static final String FUNCION_UPDATE_PEDIDO = "update_pedido";

//gestión de productos en pedidos
public static final String FUNCION_ADD_PRODUCTO_TO = "add_producto_to";
public static final String FUNCION_LIST_PRODUCTOS_FROM_PEDIDO = "list_productos_from_pedido";
public static final String FUNCION_DELETE_PRODUCTO_FROM ="delete_producto_from";
public static final String FUNCION_DELETE_PRODUCTO_FROM_ID ="delete_producto_from_id";
public static final String FUNCION_UPDATE_PRODUCTO_FROM = "update_producto_from";
public static final String FUNCION_UPDATE_PRODUCTO_FROM_ID = "update_producto_from_id";
public static final String FUNCION_LIST_PRODUCTOS_PENDIENTES ="list_productos_pendientes";
public static final String FUNCION_LIST_PEDIDO_FROM_ID ="list_pedido_from_id";

//gestión de restaurantes
public static final String FUNCION_ADD_RESTAURANTE = "add_restaurante";
//public static final String FUNCION_DELETE_RESTAURANTE = "delete_restaurante";
//public static final String FUNCION_LIST_RESTAURANTES = "list_restaurantes";
//public static final String FUNCIÓN_UPDATE_RESTAURANTE = "upsate_restaurante";

//gestión de mesas
public static final String FUNCION_ADD_MESA = "add_mesa";
public static final String FUNCION_LIST_MESAS_LIBRES = "list_mesas_libres";

//gestión de productos
public static final String FUNCION_ADD_PRODUCTO = "add_producto";
public static final String FUNCION_DELETE_PRODUCTO = "delete_producto";
public static final String FUNCION_LIST_PRODUCTOS = "list_productos";

//informed
public static final String FUNCION_INFORME_VENTAS = "get_informe_ventas";

//gestión de la conexión
public static final String FUNCION_FIN_CON = "fin_conexion";

//otros
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
