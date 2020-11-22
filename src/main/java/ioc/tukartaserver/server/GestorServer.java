package ioc.tukartaserver.server;

import com.google.gson.Gson;
import ioc.tukartaserver.gestorDB.GestorDB;
import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Estado;
import ioc.tukartaserver.model.Gestor;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.Mesa;
import ioc.tukartaserver.model.Pedido;
import ioc.tukartaserver.model.Producto;
import ioc.tukartaserver.model.Restaurante;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.model.Utiles;
import ioc.tukartaserver.security.GestorSesion;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.sql.SQLException;

/**
 * Clase que gestiona la recepción de los mensajes de petición del cliente y prepara su devolución. 
 * @author Manu Mora
 */
public class GestorServer {

private static final String SERVER ="GESTOR SERVER: ";

private PrintStream out;
private BufferedReader in;
private GestorDB gestorDB;
private GestorSesion gestorSesion;
private Codes code = null;
private Gson gson;

private MensajeRespuesta respuesta;

/******************
 * CONSTRUCTOR  
 ******************
 */

/**
 * Único constructor de la clase GestorServer. Necessita los canales de entrada y de salida para poder funcionar. 
 * @param in BufferedReader que gestiona la entrada de mensajes en el Socket del servidor desde el cliente
 * @param out PrintStream que gestiona la salida de mensajes del Socket del servidor hacia el cliente
 */
public GestorServer(BufferedReader in, PrintStream out, GestorSesion gestorsesion){
  this.in=in;
  this.out=out;
  gestorSesion=gestorsesion;
  try{
    gestorDB = new GestorDB();
  } catch (SQLException ex) {
    respuesta = new MensajeRespuesta(new Codes(Codes.CODIGO_ERR_BD), "conexión");
    sendMensaje(respuesta);
    System.out.println("GESTOR DB: ERROR AL CONECTAR CON LA BASE DE DATOS");
  } catch (ClassNotFoundException ex) {
    respuesta = new MensajeRespuesta(new Codes(Codes.CODIGO_ERR), "conexión");
    sendMensaje(respuesta);
    System.out.println(ex.getMessage());
  }
  gson = new Gson();
  if(in==null || out==null){
    System.out.println(SERVER+"LOS CANALES NO SE HAN ABIERTO CORRECTAMENTE");
  }
}

/******************
 * GETTERS
 ******************
 */
  


/******************
 * SETTERS
 ******************
 */

/**
 * Establece un Gestor de sesiones distinto al original 
 * @param gestorSesion GestorSesion
 */
public void setGestorSesion(GestorSesion gestorSesion) {
  this.gestorSesion = gestorSesion;
}

/**
 * Establece un canal de entrada distinto al original
 * @param in BufferedReader 
 */
public void setIn(BufferedReader in) {
  this.in = in;
}

/**
 * Establece un canal del salida distinto al original creado por el constructor. 
 * @param out PrintStream
 */
public void setOut(PrintStream out) {
  this.out = out;
}
  
/******************
 * GESTIÓN DE USUARIOS
 ******************
 */

/**
 * Se encarga de gestionar el proceso de login. Requiere un usuario y el tipo de permisos exigido. Conecta con la base de datos para comprobar que el usuario existe y tiene los permisos indicados
 * @param usuario Usuario con, al menos, los datos de email y pass del usuario que intenta hacer login
 * @param isGestor un boolean que indica si se está solicitando un login de gestor o de empleado
 * @return Devuele un mensaje de tipo MensajeRespuesta con la información solicitada. Si ha habido algún error, el mensaje solo contendrá el código de error y la petición solicitada. Si es correcta, devolverá un codigo 10, un token de sesión y los datos del usuario
 */
public MensajeRespuesta processMensajeLogin(Usuario usuario, boolean isGestor){
  respuesta =null;
  String pet = (isGestor)? Mensaje.FUNCION_LOGIN_ADMIN:Mensaje.FUNCION_LOGIN;
  //comprobamos que hay datos en la petición:
  if (usuario==null||usuario.getEmail()==null || usuario.getPwd()==null){
    //si alguno de los datos es nulo, entonces enviamos un mensaje de error    
    respuesta = Utiles.mensajeErrorDatosIncorrectos(pet);    
  }else{   
    //si no, continuamos con el login
    //primero comprobamos que el usuario ha mandado los datos correctos y que está en la base de datis
    respuesta = gestorDB.loginMens(usuario.getEmail(), usuario.getPwd(), isGestor);
    String codeString = respuesta.getCode().getCode();    
    System.out.println(SERVER+"código recibido del gestor de la base de datos: "+codeString);
    if(codeString.equals(Codes.CODIGO_OK)) {     
      //si el código es un código "10", entonces generamos un token
      TokenSesion token = new TokenSesion(usuario);
      //comprobamos que no hay sesión iniciada para este usuario
      if(gestorSesion.isToken(usuario.getEmail())){
        System.out.println(SERVER+"el usuario tenía sesión iniciada");
        //si el usuario tiene una sesión, lo que haremos será cerrarla antes de añadirla
        gestorSesion.removeSesion(usuario.getEmail());               
        System.out.println(SERVER+"sesión de usuario antigua cerrada");
      }
      //añadimos la sesión al gestor
      if (!gestorSesion.addSesion(token)){
        //si no se añade la asesión, cambiamos el mensaje respuesta por uno de error:
        System.out.println(SERVER+"no se ha podido añadir la sesión del usuario");
        respuesta = Utiles.mensajeError(pet);
      }else{
        //si todo ha ido bien añadimos al mensaje respuesta el token del usuario
        System.out.println(SERVER+"sesión de usuario añadida");
        respuesta.setData(token);     
      }
    }else {
      respuesta = Utiles.mensajeError(pet);
    }  
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición de logout. Necesita un token de sesión para ello
 * @param token TokenSesion con la información de la sesión del usuario. 
 * @return MensajeRespuesta con la confirmación del cierre de sesión o un código de error
 */
public MensajeRespuesta procesarMensajeLogout(TokenSesion token){
  respuesta = null;
  String pet = "logout";
  //comprobamos que el token no es nulo
  if (token==null||token.getUsuario()==null || token.getToken()==null){    
    respuesta = new MensajeRespuesta (new Codes(Codes.CODIGO_DATOS_INCORRECTOS), pet);
  }else{
    //comprobamos que el token está en el listado de gestorSesion
    if (gestorSesion.removeSesion(token)){
      respuesta = new MensajeRespuesta (new Codes(Codes.CODIGO_OK), pet);
    } else{
      respuesta = new MensajeRespuesta (new Codes(Codes.CODIGO_NO_SESION), pet);      
    }
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición para solicitar datos de usuarios
 * @param token TokenSesion con la información de la sesión del usuario. 
 * @param email String email del Usuario que necesitamos obtener. 
 * @return 
 */
public MensajeRespuesta procesarMensajeDatosUsuario(TokenSesion token, String email){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_DATOS_USER);
  }else{
    //si el código es un código OK, continuamos con el proceso
    respuesta = gestorDB.selectDataUser(token.getUsuario());       
  }
  return respuesta;  
}

/**
 * Procesa un mensaje de petición para añadir a un usuario (empleao o gestor) a la base de datos
 * @param token TokenSesion con la información de la sesión del usuario. 
 * @param user Usuario a insertar en la base de datos
 * @param isGestor boolean que indica si el usuario es empleado (false) o gestor(true)
 * @return MensajeRespuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No lleva datos extras
 */
public MensajeRespuesta procesarMensajeAddUser(TokenSesion token, Usuario user, boolean isGestor){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_ADD_EMP);
  }else{
    //si el código es un código 10, podemos seguir adelante. 
    if (isGestor){
      respuesta = gestorDB.addData(user, Mensaje.FUNCION_ADD_GESTOR);
    }else{
      respuesta = gestorDB.addData(user, Mensaje.FUNCION_ADD_EMP);
    }
    
  }  
  return respuesta;
}

/**
 * Procesa un mensaje de petición para dar de baja a un usuario en la base de datos. No lo elimina, sino que cambia algunos de sus atributos.
 * @param token TokenSesion con la información de la sesión del usuario. 
 * @param email String email del usuario a dar de baja
 * @return MensajeRespuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No lleva datos extras
 */
public MensajeRespuesta procesarMensajeBajaUser(TokenSesion token, String email){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_ADD_EMP);
  }else{
    //si el código es un código 10, podemos seguir adelante.    
    respuesta = gestorDB.bajaUser(email);
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición para actualizar los datos de un Empleado en la base de datos. 
 * @param token TokenSesion con la información de la sesión del usuario. 
 * @param usuario OBjeto de tipo Usuario o Empleado con la información a cambiar en la base de datos
 * @return MensajeRespuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No lleva datos extras
 */
public MensajeRespuesta procesarMensajeUpdateUser(TokenSesion token, Object usuario){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_ADD_EMP);
  }else{
    //si el código es un código 10, podemos seguir adelante.    
    respuesta = gestorDB.updateData(usuario, Mensaje.FUNCION_UPDATE_EMP);
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición para listar todos los empleados asociados a un gestor o a un restaurante. 
 * @param token TokenSesion con la información de la sesión del usuario
 * @param id id 
 * @return MensajeRespuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. Incluye los datos de los usuarios listados
 */
public MensajeRespuesta procesarMensajeListUsersFrom(TokenSesion token, String id){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_ADD_EMP);
  }else{
    //si el código es un código 10, podemos seguir adelante.    
    if (id.isEmpty()){
      //si el id es nulo, significa que el token tiene el id porque es una petición de GESTOR
      respuesta = gestorDB.listUsersFrom(token.getUsuario(), Mensaje.FUNCION_LIST_USERS_FROM_GESTOR);
    }else{
      //si el id no es nulo es que es una petición de RESTAURANTE
      respuesta = gestorDB.listUsersFrom(id, Mensaje.FUNCION_LIST_USERS_FROM_REST);
    }    
  }
  return respuesta;
}

