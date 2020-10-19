package com.volley.standard_request;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface JsonArrayResponse {

    public void onResponse(JSONArray response);

    public void onErrorResponse(VolleyError error);
}
