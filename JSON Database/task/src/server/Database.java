package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {
    private HashMap<String, String> map;
    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();

    Database(){
        map = new HashMap<>();
    }

    public String set(String key, String text){
        readFromFile();
        map.put(key, text);
        writeToFile();
        return "OK";
    }

    public String get(String key){
        readFromFile();
        map.forEach((k, value) -> System.out.println(k + " " + value));
        return map.getOrDefault(key, "ERROR");
    }

    public String delete(String key){
        readFromFile();
        if(map.containsKey(key)){
            map.remove(key);
            writeToFile();
            return "OK";
        }else{
            return "ERROR";
        }
    }

    private void writeToFile(){
        try {
            File myObj = new File("C:\\Users\\wh\\IdeaProjects\\JSON Database1\\JSON Database\\task\\src\\server\\data\\db.json");
            if(myObj.createNewFile()){
            }else {
                writeLock.lock();
                FileWriter myWriter = new FileWriter(myObj);
                Gson gson = new Gson();
                String str = gson.toJson(map);
                myWriter.write(str);
                myWriter.close();
                //System.out.println("Successfully wrote to the file.");
                writeLock.unlock();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void readFromFile(){
        String data = "";
        try {
            readLock.lock();
            File myObj = new File("C:\\Users\\wh\\IdeaProjects\\JSON Database1\\JSON Database\\task\\src\\server\\data\\db.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
            readLock.unlock();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        map = new Gson().fromJson(
                data, new TypeToken<HashMap<String, String>>() {}.getType()
        );
    }
}