public MensajeRespuesta procesarMensajeGetEmpleadoFromId(TokenSesion token, String peticion){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    String userId = token.getUsuario();
    respuesta = gestorDB.getEmpleadoFromId(userId, peticion);    
  }
  return respuesta;
}
/******************
 * GESTIÓN DE PEDIDOS
 ******************
 */

/**
 * Procesa un mensaje de petición para añadir un pedido al listado.
 * @param token TokenSesion con la información de la sesión del usuario
 * @param pedido Pedido a insertar
 * @return MensajeRespuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No lleva datos extras
 */
public MensajeRespuesta procesarMensajeAddPedido(TokenSesion token, Pedido pedido){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_ADD_EMP);
  }else{
    //si el código es un código 10, podemos seguir adelante. 
    //generamos un ID para el pedido:
    pedido.setId(Pedido.generarId(pedido.getMesa()));
    //añadimos el producto
    respuesta = gestorDB.addData(pedido, Mensaje.FUNCION_ADD_PEDIDO);    
    //comprobamos si el pedido viene con productos. Si viene con productos tendremos que hacer una segunda sentencia INSERT 
    //además, comprobamos que la respuesta del gestor de la base de datos es de código 10
    if ((pedido.getLista_productos().size()>0)&& respuesta.getCode().getCode().equals(Codes.CODIGO_OK)){                  
      System.out.println(SERVER+"iniciando sentencias de inserción de pedido_estado");
      respuesta = gestorDB.addProductoEstado(pedido.getLista_productos(), pedido.getEstado_productos(), pedido.getId());      
    }    
  }
  return respuesta;  
}

