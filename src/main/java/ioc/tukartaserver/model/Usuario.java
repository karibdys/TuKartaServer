package ioc.tukartaserver.model;

import java.util.Date;

/**
 * Clase que representa un usuario de la base de datos del proyecto TuKarta
 * @author Manu Mora
 */
public class Usuario {

private String email;     // primary key
private String usuario;
private String pwd;       //si es nula es que el usuario está dado de baja
private String nombre;    
private String apellidos;
private Date fecha_alta;
private Date fecha_modificacion;
private Date fecha_baja;
private boolean isGestor;

/******************
 * CONSTRUCTOR
 ******************
 */

/**
 * Constructor básico de un usuario sin parámetros.
 */
public Usuario(){   
}

public Usuario(String id){
  this.email=id;
}

/**
 * Constructor de la clase Usuario
 * @param nombreUser  String: nombre del usuario 
 * @param pass String: contraseña del usuario
 * @param email String: email del usuario
 * @param nombreReal  String: nombre real del usaurio
 * @param apellidoReal String: apellido real del usuario
 * @param isGestor boolean: true si el usuario tendrá permisos de administrador, false si no 
 */
public Usuario (String nombreUser, String pass, String email, String nombreReal, String apellidoReal, boolean isGestor){
  this.usuario=nombreUser;
  this.pwd=pass;
  this.email=email;
  this.nombre=nombreReal;
  this.apellidos=apellidoReal;
  this.fecha_alta= this.fecha_modificacion = new Date();  
  this.isGestor=isGestor;
}


/**
 * Constructor de la clase Usuario
 * @param nombreUser  String: nombre del usuario 
 * @param pass String: contraseña del usuario
 * @param email String: email del usuario
 * @param nombreReal  String: nombre real del usaurio
 * @param apellidoReal String: apellido real del usuario
 * @param isGestor boolean: true si el usuario tendrá permisos de administrador, false si no 
 */
public Usuario (String nombreUser, String pass, String email, String nombreReal, String apellidoReal, Date fechaAlta, Date fechaMod, Date fechaBaja, boolean isGestor){
  this.usuario=nombreUser;
  this.pwd=pass;
  this.email=email;
  this.nombre=nombreReal;
  this.apellidos=apellidoReal;
  this.fecha_alta=fechaAlta;
  this.fecha_modificacion=fechaMod;
  this.fecha_baja = fechaBaja;
  this.fecha_alta= this.fecha_modificacion = new Date();  
  this.isGestor=isGestor;
}


/******************
 * GETTERS
 ******************
 */

/**
 * Devuelve el nombre de usuario de este usuario
 * @return String
 */
  public String getUsuario() {
    return usuario;
  }
  
  /**
   * Devuelve la contraseña de este usuario
   * @return String
   */

  public String getPwd() {
    return pwd;
  }

  /**
   * Devuelve el email de este usuario
   * @return String
   */
  public String getEmail() {
    return email;
  }
  
/**
 * Devuelve el nombre real del usuario
 * @return String
 */
  public String getNombre() {
    return nombre;
  }

  /**
   * Devuelve los apellidos del usuario
   * @return String
   */
  public String getApellidos() {
    return apellidos;
  }
  
  /**
   * Devuelve el tipo de usuario 
   * @return true si el usuario es gestor, false si no lo es
   */
  public boolean getIsGestor(){
    return this.isGestor;
  }

  /**
   * Devuelve la fecha de alta en la que se registró el usuario
   * @return Date
   */
  public Date getFecha_alta() {
    return fecha_alta;
  }

  /**
   * Devuelve la fecha de la última modificación de los datos del usuario
   * @return Date
   */
  public Date getFecha_modificacion() {
    return fecha_modificacion;
  }
  
  /**
   * Devuelve la fecha de baja del usuario
   * @return Date
   */
  public Date getFecha_baja() {
    return fecha_baja;
  }

 /******************
 * SETTERS
 ******************
 */
  
  /**
   * Establece el nombre de usuario de este usuario. Cambia le fecha de última modificación de los datos del usuario
   * @param nomUsuario String
   */
  public void setUsuario(String nomUsuario) {
    this.usuario = nomUsuario;  
  }
  
/**
 * Establece la contraseña de este usuario. Cambia le fecha de última modificación de los datos del usuario
 * @param password String
 */
  public void setPwd(String password) {
    this.pwd = password;
  }

  /**
   * Establece el email de este usuario. Cambia le fecha de última modificación de los datos del usuario
   * @param email String
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Establece el nombre real este usuario. Cambia le fecha de última modificación de los datos del usuario 
   * @param nombre String
   */
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   * Establece los apellidos de este usuario. Cambia le fecha de última modificación de los datos del usuario
   * @param apellido 
   */
  public void setApellidos(String apellido) {
    this.apellidos = apellido;
  }
  
  /**
   * Establece si el usuario es o no gestor. Cambia le fecha de última modificación de los datos del usuario
   * @param gestor 
   */
   public void  setIsGestor(boolean gestor){
    this.isGestor = gestor;
    this.fecha_modificacion=new Date();
  }
  
 /**
  * Establece la fecha de alta de este usuario
  * @param fechaAlta Date con la fecha de alta del usuario
  */  
 public void setFecha_alta(Date fechaAlta){
   this.fecha_alta=fechaAlta;
 }
  /**
   * Cambia le fecha de última modificación de los datos del usuario
   * @param fechaUltimaMod Date
   */
  public void setFecha_modificacion(Date fechaUltimaMod) {
    this.fecha_modificacion = fechaUltimaMod;    
  }

  /**
   * Establece la fecha de baja de usuario. Cambia le fecha de última modificación de los datos del usuario
   * @param fechaBaja Date
   */
  public void setFecha_baja(Date fechaBaja) {
    this.fecha_baja = fechaBaja;
  }  
 
  /**
   * Devuelve un String con la representación de los datos de este Usuario
   * @return String
   */
  @Override
  public String toString(){
    StringBuilder builder =new StringBuilder();
    if (this.usuario!=null){
      builder.append("usuario: "+this.usuario);
    }
    if (this.pwd!=null){
      builder.append("\npassword: "+this.pwd);
    }
    if(this.nombre!=null){
      builder.append("\nnombre real: "+this.nombre);
    }
    if (this.apellidos!=null){
      builder.append("\napellido real: "+this.apellidos); 
    }
    if (this.email!=null){
      builder.append("\nemail: "+this.email);
    }        
    if (this.fecha_alta!=null){
      builder.append("\nalta: "+this.fecha_alta);
    }
    if (this.fecha_modificacion!=null){
      builder.append("\nalta: "+this.fecha_modificacion);
    }
    if (this.fecha_baja!=null){
      builder.append("\nalta: "+this.fecha_baja);
    }
    
    return builder.toString();
  }
  
}
