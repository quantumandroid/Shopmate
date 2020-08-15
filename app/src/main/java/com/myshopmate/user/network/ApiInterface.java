package com.myshopmate.user.network;

import com.myshopmate.user.ModelClass.ForgotEmailModel;
import com.myshopmate.user.ModelClass.NotifyModelUser;
import com.myshopmate.user.ModelClass.PaymentVia;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("forgot_password")
    @FormUrlEncoded
    Call<ForgotEmailModel> getEmailOtp(@Field("user_email") String user_email,@Field("user_phone") String user_phone);

    @GET("checkotponoff")
    Call<ForgotEmailModel> getOtpOnOffStatus();

    @GET("pymnt_via")
    Call<PaymentVia> getPaymentVia();

    @POST("notifyby")
    @FormUrlEncoded
    Call<NotifyModelUser> getNotifyUser(@Field("user_id") String user_id);

}
