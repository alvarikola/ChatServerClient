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
            
            // Lectura de la bienvenida y solicitud de nombre de usuario
            String mensaje = input.readLine();  // "Bienvenidos a Jurassic Chat"
            System.out.println(mensaje);
            
            mensaje = input.readLine();  // "Identificate humano: "
            System.out.print(mensaje);
            
            // Enviar el nombre de usuario al servidor
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            String username = consoleInput.readLine();
            output.println(username);
            
            // Crear un hilo para recibir mensajes del servidor
            Thread receiveThread = new Thread(new ReceiveMessages(input));
            receiveThread.start();
            
            // Enviar mensajes al servidor
            String message;
            while (true) {
                message = consoleInput.readLine();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                output.println(message);
            }
            
            // Cerrar los streams
            input.close();
            output.close();
            conexion.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // Clase interna para recibir mensajes del servidor
    static class ReceiveMessages implements Runnable {
        private BufferedReader input;
        
        ReceiveMessages(BufferedReader input) {
            this.input = input;
        }
        
        @Override
        public void run() {
            try {
                String serverMessage;
                while ((serverMessage = input.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
