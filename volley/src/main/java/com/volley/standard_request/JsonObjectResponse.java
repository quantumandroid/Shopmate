package com.volley.standard_request;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface JsonObjectResponse {

    public void onResponse(JSONObject response);

    public void onErrorResponse(VolleyError error);
}
