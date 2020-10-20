package ioc.tukartaserver.model;

/**
 *
 * @author Manu
 */
public class MensajeSolicitud implements Mensaje{
private String peticion;
private String data;

public MensajeSolicitud(){
  this.peticion="";
  this.data=null;
}

public MensajeSolicitud(String funcion, String objetoJson){
  this.peticion=funcion;
  this.data=objetoJson;
}

  public String getPeticion() {
    return peticion;
  }

  public String getData() {
    return data;
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
