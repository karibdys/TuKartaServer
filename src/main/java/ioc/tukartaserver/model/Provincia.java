package ioc.tukartaserver.model;

/**
 *  Clase que lista las provincias españolas
 * @author Manu Mora
 */
public enum Provincia {
  ALAVA("Álava"),
  ALBACETE("Albacete"),
  ALICANTE("Alicante"),
  ALMERÍA ("Almería"),
  ASTURIAS ("Asturias"),
  ÁVILA("Avila"),
  BADAJOZ("Badajoz"),
  BARCELONA("Barcelona"),
  BURGOS ("Burgos"),
  CACERES ("Cáceres"),
  CADIZ("Cádiz"),
  CANTABRIA("Cantabria"),
  CASTELLON("Castellón"),
  CEUTA("Ceuta"),
  CIUDAD_REAL("Ciudad Real"),
  CORDOBA("Córdoba"),
  CORUNA("A Coruña"),
  CUENCA("Cuenca"),
  GIRONA("Girona"),
  GRANADA("Granada"),
  GUADALAJARA("Guadalajara"),
  GUIPUZKOA ("Guipuzkoa"),
  HUELVA("Huelva"), 
  HUESCA ("Huesca"),
  BALEARES ("Islas Baleares"),
  JAÉN("Jaen"),
  LEÓN("León"),
  LLEIDA("Lleida"),
  LUGO("Lugo"),
  MADRID("Madrid"),
  MÁLAGA("Málaga"),
  MELILLA("Mellia"),
  MURCIA ("Murcia"),
  NAVARRA ("Navarra"),
  OURENSE ("Ourense"),
  PALENCIA ("Palencia"),
  PALMAS ("Las Palmas"),
  PONTEVEDRA("Pontevedra"),
  RIOJA ("La Rioja"),
  SALAMANCA("Salamanca"),
  SEGOVIA("Segovia"),
  SEVILLA ("Sevilla"),
  SORIA("Soria"),
  TARRAGONA("Tarragona"),
  TENERIFE("Tenerife"),
  TERUEL("Teruel"),      
  TOLEDO("Toledo"),   
  VALENCIA ("Valencia"), 
  VALLADOLID ("Valladolid"),
  BIZKAIA ("Bizkaia"),
  ZAMORA("Zamora"),
  ZARAGOZA("Zaragoza");
  
  private String nombre;
        
  private Provincia (String nombre){
    this.nombre=nombre;
  }
  
  public String getNombre(){
    return this.nombre;
  }
}
