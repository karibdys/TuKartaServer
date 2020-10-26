package ioc.tukartaserver.model;

import java.util.Date;

/**
 * Clase que representa un usuario de la base de datos del proyecto TuKarta
 * @author Manu Mora
 */
public class Usuario {

private String usuario;
private String pwd;
private String email;
private String nombre;
private String apellidos;
//private Restaurante[] restaurantes;
private Date fecha_alta;
private Date fecha_modificacion;
private Date fecha_baja;
private boolean isGestor;

/**
 * Constructor básico de un usuario sin parámetros. Incluye la fecha de creación.
 */
public Usuario(){
 this.fecha_alta= this.fecha_modificacion = new Date();    
}

/**
 * Constructor de la clase Usuario
 * @param nombreUser  String: nombre del usuario 
 * @param pass String: contraseña del usuario
 * @param email String: email del usuario
 * @param nombreReal  String: nombre real del usaurio
 * @param apellidoReal String: apellido real del usuario
 * @param gestor boolean: true si el usuario tendrá permisos de administrador, false si no 
 */
public Usuario (String nombreUser, String pass, String email, String nombreReal, String apellidoReal, boolean gestor){
  this.usuario=nombreUser;
  this.pwd=pass;
  this.email=email;
  this.nombre=nombreReal;
  this.apellidos=apellidoReal;
  this.fecha_alta= this.fecha_modificacion = new Date();  
  this.isGestor=gestor;
}

  public String getUsuario() {
    return usuario;
  }

  public String getPwd() {
    return pwd;
  }

  public String getEmail() {
    return email;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellidos;
  }
  
  public boolean getIsGestor(){
    return this.isGestor;
  }

  /*
  public Restaurante[] getRestaurantes() {
    return restaurantes;
  }
  */

  public Date getFecha_alta() {
    return fecha_alta;
  }

  public Date getFecha_modificacion() {
    return fecha_modificacion;
  }

  public Date getFecha_baja() {
    return fecha_baja;
  }

  public void setUsuario(String nomUsuario) {
    this.usuario = nomUsuario;
    this.fecha_modificacion= new Date();
  }

  public void setPwd(String password) {
    this.pwd = password;
    this.fecha_modificacion= new Date();
  }

  public void setEmail(String email) {
    this.email = email;
    this.fecha_modificacion= new Date();
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
    this.fecha_modificacion= new Date();
  }

  public void setApellido(String apellido) {
    this.apellidos = apellido;
    this.fecha_modificacion= new Date();
  }
  
   public void  setIsGestor(boolean gestor){
    this.isGestor = gestor;
  }
  
  /*
  public void setRestaurantes(Restaurante[] restaurantes) {
    this.restaurantes = restaurantes;
    this.fechaUltimaMod= new Date();
  }
  */

  public void setFecha_modificacion(Date fechaUltimaMod) {
    this.fecha_modificacion = fechaUltimaMod;    
  }

  public void setFecha_baja(Date fechaBaja) {
    this.fecha_baja = fechaBaja;
    this.fecha_modificacion= new Date();
  }  
 
  @Override
  public String toString(){
    StringBuilder builder =new StringBuilder();
    builder.append("usuario: "+this.usuario);
    builder.append("\npassword: "+this.pwd);
    builder.append("\nnombre real: "+this.nombre);
    builder.append("\napellido real: "+this.apellidos);
    builder.append("\nemail: "+this.email);
    
    return builder.toString();
  }
  
}