/**
 * Procesa un mensaje de petición para eliminar un pedido al listado.
 * @param token TokenSesion con la información de la sesión del usuario
 * @param idPedido String con el ID del pedido a eliminar
 * @return MensajeRespuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No lleva datos extras
 */
public MensajeRespuesta procesarMensajeDeletePedido(TokenSesion token, String idPedido){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_ADD_EMP);
  }else{
    respuesta = gestorDB.deleteData(idPedido, Mensaje.FUNCION_DELETE_PEDIDO);
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición para actualizar los datos de un pedido
 * @param token TokenSesion token con la información de la sesiónd el usuario
 * @param pedido Pedido con la id del pedido y los datos a cambiar
 * @return MensajeRespuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No lleva datos extras
 */
public MensajeRespuesta procesarMensajeUpdatePedido(TokenSesion token, Pedido pedido){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_UPDATE_PEDIDO);
  }else{
    respuesta = gestorDB.updateData(pedido, Mensaje.FUNCION_UPDATE_PEDIDO);
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición para listar los Pedidos activos pertenecientes a un usuario determinado
 * @param token TokenSesion con la información de la sesión del usuario
 * @param idFiltro String con el id del Empleado del que queremos ver los pedidos
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. Incluye los datos de los pedidos listados. 
 */
public MensajeRespuesta procesarMensajeListPedidoFrom(TokenSesion token, String idFiltro, String peticion){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    String id ="";
    //si el código es un código 10, podemos seguir adelante.        
    if (idFiltro==null){
      id = token.getUsuario();
    }else{
      id = idFiltro;
    }
    respuesta = gestorDB.listPedidoFrom(id, peticion);
  }
  return respuesta;
}

/**
 * Procesa un mensaje para devolver un listado de Pedido con todos los productos que tiene asociados
 * @param token TokenSesion con la información de la sesión del usuario
 * @param id String con el ID que diferenciará el pedido (puede ser un ID de empleado o un ID de pedido, la diferenciación se hará con el tipo de Petición hecha)
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. Incluye los datos de los Pedidos solicitados
 */
