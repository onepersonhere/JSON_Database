package server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session extends Thread{
    private final Socket socket;

    public Session(Socket socketForClient){
        this.socket = socketForClient;
    }

    public void run(){
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        ){
            //received
            String msg = input.readUTF();
            System.out.println("Received: " + msg);
            //processing
            String newmsg = commandHandler(msg);
            //sent
            System.out.println("Sent: " + newmsg);
            output.writeUTF(newmsg);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String commandHandler(String inputs){
        Gson gson = new Gson();
        CommandHandler commands = gson.fromJson(inputs, CommandHandler.class);
        return commands.handleCommands();
    }
}
