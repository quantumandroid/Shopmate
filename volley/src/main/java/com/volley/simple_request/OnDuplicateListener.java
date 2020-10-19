package com.volley.simple_request;

import com.volley.response.Response;

public interface OnDuplicateListener {
    void onSuccess(Response response);

    void onDuplicate(Response response);

    void onFailure(String error);
}
