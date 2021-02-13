package com.myshopmate.user.network;

import com.myshopmate.user.ModelClass.CountryCodeModel;
import com.myshopmate.user.ModelClass.FirebaseStatusModel;
import com.myshopmate.user.ModelClass.ForgotEmailModel;
import com.myshopmate.user.ModelClass.NotifyModelUser;
import com.myshopmate.user.ModelClass.PaymentVia;
import com.myshopmate.user.ModelClass.PopularCategoryResponse;
import com.myshopmate.user.ModelClass.VerifyOtp;

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

    @GET("firebase")
    Call<FirebaseStatusModel> getFirebaseOtpStatus();

    @GET("countrycode")
    Call<CountryCodeModel> getCountryCode();

    @POST("firebase_otp_ver")
    @FormUrlEncoded
    Call<VerifyOtp> getVerifyOtpStatus(@Field("status") String status , @Field("user_phone") String userPhone);

    @POST("checknum")
    @FormUrlEncoded
    Call<VerifyOtp> checkNumIsRegisterOrNot(@Field("user_phone") String userPhone);

    @GET("popular_category_products")
    Call<PopularCategoryResponse> getPopularCategoryProducts();
}
