package ioc.tukartaserver.model;

import com.google.gson.Gson;

/**
 *
 * @author Manu
 */
public class MensajeSolicitud implements Mensaje{
private String peticion;
private String token;
private String data;
private static Gson gson = new Gson();


public MensajeSolicitud(){
  this.peticion="";
  this.data=null;
}

public MensajeSolicitud(String funcion, String objetoJson, String token){
  this.peticion=funcion;
  this.data=objetoJson;
  this.token= token;
}

public MensajeSolicitud(String function, Object data, TokenSesion token){
  this.peticion=function;
  this.data = gson.toJson(data);
  this.token=gson.toJson(token);
}

  public String getPeticion() {
    return peticion;
  }

  public String getData() {
    return data;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setPeticion(String peticion) {
    this.peticion = peticion;
  }

  public void setData(String data) {
    this.data = data;
  }
  
  @Override
  public String toString(){
    StringBuilder builder = new StringBuilder();
    builder.append("FUNCIÓN: "+this.peticion);
    builder.append("\nOBJETO: "+this.data);
    
    return builder.toString();
  }


}
