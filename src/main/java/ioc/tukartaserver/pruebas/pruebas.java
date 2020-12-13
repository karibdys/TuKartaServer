/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.pruebas;

import com.google.gson.Gson;
import ioc.tukartaserver.gestorDB.GestorDB;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.Utiles;
import ioc.tukartaserver.security.GestorCrypto;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;



/**
 *
 * @author Manu
 */
public class pruebas {

public static final String LIST_PEDIDO_FROM_USER = "SELECT* from PEDIDO WHERE empleado = ? AND activo = true";
public static Gson gson = new Gson();


public static void main (String... args) throws ClassNotFoundException, SQLException  {  
  GestorDB gestor = new GestorDB();
  MensajeRespuesta res = gestor.informeVentasPorRestaurante("informe pruebas", "res1Marc");
  log(res.toString());
  /*
  String comprobar = "cAOjaGWjiCUcVBpMDeAjhqkRUGDiQsT9bJNTTOE0t/japVbDqxdUmJ826X21Utyc1hbEdAFmYTa+VaDCgkRxNQ==";
  String pass = "TuKartaPass";
  String salt = "tukarta@tukarta.com";
  String mensajeFinal ="";
  try {
    mensajeFinal = GestorCrypto.encriptarPass(pass, salt);
  } catch (NoSuchAlgorithmException ex) {
    log("ERROR");
  }
  log(mensajeFinal);
  log(comprobar.equals(mensajeFinal)? "WAI": "NO WAI");
    */

}

public static void log(String text){
  System.out.println(text);
}

}
