package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {
    private HashMap<Object, Object> map;
    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();

    Database(){
        map = new HashMap<>();
    }

    public String set(Object text, Object key){
        readFromFile();
        //check key -> [] if it does iterate all subnodes of value in object
        if (key.toString().contains("[")) {
            List<String> keyArr = (List<String>) key;

            if(map.containsKey(keyArr.get(0))) {
                Object value = map.get(keyArr.get(0));
                //find the nested json
                LinkedTreeMap<Object, Object> mp = new LinkedTreeMap<>();
                for (int i = 1; i < keyArr.size(); i++) {
                    mp = (LinkedTreeMap<Object, Object>) value; //reduce map size by 1
                    Object k = keyArr.get(i);

                    if (mp.containsKey(k)) {
                        value = mp.get(k);
                        if (i == keyArr.size() - 1) {
                            mp.put(k, text);
                            writeToFile();
                            return "OK";
                        }
                    } else {
                        return "ERROR";
                    }
                }
            }
        } else {
            map.put(key, text);
            writeToFile();
            return "OK";
        }
        return "ERROR";
    }

    public Object get(Object key){
        readFromFile();
        Object returnObj = "ERROR";
        if(key.toString().contains("[")){
            returnObj = getNested(key);
        }else{
            returnObj = map.getOrDefault(key, "ERROR");
        }
        //map.forEach((k, value) -> System.out.println(k + " " + value));

        return returnObj;
    }

    public String delete(Object key) {
        readFromFile();

        if (key.toString().contains("[")) {
            List<String> keyArr = (List<String>) key;

            if(map.containsKey(keyArr.get(0))) {
                Object value = map.get(keyArr.get(0));
                //find the nested json
                LinkedTreeMap<Object, Object> mp = new LinkedTreeMap<>();
                for (int i = 1; i < keyArr.size(); i++) {
                    mp = (LinkedTreeMap<Object, Object>) value; //reduce map size by 1
                    Object k = keyArr.get(i);

                    if (mp.containsKey(k)) {
                        value = mp.get(k);
                        if (i == keyArr.size() - 1) {
                            mp.remove(k);
                            writeToFile();
                            return "OK";
                        }
                    } else {
                        return "ERROR";
                    }
                }
            }
        } else {
            if (map.containsKey(key)) {
                map.remove(key);
                writeToFile();
                return "OK";
            }
        }
        return "ERROR";
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
        String data = null;
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
                data, new TypeToken<HashMap<Object, Object>>() {}.getType()
        );
    }

    private Object getNested(Object key){
        //key is an ArrayList
        List<String> keyArr = (List<String>) key;
        Object value = map.get(keyArr.get(0));
        //find the nested json
        LinkedTreeMap<Object, Object> mp = new LinkedTreeMap<>();
        for(int i = 1; i < keyArr.size(); i++){
            mp = (LinkedTreeMap<Object, Object>) value;
            value = mp.get(keyArr.get(i));
        }
        return value;
    }
}
