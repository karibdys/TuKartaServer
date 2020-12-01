/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.pruebas;

import com.google.gson.Gson;
import ioc.tukartaserver.gestorDB.GestorDB;
import ioc.tukartaserver.model.Alergeno;
import ioc.tukartaserver.model.Empleado;
import ioc.tukartaserver.model.Estado;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.Mesa;
import ioc.tukartaserver.model.Usuario;
import ioc.tukartaserver.model.Pedido;
import ioc.tukartaserver.model.Producto;
import ioc.tukartaserver.model.Rol;
import ioc.tukartaserver.model.TokenSesion;
import ioc.tukartaserver.model.Utiles;
import ioc.tukartaserver.security.GestorCrypto;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Manu
 */
public class pruebas {
private static final String CLASE = "org.postgresql.Driver";
private static final String LOCAL_URL = "jdbc:postgresql://localhost:5432/TuKarta";    
private static final String USER = "tukarta";
private static final String PASS = "TuKartaP4$$"; 
public static final String LIST_PEDIDO_FROM_USER = "SELECT* from PEDIDO WHERE empleado = ? AND activo = true";
public static Gson gson = new Gson();


public static void main (String... args)  {   
 GestorCrypto crypto = null;
 PublicKey pKey=null;
 String keyJSON=null;
 PublicKey keyRec = null;
  try{
    crypto = new GestorCrypto();
 }catch (Exception ex){   
   log(ex.getMessage());
 }
 if (crypto!=null){
   pKey = crypto.getPublicKey();
   
   log("Clave bruta: \n"+pKey.toString());
   BigInteger [] datos = new BigInteger[2];
 
   KeyFactory fact=null;
   try {
     fact = KeyFactory.getInstance("RSA");
     RSAPublicKeySpec pub = fact.getKeySpec(pKey ,RSAPublicKeySpec.class);   
     datos[0]= pub.getModulus();
     datos[1]= pub.getPublicExponent();
   } catch (NoSuchAlgorithmException ex) {
     Logger.getLogger(pruebas.class.getName()).log(Level.SEVERE, null, ex);
   } catch (InvalidKeySpecException ex) {
     Logger.getLogger(pruebas.class.getName()).log(Level.SEVERE, null, ex);
   }
   
   keyJSON = gson.toJson(datos);
   log("Datos en JSON: \n"+keyJSON);
   //volvemos atrás
   BigInteger[] datosRec = gson.fromJson(keyJSON, BigInteger[].class);
   RSAPublicKeySpec spec = new RSAPublicKeySpec(datosRec[0], datosRec[1]);
   try {
     keyRec = fact.generatePublic(spec);
   } catch (InvalidKeySpecException ex) {
     log("error al desencriptar");
   }
   log("Clave pública recuperada: \n"+keyRec);
 }
}

public static void log(String text){
  System.out.println(text);
}
  
}
