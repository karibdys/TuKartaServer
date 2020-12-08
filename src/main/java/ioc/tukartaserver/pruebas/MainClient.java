package ioc.tukartaserver.pruebas;

/**
 *
 * @author Manu
 */


import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClient {
    public static void main (String [] args) {

      try {
        System.out.println("MAIN: Iniciando cliente\n");
        Cliente cli = new Cliente();        
        cli.startClient();
        System.out.println("MAIN: Cliente iniciado");
        System.out.println("MAIN: Cliente cerrado");
      } catch (IOException e) {
        System.out.println("MAIN: ERROR: "+e.getMessage());
      } catch (KeyStoreException ex) {
        Logger.getLogger(MainClient.class.getName()).log(Level.SEVERE, null, ex);
      } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(MainClient.class.getName()).log(Level.SEVERE, null, ex);
      } catch (CertificateException ex) {
        Logger.getLogger(MainClient.class.getName()).log(Level.SEVERE, null, ex);
      } catch (UnrecoverableKeyException ex) {
        Logger.getLogger(MainClient.class.getName()).log(Level.SEVERE, null, ex);
      }

    }
}