package com.volley.query_builder;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class SqlQuery {


    public SqlQuery() {
    }

    public static SqlQuery getInstance() {
        return new SqlQuery();
    }

    public String insert(String table, Object object) {

        String sql = "";
        StringBuilder keyBuilder;
        StringBuilder valueBuilder;

        Gson gson = new Gson();
        String json = gson.toJson(object, object.getClass());
        JSONObject jsonObject = null;

        try {

            keyBuilder = new StringBuilder();
            valueBuilder = new StringBuilder();

            boolean first = true;

            jsonObject = new JSONObject(json);

            Iterator<?> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = jsonObject.getString(key);

                if (first) {
                    first = false;
                } else {
                    keyBuilder.append(",");
                    valueBuilder.append(",");
                }


                keyBuilder.append(key);

                valueBuilder.append("'");
                valueBuilder.append(value);
                valueBuilder.append("'");

            }
            sql = "INSERT INTO " + table + " (" + keyBuilder.toString() + ") VALUES (" + valueBuilder.toString() + ")";


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return sql;
    }

    public String update(String table, Object object, String whereClause) {

        String sql = "";

        StringBuilder sb = new StringBuilder();

        boolean first = true;

        Gson gson = new Gson();
        String json = gson.toJson(object, object.getClass());

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            Iterator<?> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = jsonObject.getString(key);

                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }

                sb.append(key);
                sb.append(" = ");
                sb.append("'");
                sb.append(value);
                sb.append("'");

            }
            if (whereClause == null) {
                sql = "UPDATE " + table + " SET " + sb.toString() + "";
            } else {
                sql = "UPDATE " + table + " SET " + sb.toString() + " WHERE " + whereClause + "";
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sql;
    }

    public String delete(String table, String whereClause) {
        String sql = "";

        if (whereClause == null) {
            sql = "DELETE FROM " + table + "";
        } else {
            sql = "DELETE FROM " + table + " WHERE " + whereClause + "";
        }

        return sql;
    }

    public String select(String table, String selection, String whereClause) {

        String sql = "";

        if (whereClause == null) {
            sql = "SELECT " + selection + " FROM " + table;
        } else {
            sql = "SELECT " + selection + " FROM " + table + " WHERE " + whereClause;
        }

        return sql;
    }
}
