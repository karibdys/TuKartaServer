package ioc.tukartaserver.model;

import ioc.tukartaserver.gestorDB.GestorDB;
import ioc.tukartaserver.security.GestorCrypto;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 *  Clase con métodos variados y útiles para la gestión del servidor
 * @author Manu Mora
 */
public class Utiles {

public static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

/**
 * Convierte un objeto Date en un formato legible y simple: dd/mm/aaaa
 * @param fecha Date con una fecha
 * @return String con la fecha en formato dd/MM/AAAA
 */
public static Date ParseDate(String fecha){
  Date date = null;
  try {
    date = formato.parse(fecha);
  }
  catch (ParseException ex) 
  {
    System.out.println(ex);
  }
  return date;
}

public static String ParseDate(Date fecha){  
  return formato.format(fecha);
}


/***************
 * CONSTRUCTORES DE MENSAJES RÁPIDOS
***************/

 /**
  * Construyen un mensaje genérico de petición completada con éxito: CODIGO 10
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 10 y la petición a respondida. Necesita añadir los datos
  */
 public static MensajeRespuesta mensajeOK(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_OK), peticion);  
 }
 
 /**
  * Construye un mensaje genérico de error debido al sistema de seguridad del servidor: CÓDIGO 20
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 20 y la petición a respondida. 
  */
 public static MensajeRespuesta mensajeErrorSeguridad(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERROR_KEY), peticion);  
 }
 
  /**
  * construye un mensaje genérico de error de datos incorrectos: CÓDIGO 40
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 40 y la petición indicada como parámetro. 
  */
 public static MensajeRespuesta mensajeErrorDatosIncorrectos (String peticion){
    return new MensajeRespuesta(new Codes(Codes.CODIGO_DATOS_INCORRECTOS), peticion);  
 }
 
   /**
  * construye un mensaje genérico de error de función no soportada: CÓDIGO 41
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 41 y la petición indicada como parámetro. 
  */
 public static MensajeRespuesta mensajeErrorFuncionNoSoportada (String peticion){
    return new MensajeRespuesta(new Codes(Codes.CODIGO_FUNCION_ERR), peticion);  
 }
 
