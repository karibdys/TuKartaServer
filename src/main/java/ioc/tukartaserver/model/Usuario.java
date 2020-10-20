package ioc.tukartaserver.model;

import java.util.Date;
import org.json.JSONObject;

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

//nombres de los campos
public static final String NOM_USUARIO = "usuario";
public static final String PASSWORD = "pwd";
public static final String EMAIL = "email";
public static final String NOM_REAL = "nombre";
public static final String APE_REAL = "apellidos";
public static final String FECHA_ALTA = "fecha_alta";
public static final String FECHA_MOD = "fecha_modificacion";
public static final String FECHA_BAJA = "fecha_baja";

/**
 * Constructor básico de un usuario sin parámetros. Incluye la fecha de creación.
 */
public Usuario(){
 this.fecha_alta= this.fecha_modificacion = new Date();    
}
/**
 * Constructor de la clase Usuario
 * @param nombreUser 
 * @param pass
 * @param email
 * @param nombreReal
 * @param apellidoReal 
 */
public Usuario (String nombreUser, String pass, String email, String nombreReal, String apellidoReal){
  this.usuario=nombreUser;
  this.pwd=pass;
  this.email=email;
  this.nombre=nombreReal;
  this.apellidos=apellidoReal;
  this.fecha_alta= this.fecha_modificacion = new Date();  
}

public Usuario(JSONObject json){
  if (json.has(NOM_USUARIO)){
    this.usuario=(String) json.get(NOM_USUARIO);
  };
  if (json.has(PASSWORD)){
    this.pwd=(String) json.get(PASSWORD);
  };
  if (json.has(EMAIL)){
    this.email=(String) json.get(EMAIL);
  };
  if (json.has(NOM_REAL)){
    this.nombre=(String) json.get(NOM_REAL);
  };
  if (json.has(APE_REAL)){
    this.apellidos=(String) json.get(APE_REAL);
  };
  if (json.has(FECHA_ALTA)){
    this.fecha_alta= Utiles.ParseDate(json.getString(FECHA_ALTA));
  };
  if (json.has(FECHA_BAJA)){
    this.fecha_baja= Utiles.ParseDate(json.getString(FECHA_BAJA));
  };
  if (json.has(FECHA_MOD)){
    this.fecha_modificacion= Utiles.ParseDate(json.getString(FECHA_MOD));
  };
}

  public String getNomUsuario() {
    return usuario;
  }

  public String getPassword() {
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

  /*
  public Restaurante[] getRestaurantes() {
    return restaurantes;
  }
  */

  public Date getFechaAlta() {
    return fecha_alta;
  }

  public Date getFechaUltimaMod() {
    return fecha_modificacion;
  }

  public Date getFechaBaja() {
    return fecha_baja;
  }

  public void setNomUsuario(String nomUsuario) {
    this.usuario = nomUsuario;
    this.fecha_modificacion= new Date();
  }

  public void setPassword(String password) {
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
  
  /*
  public void setRestaurantes(Restaurante[] restaurantes) {
    this.restaurantes = restaurantes;
    this.fechaUltimaMod= new Date();
  }
  */

  public void setFechaUltimaMod(Date fechaUltimaMod) {
    this.fecha_modificacion = fechaUltimaMod;    
  }

  public void setFechaBaja(Date fechaBaja) {
    this.fecha_baja = fechaBaja;
    this.fecha_modificacion= new Date();
  }
  
  /**
   * Método que convierte este usuario en un objeto JSONObject para poder trabajar con él como si fuese un JSON
   * 
   * @return un objeto JSONObject con todos los parámetros del usuario.
   */
  public JSONObject parseJSON(){
  JSONObject json = new JSONObject();
  json.put(NOM_USUARIO, this.usuario);
  json.put(EMAIL, this.email);
  json.put(PASSWORD, this.pwd);
  json.put(NOM_REAL, this.nombre);
  json.put(APE_REAL, this.apellidos);      
  json.put(FECHA_ALTA, Utiles.ParseDate(this.fecha_alta));
  json.put(FECHA_MOD, Utiles.ParseDate(this.fecha_modificacion));
  if (this.fecha_baja!=null){
    json.put(FECHA_BAJA, Utiles.ParseDate(this.fecha_baja));
  }    
  return json;
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
