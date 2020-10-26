package ioc.tukartaserver.pruebas;

/**
 *
 * @author Manu
 */


import java.io.IOException;

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
      }

    }
}