/**
  * Construyen un mensaje genérico de error de usuario no indicado: CÓDIGO 42
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 42 y la petición a respondida
  */ 
 public static MensajeRespuesta mensajeErrorNoUser (String peticion){
    return new MensajeRespuesta(new Codes(Codes.CODIGO_NO_USER), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de error cuando la sesión no está en el listado de sesiones: CÓDIGO 44
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 44 y la petición a respondida
  */
 
  public static MensajeRespuesta mensajeNoSesion(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_NO_SESION), peticion);  
 }
  
  /**
  * Construyen un mensaje de error cuando el usuario que intenta acceder ha sido dado de baja: CÓDIGO 45
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 45 y la petición a respondida
  */
   public static MensajeRespuesta mensajeErrorBaja(String peticion) {
    return new MensajeRespuesta(new Codes(Codes.CODIGO_ERROR_USER_BAJA), peticion);  
  }

  /**
  * Construyen un mensaje genérico de error : CÓDIGO 50
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 50 y la petición a respondida
  */
 public static MensajeRespuesta mensajeError(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR), peticion);  
 }
 
 /**
  * Construyen un mensaje genérico de error producido en la Base de Datos CÓDIGO 60
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 60 y la petición a respondida
  */
 public static MensajeRespuesta mensajeErrorDB(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR_BD), peticion);  
 }
 
  /**
  * Construyen un mensaje genérico de error producido porque el dato no está en la base de datos: CÓDIGO 61
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 60 y la petición a respondida
  */
 public static MensajeRespuesta mensajeErrorPKNotFound(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR_PK_NOT_FOUND), peticion);  
 }
  
 /**
  * Construyen un mensaje genérico de error producido en la Base de Datos al no coincidir la contraseña con el usuari introducido CÓDIGO 62
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 62 y la petición a respondida
  */
  public static MensajeRespuesta mensajeErrorPWD(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERR_PWD), peticion);  
 }
  
  /**
  * Construyen un mensaje genérico de error producido en la Base de Datos al estar la Primary Key repetida en la base de datos
  * @param peticion String nombre de la petición a responder
  * @return MensajeRespuesta con el código 63 y la petición a respondida
  */
  public static MensajeRespuesta mensajeErrorUserRep(String peticion){
   return new MensajeRespuesta(new Codes(Codes.CODIGO_ERROR_DATA_REP), peticion);  
 }
 
  /**
  * Construyen un mensaje genérico de fin de conexión: CÓDIGO 90
  * @return MensajeRespuesta con el código 90 y la petición "fin de conexión"
  */
  public static MensajeRespuesta mensajeEndConnection(){
   return new MensajeRespuesta(new Codes(Codes.END_CONNECTION), "fin de conexion");  
 }
 
 /***********************
  * CONVERSORES A SQL DE USUARIO
  ********************/
   
  /**
   * Crea una sentencia SQL con el comando INSERT INTO para poder añadir un objeto de tipo Usuario o Empleado en función de los atributos que tiene definidos. 
   * @param user Usuario o Empleado con los campos necesarios para construir un registro válido en la BBDD, es decir, con nombre de usuario, password, email, fecha de alta, fecha de modificación y el indicativo de si es gestor.
   * @return String con la sentencia INSERT SQL completa
   */
  public static String sentenciaUsuarioToInsertSQL (Object user) throws NoSuchAlgorithmException{
    StringBuilder builder = new StringBuilder();
    builder.append("INSERT INTO "+GestorDB.TABLA_USERS+" (");
    //primero ponemos los campos obligatorios
    builder.append("\"usuario\", \"pwd\", \"email\", \"fecha_alta\", \"fecha_modificacion\"");
    //ahora comprobamos que los campos tienen datos para introducirlos
    if (((Usuario)user).getNombre()!=null){
      builder.append(", \"nombre\"");   
    }
    if (((Usuario)user).getApellidos()!=null){
      builder.append(", \"apellidos\"");   
    }    
    if (((Usuario)user).getFecha_baja()!=null){
      builder.append(", \"fecha_baja\"");      
    }
    if (user instanceof Empleado){
      if (((Empleado)user).getGestor() !=null){         
        builder.append(", \"gestor\"");
      }
      if (((Empleado)user).getTrabajadorDe()!=null){
        builder.append(", \"trabajadorde\"");
      }
      builder.append(",\"salario\"");
      if (((Empleado)user).getRol()!=null){
        builder.append(", \"rol\"");      
      }
    }    
    
    builder.append(") VALUES (");
    //hasta aquí construimos la primera parte de la sentencia.    
    //ahora toca meter los datos
    builder.append("'"+ ((Usuario)user).getUsuario()+"'");
    //para la pass tenemos que encriptar
    String dato1 = ((Usuario)user).getPwd();
    String dato2 = ((Usuario)user).getEmail();
    String passEnc = GestorCrypto.encriptarPass(dato1, dato2);
    builder.append(", '"+ passEnc);
    builder.append(", '"+ dato2);
    builder.append(", '"+ convertDateJavaToSQL(((Usuario)user).getFecha_alta())+"'");
    builder.append(", '"+ convertDateJavaToSQL(((Usuario)user).getFecha_modificacion())+"'");
    //pasamos a comprobar los campos aleatorios:
    if(((Usuario)user).getNombre()!=null){
      builder.append(", '"+ ((Usuario)user).getNombre()+"'");
    }
    if (((Usuario)user).getApellidos()!=null){
      builder.append(", '"+ ((Usuario)user).getApellidos()+"'");
    }    
    if (((Usuario)user).getFecha_baja()!=null){
      builder.append(", '"+ convertDateJavaToSQL(((Usuario)user).getFecha_baja())+"'");
    }  
    if (user instanceof Empleado){
      if (((Empleado)user).getGestor() !=null){         
      builder.append(", '"+ ((Empleado)user).getGestor().getEmail()+"'");
      }
      if (((Empleado)user).getTrabajadorDe()!=null){
        builder.append(", '"+ ((Empleado)user).getTrabajadorDe().getId()+"'");
      }
      //salario
      builder.append(", "+ ((Empleado)user).getSalario()+"");
      if (((Empleado)user).getRol()!=null){
        builder.append(",'"+ ((Empleado)user).getRol().getNombreRol()+"'");
      }    
    }    
        
    builder.append(")");
    System.out.println("GESTOR BASE DATOS: sentencia\n--> "+builder.toString());
    return builder.toString();
  }   
 
  /**
   * Crea una sentencia SQL con el comando UPDATE para poder actualizar un objeto de tipo Usuario o Empleado en función de los atributos que tiene definidos. Aquellos datos que sean nulos no serán incorporados en la sentencia.
   * @param user Usuario o Empleado con los campos que se modificarán. Como mínimo necesita la PRIMARY KEY, que es el email, y otro dato a actualizar.
   * @return String con la sentencia UPDATE SQL completa
   */
  public static String sentenciaUsuarioToUpdateSQL (Usuario user){
    StringBuilder builder = new StringBuilder();
    builder.append("UPDATE "+GestorDB.TABLA_USERS+" SET ");
    //como en teoría el mail no cambia, lo ponemos primero para poder establecer luego las "comas"
    builder.append("\"email\" = '"+user.getEmail()+"'");
    if (user.getUsuario()!=null){
      builder.append(", \"usuario\" = '"+user.getUsuario()+"'");
    }
    if (user.getPwd()!=null){
      builder.append(", \"pwd\" = '"+user.getPwd()+"'");
    }
    if (user.getNombre()!=null){
      builder.append(", \"nombre\" = '"+user.getNombre()+"'");
    }
    if (user.getApellidos()!=null){
      builder.append(", \"apellidos\" = '"+user.getApellidos()+"'");
    }
    //la fecha de modificación se pone OBLIGATORIAMENTE
    builder.append(", \"fecha_modificacion\" = '"+Utiles.ParseDate(new Date())+"'");
    
    //hasta aquí el usuario, ahora toca ver si es o no empleado
    if (user instanceof Empleado){    
      if (((Empleado)user).getGestor()!=null){
        builder.append(", \"gestor\" = '"+((Empleado)user).getGestor().getEmail()+"'");
      }
      if (((Empleado)user).getTrabajadorDe()!=null){
        builder.append(", \"trabajadorde\" = '"+((Empleado)user).getTrabajadorDe().getId()+"'");
      }    
      if (((Empleado)user).getSalario()!=0){
        builder.append(", \"salario\" = '"+((Empleado)user).getSalario()+"'");
      }
      if(((Empleado)user).getRol()!=null){
        builder.append(", \"rol\" = '"+((Empleado)user).getRol().getNombreRol()+"'");
      }
    }          
    
    builder.append(" WHERE email = '"+user.getEmail()+"'");
    return builder.toString();
  }
    
  /**
  * Crea un usuario a partir de un ResultSet. Puede ser un usuario completo (con todos los datos) o simple (sin los datos menos importantes. Nunca llevará la contraseña.
  * @param result ResultSet que contiene el dato del Usuario
  * @param isGestor boolean que indica si el usuario es de tipo gestor o empleado
  * @param basic boolean que indica si queremos todos los datos del usuario o solo los básicos
  * @return Usuario que puede ser Empleado o Gestor
  * @throws SQLException Al acceder a la base de datos
  */
 public static Empleado createEmpleadoFromResultSet(ResultSet result, boolean basic) throws SQLException{
   Empleado user = new Empleado();   
   if (user ==null){
     //si el usuario es nulo, devolvemos un nulo
     return user = null;
   }else{
     //si no es nulo, empezamos a crear el usuario a partir de los datos del ResultSet:
     user.setUsuario(result.getString("usuario"));
     user.setEmail(result.getString("email"));
     user.setNombre(result.getString("nombre"));
     user.setApellidos(result.getString("apellidos"));  
     //si nos piden los datos completos:
     if (!basic){       
       if (result.getDate("fecha_alta")!=null){
         user.setFecha_alta(Utiles.convertDateSQLtoJava(result.getDate("fecha_alta")));
         System.out.println("    UTILES: "+user.getFecha_alta());
       }
       if (result.getDate("fecha_modificacion")!=null){
         user.setFecha_modificacion(Utiles.convertDateSQLtoJava(result.getDate("fecha_modificacion")));
         System.out.println("    UTILES: "+user.getFecha_modificacion());
       }     
       if (result.getDate("fecha_baja")!=null){
         user.setFecha_baja(Utiles.convertDateSQLtoJava(result.getDate("fecha_baja")));
         System.out.println("    UTILES: "+user.getFecha_baja());
       }
     }
     //Si el usuario es de tipo Empleado, entonces crearemos el empleado
     if (result.getString("trabajadorde")!=null){
       //creamos el restaurante
       String idRest = result.getString("id");
       String  nombreRest = result.getString(15);
       Restaurante rest = new Restaurante (idRest, nombreRest);
       ((Empleado)user).setRestaurante(rest);
     }
     ((Empleado)user).setSalario(result.getFloat("salario"));                  
     
     if (result.getString("rol")!=null){
       String rol = result.getString("rol");
       ((Empleado)user).setRol(Rol.valueOf(rol.toUpperCase()));
     }      
     return user;
   }
 }
 
  /**
  * Crea un Pedido a partir de un ResultSet. 
  * @param result ResultSet que contiene el dato del Pedido  
  * @return Pedido creado a partir de la sentencia SQL ejecutada. 
  * @throws SQLException Al acceder a la base de datos
  */
