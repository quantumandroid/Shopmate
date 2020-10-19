package com.volley.standard_request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.volley.config.VolleyClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class StandardRequest {

    public static final String TAG = StandardRequest.class.getSimpleName();

    public static StandardRequest mInstance;

    private Context context;

    public StandardRequest(Context context) {

        this.context = context;

    }

    public static StandardRequest getInstance(Context context) {

        if (mInstance == null) {

            mInstance = new StandardRequest(context);

        }

        return mInstance;

    }

    public void string(int method, String url, final StringResponse listener, final Map<String, String> params, String tag) {

        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        listener.onErrorResponse(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                return params;
            }
        };

        VolleyClient.getInstance(context).addToRequestQueue(stringRequest, tag.isEmpty() ? TAG : tag);

    }

    public void jsonObject(int method, String url, JSONObject jsonObject, final JsonObjectResponse listener, final Map<String, String> params, String tag) {


        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(method, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        listener.onResponse(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorResponse(error);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        return params;
                    }

                };

        VolleyClient.getInstance(context).addToRequestQueue(jsonObjectRequest, tag.isEmpty() ? TAG : tag);
    }

    public void jsonArray(int method, String url, JSONArray jsonArray, final JsonArrayResponse listener, final Map<String, String> params, String tag) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(method, url, jsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                listener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorResponse(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }

        };

        VolleyClient.getInstance(context).addToRequestQueue(jsonArrayRequest, tag.isEmpty() ? TAG : tag);
    }

}