public MensajeRespuesta procesarMensajeListPedidoCompletoFrom(TokenSesion token, String id, String peticion){
   //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    respuesta = gestorDB.listPedidoCompletoFrom(id, peticion);
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición para añadir un producto a un pedido
 * @param token TokenSesion con la información de la sesión del usuario
 * @param datos String[] con los datos de un ProductoEstado
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No tiene datos asociados
 */
public MensajeRespuesta procesarMensajeAddProductoTo(TokenSesion token, String[] datos, String peticion){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    //si el código es 10, podemos continuar    
    respuesta  = gestorDB.addProductoEstado(datos[0], datos[1], Estado.PREPARANDO.getEstado(), peticion);
  }
  return respuesta;
}

/**
 * Peocesa un mensaje de petición para eliminar un producto de un pedido concreto sin us id de registro
 * @param token TokenSesion con la información de la sesión del usuario
 * @param datos String[] con los datos de un ProductoEstado
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No tiene datos asociados
 */
public MensajeRespuesta procesarMensajeDeleteProductoFrom(TokenSesion token, String[] datos, String peticion){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    //si el código es 10, podemos continuar 
    if (datos.length==2){
      respuesta  = gestorDB.deleteProductoEstado(null, datos[0], datos[1], null, peticion);
    }else if(datos.length==3){
      respuesta  = gestorDB.deleteProductoEstado(null, datos[0], datos[1], datos[2], peticion);
    }else{
      respuesta = Utiles.mensajeErrorDatosIncorrectos(peticion);
    }
    
  }
  return respuesta;
}

/**
 * Peocesa un mensaje de petición para eliminar un producto de un pedido concreto con su id de registro
 * @param token TokenSesion con la información de la sesión del usuario
 * @param datos String con el ID de registro
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No tiene datos asociados
 */
public MensajeRespuesta procesarMensajeDeleteProductoFrom(TokenSesion token, String dato, String peticion){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_ADD_EMP);
  }else{
    //si el código es 10, podemos continuar 
    respuesta  = gestorDB.deleteProductoEstado(dato, null, null, null, peticion);    
  }
  return respuesta;
}

/**
 * Peocesa un mensaje de petición para actualizar un producto de un pedido concreto
 * @param token TokenSesion con la información de la sesión del usuario
 * @param datos String[] con los datos de un ProductoEstado a actualizar
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No tiene datos asociados
 */
