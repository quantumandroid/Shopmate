package com.volley.simple_request;

import com.volley.response.Response;

public interface OnExistListener {

    void onExist(Response response);

    void onNonExist();

    void onFailure(String error);
}
