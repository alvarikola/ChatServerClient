package chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient2 {

    public static void main(String[] args) {
        
        // Comprobación de argumentos
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
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(System.in));
            
            // Lectura de la bienvenida y solicitud de nombre de usuario
            String mensaje = input.readLine();  // "Bienvenidos a Jurassic Chat"
            System.out.println(mensaje);
            
            mensaje = input.readLine();  // "Identificate humano: "
            System.out.print(mensaje);
            
            // Enviar el nombre de usuario al servidor
            String username = stdInput.readLine();
            output.println(username);
            
            // Crear un hilo para recibir mensajes del servidor
            Thread receiveThread = new Thread(new ReceiveMessages(input));
            receiveThread.start();
            
            // Enviar mensajes al servidor
            String message;
            while (true) {
                message = stdInput.readLine();
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