public MensajeRespuesta procesarMensajeUpdateProductoFrom(TokenSesion token, String[] datos, String peticion){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_ADD_EMP);
  }else{
    //si el código es 10, podemos continuar 
    if (datos.length==2){
      //si trae 2 elementos, es que nos están mandando el ID
      respuesta  = gestorDB.updateProductoFromPedido(datos[0], null, null, null, datos[1], peticion);    
    }else if (datos.length==4){
      //si trae 4 es que nos está mandando los detalles
      respuesta  = gestorDB.updateProductoFromPedido(null, datos[0], datos[1], datos[2], datos[3], peticion);    
    }else{
      //si hay más o menos datos están mal enviados:
      respuesta = Utiles.mensajeErrorDatosIncorrectos(peticion);
    }    
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición para listar los productos pendientes en cocina
 * @param token TokenSesion con la información de la sesión del usuario
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. Incluye un listado de los ProductosEstado a procesar
 */
public MensajeRespuesta procesarMensajeListProductosPendientes(TokenSesion token){  
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, Mensaje.FUNCION_ADD_EMP);
  }else{
    //si el código es 10, podemos continuar 
    respuesta  = gestorDB.listProductosPendientes();    
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición para añadir un Restaurante a la base de datos
 * @param token TokenSesion con la información de la sesión del usuario
 * @param rest  Restaurante con la información del restaurante a añadir
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No tiene datos asociados
 */
public MensajeRespuesta procesarMensajeAddRestaurante(TokenSesion token, Restaurante rest, String peticion){
  
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    //si el código es 10, podemos continuar 
    System.out.println(SERVER+"Creando gestor");
    Gestor gestor = new Gestor();
    gestor.setEmail(token.getUsuario());
    //truquito para tener el ID del restaurante a mano
    gestor.setNombre(rest.getId());
    gestor.addRestaurante(rest);
    respuesta  = gestorDB.addData(gestor, peticion);    
    System.out.println(SERVER+" llegamos a hacer la petición: "+respuesta);
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición para añadir una Mesa a la base de datos
 * @param token TokenSesion con la información de la sesión del usuario
 * @param mesa  Mesa con la información de la mesa a añadir
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No tiene datos asociados
 */
public MensajeRespuesta procesarMensajeAddMesa(TokenSesion token, Mesa mesa, String peticion){
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    respuesta = gestorDB.addData(mesa, peticion);
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición para añadir un producto a la base de datos
 * @param token TokenSesion con la información de la sesión del usuario
 * @param producto Producto con la información del producto a añadir
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No tiene datos asociados
 */
public MensajeRespuesta procesarMensajeAddProducto(TokenSesion token, Producto producto, String peticion){
  System.out.println(SERVER+"procesando peticion de add producto");
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    respuesta = gestorDB.addData(producto, peticion);
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición para eliominar un producto de  la base de datos
 * @param token TokenSesion con la información de la sesión del usuario
 * @param productoId String con el id del producto a eliminar
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. No tiene datos asociados
 */
public MensajeRespuesta procesarMensajeDeleteProducto(TokenSesion token, String productoId, String peticion){
   System.out.println(SERVER+"procesando peticion de delete producto");
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    respuesta = gestorDB.deleteData(productoId, peticion);
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición de listar todos los productos 
 * @param token TokenSesion con la información de la sesión del usuario
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. Devuelve un array con todos los Productos 
 */
public MensajeRespuesta procesarMensajeListProductos(TokenSesion token, String peticion){
  System.out.println(SERVER+"procesando peticion de listar productos producto");
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    respuesta = gestorDB.listProductos(peticion, null);
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición de listar todas las mesas de un restaurante
 * @param token TokenSesion con la información de la sesión del usuario
 * @param restID String con el ID del restaurante del que buscamos las mesas
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. Devuelve un array con todas las mesas 
 */
public MensajeRespuesta procesarMensajeListMesas(TokenSesion token, String restID, String peticion){
  System.out.println(SERVER+"procesando peticion de listar productos producto");
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    respuesta = gestorDB.listMesas(peticion, restID, null);
  }
  return respuesta;
}

/**
 * Procesa un mensaje de petición de listar todos los ProductosEstado de un pedido concreto
 * @param token TokenSesion con la información de la sesión del usuario
 * @param pedidoId String con el ID del pedido del que queremos conocer los ProductoEstado
 * @param peticion String con el nombre de la petición que estamos realizando
 * @return MensajeRepuesta con el código 10 si todo ha ido bien o un código de error si ha habido algún fallo. Devuelve un array con todos los ProductoEstado
 */
public MensajeRespuesta procesarMensajeListProductosFromId(TokenSesion token, String pedidoId, String peticion){
  System.out.println(SERVER+"procesando peticion de listar productos producto");
  //comprobamos si el token es válido o no
  Codes codigoMens = comprobarSesion(token);
  //si el código NO ES un código OK, mandamos un mensaje de error con lo que nos devuelva el token
  if (!codigoMens.getCode().equals(Codes.CODIGO_OK)){
    respuesta = new MensajeRespuesta(codigoMens, peticion);
  }else{
    respuesta = gestorDB.listProductosEstadoFromPedido(peticion, pedidoId);
  }
  return respuesta;
}
/******************
 * MÉTODOS AUXILIARES
 ******************
 */
/**
 * Envía un mensaje, ya sea de tipo respuesta o solicitud
 * @param mensaje MensajeRepuesta completo 
 */
public void sendMensaje (Mensaje mensaje){
  String mensajeJSON = gson.toJson(mensaje);
  out.println(mensajeJSON);
  out.flush();
}

/**
 * Envía un mensaje de fin de sesión al cliente y cierra el canal entre ambos.
 */
public void endConnection(){
  respuesta = new MensajeRespuesta (new Codes(Codes.END_CONNECTION), "fin conexión");
  sendMensaje(respuesta);
}

/**
 * Comprueba si una sesión está activa o no
 * @param token TokenSesion a verificar en el listado de sesiones
 * @return true si la sesión está en el listado de sesiones y false si no
 */
private Codes comprobarSesion(TokenSesion token){
  Codes codigoRet = null;
  //comprobamos que el token es válido
  if (token==null||token.getUsuario()==null || token.getToken()==null){    
    codigoRet = new Codes(Codes.CODIGO_DATOS_INCORRECTOS);
  }else{
    //comprobamos que el token está en el listado de gestorSesion
    if (gestorSesion.isToken(token.getUsuario())){
      codigoRet = new Codes(Codes.CODIGO_OK);
    }else{
      codigoRet = new Codes(Codes.CODIGO_NO_SESION);
    }       
  }
  return codigoRet;
} 
}
