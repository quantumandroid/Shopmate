package com.volley.response;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Response {
    private String JSON_STRING;

    public Response(String json) {
        this.JSON_STRING = json;
    }

    public String getString() {
        return this.JSON_STRING;
    }

    public String getMsg() {
        String msg = "";

        try {
            JSONObject main_object = new JSONObject(this.JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            msg = jsonObject.getString("msg");
        } catch (JSONException var4) {
            var4.printStackTrace();
        }

        return msg;
    }

    public String getInsertId() {
        String id = "";

        try {
            JSONObject main_object = new JSONObject(this.JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            id = jsonObject.getString("id");
        } catch (JSONException var4) {
            var4.printStackTrace();
        }

        return id;
    }

    public String getResult() {
        String result = "";

        try {

            JSONObject main_object = new JSONObject(this.JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            result = jsonObject.toString();

        } catch (JSONException var8) {
            var8.printStackTrace();
        }

        return result;
    }

    public ArrayList<HashMap<String, String>> getArrayList() {
        ArrayList arrayList = new ArrayList();

        try {
            JSONObject main_object = new JSONObject(this.JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); ++i) {
                JSONObject jo = result.getJSONObject(i);
                HashMap<String, String> hashMap = this.jsonToMap(jo);
                arrayList.add(hashMap);
            }
        } catch (JSONException var8) {
            var8.printStackTrace();
        }

        return arrayList;
    }

    public List<JSONObject> getJsonObjects() {
        List<JSONObject> list = new ArrayList<>();

        try {
            JSONObject main_object = new JSONObject(this.JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); ++i) {
                JSONObject jo = result.getJSONObject(i);
                list.add(jo);

            }
        } catch (JSONException var8) {
            var8.printStackTrace();
        }

        return list;
    }

    public HashMap<String, String> getHashMap() {
        HashMap hashMap = new HashMap();

        try {
            JSONObject main_object = new JSONObject(this.JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject c = result.getJSONObject(0);
            hashMap = this.jsonToMap(c);
        } catch (JSONException var6) {
            var6.printStackTrace();
        }

        return hashMap;
    }

    public JSONObject getJsonObject() {
        JSONObject obj = null;

        try {
            JSONObject main_object = new JSONObject(this.JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            JSONArray result = jsonObject.getJSONArray("result");
            obj = result.getJSONObject(0);

        } catch (JSONException var6) {
            var6.printStackTrace();
        }

        return obj;
    }

    public String[] getStringArray() {
        String[] array = new String[0];

        try {
            JSONObject main_object = new JSONObject(this.JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            JSONArray result = jsonObject.getJSONArray("result");
            array = new String[result.length()];

            for (int i = 0; i < result.length(); ++i) {
                JSONObject jo = result.getJSONObject(i);
                array[i] = this.jsonToStringArray(jo);
            }
        } catch (JSONException var7) {
            var7.printStackTrace();
        }

        return array;
    }

    public HashMap<String, String> jsonToMap(JSONObject jObject) throws JSONException {
        HashMap<String, String> map = new HashMap();
        Iterator keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            map.put(key, value);
        }

        return map;
    }

    public String jsonToStringArray(JSONObject jObject) throws JSONException {
        String str = "";

        String value;
        for (Iterator keys = jObject.keys(); keys.hasNext(); str = value) {
            String key = (String) keys.next();
            value = jObject.getString(key);
        }

        return str;
    }

    public List<Object> getList(Class mClass) {

        List<Object> list = new ArrayList<>();
        Gson gson = new Gson();

        try {
            JSONObject main_object = new JSONObject(this.JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); ++i) {
                JSONObject jo = result.getJSONObject(i);
                Object object = gson.fromJson(jo.toString(), mClass);
                list.add(object);

            }
        } catch (JSONException var8) {
            var8.printStackTrace();
        }

        return list;
    }

    public Object get(Class mClass) {

        Object object = null;
        Gson gson = new Gson();

        try {
            JSONObject main_object = new JSONObject(this.JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject c = result.getJSONObject(0);
            object = gson.fromJson(c.toString(), mClass);

        } catch (JSONException var8) {
            var8.printStackTrace();
        }

        return object;
    }

}