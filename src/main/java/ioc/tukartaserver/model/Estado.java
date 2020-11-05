package ioc.tukartaserver.model;

/**
 * 
 * @author Manu Mora
 */
public enum Estado {
  NO_INICIADO ("No iniciado"),
  EN_PREPARACIÓN ("En preparación"),
  PREPARADO ("Listo");
  
  private String estado;
  private Estado (String estado){
    this.estado = estado;
  }
  
  public String getEstado(){
    return this.estado;
  }
  
}
