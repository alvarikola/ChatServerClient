package chatserver;

// Clase servidor

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {

    // Propiedades
    // Array dinamico para guardar los clientes conectados
    private static CopyOnWriteArrayList<ChatThread> clients = 
            new CopyOnWriteArrayList<>(); 
    
    // Metodo principal
    public static void main(String[] args) {
        int serverPort = 9876;
        
        try {
            // Iniciar el servidor
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Servidor iniciado en el puerto: " + serverPort);
            
            // Bucle aceptando clientes de chat
            while(true) {
                Socket clientSocket = serverSocket.accept();
                
                ChatThread chatThread = new ChatThread(clientSocket);
                clients.add(chatThread);
                
                new Thread(chatThread).start(); 
            }
        } catch(IOException ex) {
            System.out.println("Error de E/S al iniciar el servidor");
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    // Metodo sincronizado para broadcast
    public static synchronized void broadcast(String message, ChatThread sender) {
        for (ChatThread client: clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
    
    // Clase hilo
    static class ChatThread implements Runnable {
        // Propiedades
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;
        private String username;
        
        // Constructor
        ChatThread(Socket socket) {
            this.socket = socket;
        }
        
        // Metodo run()
        @Override
        public void run() {
            try {
                // Capturar los streams de entrada y salida
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                
                // Registrar al usuario
                output.println("Bienvenidos a Jurassic Chat");
                output.println("Identificate humano: ");
                username = input.readLine();
                
                broadcast("El usuario [" + username + "] se a unido al chat.", this);
                
                // Bucle para el manejo de mensajes del usuario
                String message;
                while ((message = input.readLine()) != null) {
                    broadcast("[" + username + "]: " + message, this);
                }
                        
            } catch(IOException ex) {
                System.out.println("Error en una conexion: " + ex.getMessage());
            } finally {
                // El usuario abandona el chat
                // Actualizar la lista de clientes
                try {
                    clients.remove(this);
                    broadcast("El usuario [" + username + "] ha abandonado el chat.", this);
                    socket.close();
                } catch(IOException ex) {
                    System.out.println("Error al cerrar una conexion" + ex.getMessage());
                }
                
            }
        }
        
        // Metodo para enviar un mensaje
        public void sendMessage(String message) {
            output.println(message);
        }
    }
}
