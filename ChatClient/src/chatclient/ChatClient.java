package chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ChatClient {

    public static void main(String[] args) {
        // Dirección y puerto del servidor
        String host = "localhost";
        int port = 9876; 
        
        try {
            // Conexión al servidor
            Socket conexion = new Socket(host, port);
            System.out.println("Conectado al servidor " + host + " en el puerto " + port + ".");
            
            // Streams para enviar y recibir mensajes
            BufferedReader input = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            PrintWriter output = new PrintWriter(conexion.getOutputStream(), true);
            
            // Comunicacion con el servidor
            // Envio
            boolean continuar = true;
            while (continuar) {
                String mensage = input.readLine();
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
