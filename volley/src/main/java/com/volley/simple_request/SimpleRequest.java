package com.volley.simple_request;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.volley.config.Config;
import com.volley.config.VolleyClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SimpleRequest {

    public static String TAG = "";
    public static SimpleRequest mInstance;
    public final String REQUEST = "request";
    public final String SQL = "sql";
    private Context context;

    public SimpleRequest(Context context) {
        this.context = context;
    }

    public static SimpleRequest getInstance(Context context) {

        // If Instance is null then initialize new Instance
        if (mInstance == null) {
            mInstance = new SimpleRequest(context);

        }
        // Return MySingleton new Instance
        return mInstance;

    }

    //GET, POST, PUT, DELETE
    public void get(String sql, OnResponseListener listener) {   // TODO SELECT
        send(sql, Type.SELECT, listener);
    }

    public void post(String sql, OnResponseListener listener) {  // TODO INSERT
        send(sql, Type.INSERT, listener);
    }

    public void postUnique(String selectSql, final String insertSql, final OnDuplicateListener listener) {  // TODO INSERT Unique

        checkForPost(selectSql, Type.CHECK, new OnExistListener() {
            @Override
            public void onExist(com.volley.response.Response response) {
                listener.onDuplicate(response);
            }

            @Override
            public void onNonExist() {
                sendPostUnique(insertSql, listener);
            }

            @Override
            public void onFailure(String error) {

                listener.onFailure(error);

            }
        });

    }

    private void checkForPost(final String sql, final String type, final OnExistListener listener) {


        if (listener == null) {
            Toast.makeText(context, "Response Listener is null", Toast.LENGTH_LONG).show();
            return;
        }

        if (!network(context)) {
            listener.onFailure("Please check network connection");
            return;

        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SIMPLE_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject main_object = new JSONObject(response);
                            JSONObject jsonObject = main_object.getJSONObject("output");
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            String exist = jsonObject.getString("exist");


                            if (status.equals("ok")) {
                                switch (exist) {
                                    case "true":

                                        listener.onExist(new com.volley.response.Response(response));

                                        break;
                                    case "false":

                                        listener.onNonExist();

                                        break;
                                    default:
                                        listener.onFailure(msg);
                                        break;
                                }
                            } else {
                                listener.onFailure(msg);
                            }

                        } catch (JSONException var6) {

                            var6.printStackTrace();
                            listener.onFailure(response);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        listener.onFailure(error.toString());


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(REQUEST, type);
                params.put(SQL, sql);
                return params;
            }
        };

        VolleyClient.getInstance(context).addToRequestQueue(stringRequest, SimpleRequest.TAG);


    }

    private void sendPostUnique(final String sql, final OnDuplicateListener listener) {

       /* final Progress progress = Progress.getInstance(context);
        progress.show();*/

        if (listener == null) {
            Toast.makeText(context, "Response Listener is null", Toast.LENGTH_LONG).show();
            return;
        }

        if (!network(context)) {
            listener.onFailure("Please check network connection");
            return;

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SIMPLE_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        progress.hide();

                        try {
                            JSONObject main_object = new JSONObject(response);
                            JSONObject jsonObject = main_object.getJSONObject("output");
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            if (status.equals("ok")) {
                                listener.onSuccess(new com.volley.response.Response(response));
                            } else {
                                listener.onFailure(msg);
                            }
                        } catch (JSONException var6) {
//                            progress.hide();
                            var6.printStackTrace();
                            listener.onFailure(response);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        listener.onFailure(error.toString());


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(REQUEST, Type.INSERT);
                params.put(SQL, sql);
                return params;
            }
        };

        VolleyClient.getInstance(context).addToRequestQueue(stringRequest, SimpleRequest.TAG);


    }

    public void put(String sql, OnResponseListener listener) {   // TODO UPDATE
        send(sql, Type.UPDATE, listener);
    }

    public void putUnique(String selectSql, final String insertSql, final OnDuplicateListener listener) {  // TODO INSERT Unique

        checkForPut(selectSql, Type.CHECK, new OnExistListener() {
            @Override
            public void onExist(com.volley.response.Response response) {
                listener.onDuplicate(response);
            }

            @Override
            public void onNonExist() {
                sendPutUnique(insertSql, listener);
            }

            @Override
            public void onFailure(String error) {

                listener.onFailure(error);

            }
        });

    }

    private void checkForPut(final String sql, final String type, final OnExistListener listener) {


        if (listener == null) {
            Toast.makeText(context, "Response Listener is null", Toast.LENGTH_LONG).show();
            return;
        }

        if (!network(context)) {
            listener.onFailure("Please check network connection");
            return;

        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SIMPLE_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject main_object = new JSONObject(response);
                            JSONObject jsonObject = main_object.getJSONObject("output");
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            String exist = jsonObject.getString("exist");


                            if (status.equals("ok")) {
                                switch (exist) {
                                    case "true":

                                        listener.onExist(new com.volley.response.Response(response));

                                        break;
                                    case "false":

                                        listener.onNonExist();

                                        break;
                                    default:
                                        listener.onFailure(msg);
                                        break;
                                }
                            } else {
                                listener.onFailure(msg);
                            }

                        } catch (JSONException var6) {

                            var6.printStackTrace();
                            listener.onFailure(response);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        listener.onFailure(error.toString());


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(REQUEST, type);
                params.put(SQL, sql);
                return params;
            }
        };

        VolleyClient.getInstance(context).addToRequestQueue(stringRequest, SimpleRequest.TAG);


    }

    private void sendPutUnique(final String sql, final OnDuplicateListener listener) {

       /* final Progress progress = Progress.getInstance(context);
        progress.show();*/

        if (listener == null) {
            Toast.makeText(context, "Response Listener is null", Toast.LENGTH_LONG).show();
            return;
        }

        if (!network(context)) {
            listener.onFailure("Please check network connection");
            return;

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SIMPLE_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        progress.hide();

                        try {
                            JSONObject main_object = new JSONObject(response);
                            JSONObject jsonObject = main_object.getJSONObject("output");
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            if (status.equals("ok")) {
                                listener.onSuccess(new com.volley.response.Response(response));
                            } else {
                                listener.onFailure(msg);
                            }
                        } catch (JSONException var6) {
//                            progress.hide();
                            var6.printStackTrace();
                            listener.onFailure(response);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        listener.onFailure(error.toString());


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(REQUEST, Type.UPDATE);
                params.put(SQL, sql);
                return params;
            }
        };

        VolleyClient.getInstance(context).addToRequestQueue(stringRequest, SimpleRequest.TAG);


    }

    public void delete(String sql, OnResponseListener listener) {    // TODO DELETE
        send(sql, Type.DELETE, listener);
    }

    private void send(final String sql, final String type, final OnResponseListener listener) {

       /* final Progress progress = Progress.getInstance(context);
        progress.show();*/

        if (listener == null) {
            Toast.makeText(context, "Response Listener is null", Toast.LENGTH_LONG).show();
            return;
        }

        if (!network(context)) {
            listener.onFailure("Please check network connection");
            return;

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SIMPLE_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        progress.hide();

                        try {
                            JSONObject main_object = new JSONObject(response);
                            JSONObject jsonObject = main_object.getJSONObject("output");
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            if (status.equals("ok")) {
                                listener.onSuccess(new com.volley.response.Response(response));
                            } else {
                                listener.onFailure(msg);
                            }
                        } catch (JSONException var6) {
//                            progress.hide();
                            var6.printStackTrace();
                            listener.onFailure(response);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        listener.onFailure(error.toString());


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(REQUEST, type);
                params.put(SQL, sql);
                return params;
            }
        };

        VolleyClient.getInstance(context).addToRequestQueue(stringRequest, SimpleRequest.TAG);


    }

    public void check(String sql, OnExistListener listener) {     // TODO CHECK EXIST
        checkExistence(sql, Type.CHECK, listener);
    }

    private void checkExistence(final String sql, final String type, final OnExistListener listener) {


        if (listener == null) {
            Toast.makeText(context, "Response Listener is null", Toast.LENGTH_LONG).show();
            return;
        }

        if (!network(context)) {
            listener.onFailure("Please check network connection");
            return;

        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SIMPLE_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject main_object = new JSONObject(response);
                            JSONObject jsonObject = main_object.getJSONObject("output");
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
                            String exist = jsonObject.getString("exist");


                            if (status.equals("ok")) {
                                switch (exist) {
                                    case "true":

                                        listener.onExist(new com.volley.response.Response(response));

                                        break;
                                    case "false":

                                        listener.onNonExist();

                                        break;
                                    default:
                                        listener.onFailure(msg);
                                        break;
                                }
                            } else {
                                listener.onFailure(msg);
                            }

                        } catch (JSONException var6) {

                            var6.printStackTrace();
                            listener.onFailure(response);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        listener.onFailure(error.toString());


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(REQUEST, type);
                params.put(SQL, sql);
                return params;
            }
        };

        VolleyClient.getInstance(context).addToRequestQueue(stringRequest, SimpleRequest.TAG);


    }

    private boolean network(Context context) {
        //todo  to use this you must need internet permission in manifest.xml file
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return (activeNetworkInfo != null) && (activeNetworkInfo.isConnected());

    }

    // Listener defined earlier
    public interface Type {

        public final String INSERT = "insert";
        public final String UPDATE = "update";
        public final String DELETE = "delete";
        public final String SELECT = "select";
        public final String CHECK = "check_exist";
    }

}
