package com.volley.simple_request;

import com.volley.response.Response;

public interface OnResponseListener {
    void onSuccess(Response response);

    void onFailure(String error);
}
