package server;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private String type = "";
    private Object key = "";
    private Object value = "";
    public CommandHandler(String type, Object key, Object value){
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public CommandHandler(String type, Object key){
        this.type = type;
        this.key = key;
    }

    public CommandHandler(String type){
        this.type = type;
    }
    public String handleCommands(){
        String response = "";
        String reason = "No such key";
        Database db = new Database();
        if(type.equals("get")){
            value = db.get(key);
            if (value.equals("ERROR")) {
                response = "ERROR";
            } else {
                response = "OK";
            }
        }
        if(type.equals("set")){
            response = db.set(value, key);
        }
        if(type.equals("delete")){
            response = db.delete(key);
        }
        if(type.equals("exit")){
            response = "OK";
            try {
                Main.server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("response", response);
        if(response.equals("ERROR")){
            map.put("reason", reason);
        }
        if(type.equals("get") && response.equals("OK")){
            map.put("value", value);
        }

        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