public static Pedido createPedidoFromResultSet(ResultSet result) throws SQLException {
  Pedido ret = new Pedido();
  ret.setId(result.getString("id"));
  Empleado emp = new Empleado ();
  emp.setEmail(result.getString("empleado"));
  ret.setEmpleado(emp);
  Mesa mesa = new Mesa(result.getString("mesa"));
  ret.setMesa(mesa);
  ret.setFecha(Utiles.convertDateSQLtoJava(result.getDate("fecha")));  ret.setPrecio_final(result.getFloat("precio_final"));
  ret.setActivo(result.getBoolean("activo"));
  
  return ret;
}

  /**
  * Crea un Producto a partir de un ResultSet. 
  * @param result ResultSet que contiene el dato del Producto  
  * @return Producto  creado a partir de la sentencia SQL ejecutada. 
  * @throws SQLException Al acceder a la base de datos
  */
public static Producto createProductoFromResultSet(ResultSet result, GestorDB gestor) throws SQLException{
  System.out.println("UTILES: añadiendo producto: "+result.getString("nombre"));
  Producto producto = new Producto();
  //id
  producto.setId(result.getString("id"));
  //nombre
  producto.setNombre(result.getString("nombre"));
  //alergenos
  if (result.getArray("alergenos")!=null){
    if(result.getArray("alergenos").toString().length()>2){
      System.out.print("Añadiendo alérgenos");
      String array = result.getArray("alergenos").toString();
      HashSet<Alergeno> listado = createAlergenoFromArray(array);    
      System.out.println("TAMAÑO 2: "+listado.size());    
      for(Alergeno a: listado){
        System.out.println(a.getTipo());
        producto.addAlergeno(a);
      }
    }    
  }  
  //precio
  producto.setPrecio(result.getFloat("precio"));  
  //disponible
  producto.setDisponibles(result.getInt("disponible"));
  //tiempo_elaboracion
  producto.setTiempo_elaboracion(result.getInt("tiempo_elaboracion"));  
  //contenido
  if (result.getArray("contenido")!=null){
    if(result.getArray("contenido").toString().length()>2){      
      Array arrayTemp = result.getArray("contenido");        
      String[] content = (String[])arrayTemp.getArray();
      ArrayList<Producto> listadoProd = new ArrayList<>();
      for (String idProd: content){
        Producto prod = gestor.getProductoFromId(idProd);
        listadoProd.add(prod);
      }
      producto.setContenido(listadoProd);      
    }
  }
  //precio_real
  
  producto.setPrecio_real(result.getFloat("precio_real"));
  
  //TODO imagen
  return producto;
}

  /**
  * Crea un ProductoEstado a partir de un ResultSet. 
  * @param result ResultSet que contiene el dato del ProductoEstado  
  * @return ProductoEstado creado a partir de la sentencia SQL ejecutada. 
  * @throws SQLException Al acceder a la base de datos
  */
