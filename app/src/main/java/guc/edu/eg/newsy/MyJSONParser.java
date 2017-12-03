package guc.edu.eg.newsy;

/**
 * Created by alaa on 12/1/17.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyJSONParser {
    protected String type;
    protected String message;
    protected JSONArray jsonArray;


    public MyJSONParser(JSONObject jsonObject) throws JSONException {
        String message = jsonObject.getString("message");
        try {
            JSONArray jsonArray = new JSONArray(message);
            type = "json";
            this.jsonArray  = jsonArray;
            System.out.println("MESSAGE = " + message);
            System.out.println("JSON ARRAY = " + jsonArray);
            parseJSON();
        } catch (JSONException e) {
            type = "string";
            this.message = message;
            System.out.println("MESSAGE: " + message);
        }


    }

    protected void parseJSON() throws JSONException {
        System.out.println("JSON: ");
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = (JSONObject) jsonArray.getJSONObject(i);
            System.out.println(obj.get("title"));
        }

    }

    protected String getType(){
        return this.type;
    }
    protected String getMessage(){
        return this.message;
    }
    protected JSONArray getJsonArray(){
        return this.jsonArray;
    }
}