package ioc.tukartaserver.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Manu Mora
 */
public class Utiles {

public static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

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
  
}