public static ProductoEstado createProductoEstadoFromResultSet(ResultSet result, GestorDB gestor) throws SQLException{
  ProductoEstado producto = new ProductoEstado(); 
  producto.setIdRegistro(result.getString("id"));
  producto.setIdPedido(result.getString("id_pedido"));
  producto.setIdProducto(result.getString("id_producto"));
  producto.setEstado(Estado.valueOf(result.getString("estado").toUpperCase()));
  producto.setNombreProducto(result.getString("nombre"));  
  return producto;
}

  /**
  * Crea un objeto Mesa a partir de un ResultSet. 
  * @param result ResultSet que contiene el dato de la Mesa  
  * @return Mesa creada a partir de la sentencia SQL ejecutada. 
  * @throws SQLException Al acceder a la base de datos
  */
public static Mesa createMesaFromResultSet (ResultSet result) throws SQLException{
  Mesa mesa = new Mesa();
  mesa.setId(result.getString("id"));
  mesa.setNum_comensales(result.getInt("num_comensales"));  
  mesa.setIdRestaurante(result.getString("restaurante"));
  return mesa;
}

  /**
  * Crea un set de Alérgeno a partir de una cadena que contiene los datos de un array JSON
  * @param cadena String con una cadena que representa un Array de Alergeno en formato JSON --> [{alergeno}, {alergeno2},...]
  * @return Pedido creado a partir de la sentencia SQL ejecutada. 
  * @throws SQLException Al acceder a la base de datos
  */
