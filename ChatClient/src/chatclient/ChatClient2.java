package chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ChatClient2 {

    public static void main(String[] args) {
        
        //Comprobacion de argumentos
        if (args.length < 2) {
            System.out.println("Error, debes indicarle el host y el puerto");
            System.exit(1);
        }
        
        // Dirección y puerto del servidor
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        
        
        try {
            // Conexión al servidor
            Socket conexion = new Socket(host, port);
            System.out.println("Conectado al servidor " + host + " en el puerto " + port + ".");
            
            // Streams para enviar y recibir mensajes
            BufferedReader input = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            PrintWriter output = new PrintWriter(conexion.getOutputStream(), true);
            BufferedReader stdInput = new BufferedReader (new InputStreamReader(System.in));
            
            // Comunicacion con el servidor
            // Envio
            boolean continuar = true;
            while (continuar) {
                String mensage = stdInput.readLine();
                System.out.println(mensage); 
                output.println(mensage);
            }
            output.flush();
            // Cerrar los streams
            input.close();
            
        } catch (IOException ex) {
            
        }
    }
    
}
