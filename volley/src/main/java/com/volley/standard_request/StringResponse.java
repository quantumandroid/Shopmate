package com.volley.standard_request;

import com.android.volley.VolleyError;

public interface StringResponse {

    public void onResponse(String response);

    public void onErrorResponse(VolleyError error);
}