public static HashSet<Alergeno> createAlergenoFromArray(String cadena){
  HashSet<Alergeno> listadoAlerg = new HashSet<>();
  String cadena2 = cadena.substring(1, cadena.length()-1);
  String[] arrayString = cadena2.split(",");
  for (String alerg: arrayString){
    Alergeno alergeno = Alergeno.valueOf(alerg.toUpperCase());        
    listadoAlerg.add(alergeno);
  }
  return listadoAlerg;
}

 
 /***********************
  * CONVERSORES A SQL 
  ********************/
   /**
   * Crea una sentencia SQL con el comando INSERT INTO para poder añadir un objeto de tipo Pedidoa la base de datos en función de sus parámetros definidos. 
   * @param pedido Pedido con los datos necesarios para insertarlo en la base de datos.
   * @return String con la sentencia INSERT SQL completa
   */
public static String sentenciaPedidoToInsertSQL (Pedido pedido){
  StringBuilder builder = new StringBuilder();
  builder.append("INSERT INTO "+GestorDB.TABLA_PEDIDO+" (\"id\", \"empleado\", \"mesa\", \"fecha\", \"activo\") VALUES (");
  //le metemos los datos
  builder.append("'"+ pedido.getId()+"'");
  builder.append(", '"+ pedido.getEmpleado().getEmail()+"'");
  builder.append(", '"+ pedido.getMesa().getId()+"'");
  if (pedido.getFecha()!=null){
    builder.append(", '"+Utiles.ParseDate(pedido.getFecha())+"'");
  }else{
    builder.append(", '"+Utiles.ParseDate(new Date())+"'");
  }
  builder.append(", 'true'");
  builder.append(")");
  
  return builder.toString();    
}
   /**
   * Crea una sentencia SQL con el comando UPDATE para poder actualizar un Pedido
   * @param pedido Pedido con los datos necesarios para insertarlo en la base de datos.
   * @return String con la sentencia UPDATE SQL completa
   */
  public static  String sentenciaPedidoToUpdateSQL (Pedido pedido){
  StringBuilder builder = new StringBuilder();
  builder.append("UPDATE "+GestorDB.TABLA_PEDIDO+" SET ");
  builder.append("\"id\" = '"+pedido.getId()+"'");
  if (pedido.getEmpleado()!=null){
    builder.append(", \"empleado\" = '"+pedido.getEmpleado().getEmail()+"'");
  }
  if (pedido.getMesa()!=null){
    builder.append(", \"mesa\" = '"+pedido.getMesa().getId()+"'");
  }
  if (pedido.getPrecio_final()!=0){
    builder.append(", \"precio_final\" = "+pedido.getPrecio_final());
  }
  builder.append(", \"activo\" = '"+pedido.isActivo()+"'");
  builder.append(" WHERE id = '"+pedido.getId()+"'");
  return builder.toString();
}   

  /**
   * Crea una sentencia SQL con el comando INSERT INTO para poder añadir un objeto de tipo Restaurante la base de datos en función de sus parámetros definidos. 
   * @param restaurante Restaurante con los datos necesarios para insertarlo en la base de datos.
   * @return String con la sentencia INSERT SQL completa
  */  
  public static String sentenciaRestauranteToInsertSQL(Restaurante restaurante, String gestorEmail){
  StringBuilder builder = new StringBuilder();
  builder.append("INSERT INTO "+GestorDB.TABLA_RESTAURANTE+" (\"id\", \"nombre\", \"provincia\", \"direccion\", \"gestor\") VALUES (");
  builder.append("'"+ restaurante.getId()+"'");
  builder.append(", '"+ restaurante.getNombre()+"'");
  if (restaurante.getProvincia()!=null){
    builder.append(", '"+ restaurante.getProvincia().getNombre()+"'");
  }      
  if (restaurante.getDireccion()!=null){
    builder.append(", '"+ restaurante.getDireccion()+"'");
  }      
  builder.append(", '"+ gestorEmail+"'");
  builder.append(")");           
  return builder.toString();
}
  
