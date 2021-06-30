package client;

import com.beust.jcommander.JCommander;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;

public class Main {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;
    static ReadWriteLock lock = new ReentrantReadWriteLock();
    static Lock readLock = lock.readLock();

    public static void main(String[] args) {
        System.out.println("Client started!");
        Args arg = new Args();
        JCommander.newBuilder().addObject(arg).build().parse(args);
        String cmd;

        if(!arg.getFilename().equals("")){
            String filename = arg.getFilename();
            cmd = readFile(filename);
        }else{
            cmd = requestProcessor(arg.getRequest(), arg);
        }

        //cmd = readFile("test.json");
        connectHost(cmd);
    }
    private static String readFile(String filename){
        String data = "";
        try {
            readLock.lock();
            File myObj = new File("C:\\Users\\wh\\IdeaProjects\\JSON Database1\\JSON Database\\task\\src\\client\\data\\" + filename);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            readLock.unlock();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }
    private static String requestProcessor(String request, Args args){
        Map<String, String> map = new HashMap<>();
        if(request.equals("get")){
            map.put("type", "get");
            map.put("key", args.getKey());
        }
        if(request.equals("set")){
            map.put("type", "set");
            map.put("key", args.getKey());
            map.put("value", args.getValue());
        }
        if(request.equals("delete")){
            map.put("type", "delete");
            map.put("key", args.getKey());
        }
        if(request.equals("exit")){
            map.put("type", "exit");
        }
        Gson gson = new Gson();
        return gson.toJson(map);
    }
    private static void connectHost(String cmd){
        try(
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                ) {
            //sent
            System.out.println("Sent: " + cmd);
            //received
            output.writeUTF(cmd);
            String receivedMsg = input.readUTF();
            System.out.println("Received: "+receivedMsg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
