package ioc.tukartaserver.model;

import com.google.gson.Gson;

/**
 *
 * @author Manu
 */
public class MensajeRespuesta implements Mensaje{
Gson gson;
private Codes code;
private String peticion;
private String data;

public MensajeRespuesta(){
  
}

public MensajeRespuesta(Codes code, String peticion){
  this.code=code;
  this.peticion=peticion;
}

public MensajeRespuesta(Codes code, String peticion, String data){
  this.code=code;
  this.peticion=peticion;
  this.data=data;
}

public MensajeRespuesta(Codes code, String peticion, Object data){
  gson = new Gson();
  this.code=code;
  this.peticion=peticion;
  this.data = gson.toJson(data);
}



  public Codes getCode() {
    return code;
  }

  public String getPeticion() {
    return peticion;
  }

  public String getData() {
    return data;
  }

  public void setGson(Gson gson) {
    this.gson = gson;
  }

  public void setCode(Codes code) {
    this.code = code;
  }

  public void setPeticion(String peticion) {
    this.peticion = peticion;
  }

  public void setData(String data) {
    this.data = data;
  }
  
  public MensajeRespuesta generarMensajeRespuesta(){
    MensajeRespuesta mensaje = new MensajeRespuesta();    
    return  mensaje;
  }
  
  @Override
  public String toString(){
    StringBuilder builder = new StringBuilder();
    builder.append("------\nMENSAJE:");
    builder.append("\nCódigo: "+code.getCode()+"//Mensaje: "+code.getMessage());
    builder.append("\nPetición respondida: "+peticion);
    builder.append("\nDatos extras: "+data);
    builder.append("\n------");
    return builder.toString();
  }
}
