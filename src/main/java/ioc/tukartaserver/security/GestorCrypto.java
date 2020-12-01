package ioc.tukartaserver.security;

import static ioc.tukartaserver.pruebas.pruebas.log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que se encarga de la criotografía y la seguridad 
 * @author Manu
 */
public class GestorCrypto {

private static String KEY_STORE = "tukarta_keystore.jks";
private static String PASS_STORE = "tukartaPass";
private static String ALIAS_KEY = "tukarta";

private KeyStore keyStore;
private PrivateKey clavePrivada;
private PublicKey clavePublica;
private Certificate certificado;

/**
 * Constructor básico del gestor de seguridad y criptografía
 * @throws KeyStoreException si no se puede acceder al almacén de claves
 * @throws FileNotFoundException si el almacén de claves no existe
 * @throws IOException fallo general
 * @throws NoSuchAlgorithmException Si se indica un algoritmo erróneo
 * @throws CertificateException si se da un error en la obtención del certificado. 
 */
public GestorCrypto() throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException{
  loadKeyStore();  
}

/**
 * Método que se encarga de iniciar el gestor de criptografía, obteniendo las claves necesarias para el funcionamiento. 
 * @throws KeyStoreException si no se puede acceder al almacén de claves
 * @throws FileNotFoundException si el almacén de claves no existe
 * @throws IOException fallo general
 * @throws NoSuchAlgorithmException Si se indica un algoritmo erróneo
 * @throws CertificateException si se da un error en la obtención del certificado. 
 */
public void loadKeyStore() throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
  keyStore= KeyStore.getInstance("JKS");
  File f = new File(KEY_STORE);
  if(f.isFile()){
    FileInputStream in = new FileInputStream(f);
    keyStore.load(in, PASS_STORE.toCharArray());     	 
  }else{
    log("Error al acceder al almacén de claves");
  }
  log("Almacén iniciado. Extrayendo claves");
  if (keyStore!=null){   
    clavePrivada = (PrivateKey)keyStore.getKey("tukarta", "tukartaPass".toCharArray());
    certificado = keyStore.getCertificate("tukarta");
    clavePublica = certificado.getPublicKey();  
  }  
}

/****************
 GETTERS
****************/

/**
 * Devuelve la clave pública del servidor
 * @return PublicKey con la clave del servidor. 
 */
public PublicKey getPublicKey (){
  return clavePublica;
}

public static void log(String texto){
  System.out.println("CRYPTO: "+texto);
}

  
}