/**
 * Crea una sentencia SQL con el comando INSERT INTO para poder añadir un objeto de tipo Mesa a la base de datos en función de sus parámetros definidos. 
 * @param mesa Mesa con los datos necesarios para insertarlo en la base de datos.
 * @return String con la sentencia INSERT SQL completa
*/   
public static String sentenciaMesaToInsertSQL(Mesa mesa){
  StringBuilder builder = new StringBuilder();
  builder.append("INSERT INTO "+GestorDB.TABLA_MESA+" (\"id\", \"num_comensales\", \"restaurante\") VALUES (");
  builder.append("'"+mesa.getId()+"'");
  builder.append(", '"+mesa.getNum_comensales()+"'");
  builder.append(", '"+mesa.getIdRestaurante()+"'");
  builder.append(")");     
  return builder.toString();
}

/**
 * Crea una sentencia SQL con el comando INSERT INTO para poder añadir un objeto de tipo Producto a la base de datos en función de sus parámetros definidos. 
 * @param producto  Producto con los datos necesarios para insertarlo en la base de datos.
 * @return String con la sentencia INSERT SQL completa
*/ 
public static String sentenciaProductoToInsertSQL(Producto producto){
  StringBuilder builder = new StringBuilder();
  builder.append("INSERT INTO "+GestorDB.TABLA_PRODUCTO+" (\"id\", \"nombre\", \"precio\", \"disponible\", \"tiempo_elaboracion\"");
  //campos optativos
  if (producto.getAlergenos().size()!=0){
    builder.append(", \"alergenos\"");
  }
  //nota, nunca podrá tener ni contenido ni precio real porque eso es solo para menus
    builder.append(") VALUES (");
  builder.append("'"+producto.getId()+"'");
  builder.append(", '"+producto.getNombre()+"'");
  builder.append(", "+producto.getPrecio()+"");
  builder.append(", "+producto.getDisponibles()+"");  
  builder.append(", "+producto.getTiempo_elaboracion()+"");  
  if (producto.getAlergenos().size()!=0){
    //'{gluten, lacteos, huevo}'
    builder.append(", '{");
    int items = producto.getAlergenos().size()-1;
    for (Alergeno a: producto.getAlergenos()){
      builder.append(a.getTipo());
      if (items>0){
        builder.append(", ");
        items--;
      }      
    }
    builder.append("}'");
  }  
  builder.append(")");
  
  return builder.toString();
  
}
  
  /**
 * Método para construir fechas conrrectas para insertar en la base de datos. 
 * @param uDate Date en formato Java
 * @return Date en formato SQL
 */
 public static java.sql.Date convertDateJavaToSQL (java.util.Date uDate) {
   java.sql.Date sDate = new java.sql.Date(uDate.getTime());
   return sDate;
 }

  /**
  * Método para construir fechas correctas en Java a partir de un date de SQL
  * @param uDate una fecha en formato SQL
  * @return Date con la fecha del SQL
  */
 public static java.util.Date convertDateSQLtoJava(java.sql.Date uDate) {
  java.util.Date javaDate = new java.util.Date(uDate.getTime());   
   return javaDate;
 }
 
  
}
