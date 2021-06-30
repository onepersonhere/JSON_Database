package server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private final static String ADDRESS = "127.0.0.1";
    private final static int PORT = 23456;
    public static ServerSocket server;
    public static void main(String[] args) {
        server();
    }
    private static void server(){
        try(ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))){
            Main.server = server;
            System.out.println("Server started!");
            while(true){
                Session session = new Session(server.accept());
                session.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
