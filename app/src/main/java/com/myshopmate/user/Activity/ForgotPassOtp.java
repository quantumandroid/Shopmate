package com.myshopmate.user.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.myshopmate.user.Config.BaseURL;
import com.myshopmate.user.ModelClass.ForgotEmailModel;
import com.myshopmate.user.ModelClass.VerifyOtp;
import com.myshopmate.user.R;
import com.myshopmate.user.network.ApiInterface;
import com.myshopmate.user.util.AppController;
import com.myshopmate.user.util.CustomVolleyJsonRequest;
import com.myshopmate.user.util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


public class ForgotPassOtp extends AppCompatActivity {
    public static String TAG = "Login";
    EditText et_req_mobile;
    CardView cvverify;
    CardView cv_email;
    ProgressDialog progressDialog;
    Button verify;
    TextView edit, txt_mobile;
    LinearLayout ll_edit;
    EditText et_otp;
    EditText et_mail;
    String MobileNO;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Session_management session_management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_otp);
        et_req_mobile = findViewById(R.id.et_req_mobile);
        cvverify = findViewById(R.id.cvverify);
        cv_email = findViewById(R.id.cv_email);
        et_mail = findViewById(R.id.et_mail);

        session_management = new Session_management(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        checkOtpStatus();
        if (session_management.getOtpSatus().equalsIgnoreCase("0")) {
            cv_email.setVisibility(View.VISIBLE);
        } else {
            cv_email.setVisibility(View.GONE);
        }
        cvverify.setVisibility(View.VISIBLE);

        cvverify.setEnabled(true);
        cvverify.setOnClickListener(v -> checkNumber(et_req_mobile.getText().toString().trim()));
        /*cvverify.setOnClickListener(v -> {
            cvverify.setEnabled(true);

            if (session_management.getOtpSatus().equalsIgnoreCase("0")) {
                if (et_req_mobile.getText().toString().length() != 10 || et_req_mobile.getText().toString().contains("+")) {
                    et_req_mobile.setError("Enter valid mobile number");
                } else {
                    if (et_mail.getText().toString() != null && et_mail.getText().toString().matches(emailPattern) && et_mail.getText().toString().length() > 0) {
                        progressDialog.show();
                        makeotpRequest(et_req_mobile.getText().toString(), et_mail.getText().toString());
                    } else {
                        et_req_mobile.setError("Enter valid email address!");
                    }
                }

            } else {
                if (et_req_mobile.getText().toString().length() != 10 || et_req_mobile.getText().toString().contains("+")) {
                    et_req_mobile.setError("Enter valid mobile number");
                } else {
                    progressDialog.show();
                    makeRegisterRequest();

                }
            }
        });*/
    }

    private void checkNumber(String phoneNumber) {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<VerifyOtp> checkOtpStatus = apiInterface.checkNumIsRegisterOrNot(phoneNumber);
        checkOtpStatus.enqueue(new Callback<VerifyOtp>() {
            @Override
            public void onResponse(@NonNull Call<VerifyOtp> call, @NonNull retrofit2.Response<VerifyOtp> response) {
                if (response.isSuccessful()) {
                    VerifyOtp model = response.body();
                    if (model != null) {
                        boolean otpOn = model.getStatus().equalsIgnoreCase("1");
                        if (otpOn) {
                            if (phoneNumber.length() != 10 || phoneNumber.contains("+")) {
                                et_req_mobile.setError("Enter valid mobile number");
                            } else {
                                Intent intent = new Intent(ForgotPassOtp.this, Forget_otp_verify.class);
                                intent.putExtra("user_phone", phoneNumber);
                                intent.putExtra("firebase", true);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(ForgotPassOtp.this, "Number not Register!..", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                cvverify.setEnabled(true);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<VerifyOtp> call, @NonNull Throwable t) {
                cvverify.setEnabled(true);
                progressDialog.dismiss();
                t.printStackTrace();
            }
        });
    }

    private void makeRegisterRequest() {

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_phone", et_req_mobile.getText().toString());


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.forget_password, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        cvverify.setEnabled(false);
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPassOtp.this, message, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ForgotPassOtp.this, Forget_otp_verify.class);
                        intent.putExtra("user_phone", et_req_mobile.getText().toString());
                        startActivity(intent);

                    } else {
                        cvverify.setEnabled(true);
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPassOtp.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    cvverify.setEnabled(true);
//                    Toast.makeText(ForgotPassOtp.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void makeotpRequest(String user_phone, String user_email) {


        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);
        Call<ForgotEmailModel> forgotEmailModelCall = apiInterface.getEmailOtp(user_email, user_phone);
        forgotEmailModelCall.enqueue(new Callback<ForgotEmailModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgotEmailModel> call, @NonNull retrofit2.Response<ForgotEmailModel> response) {

                if (response.isSuccessful()) {
                    ForgotEmailModel model = response.body();
                    if (model != null) {
                        if (model.getStatus().equalsIgnoreCase("1")) {
                            Toast.makeText(ForgotPassOtp.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPassOtp.this, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ForgotPassOtp.this, "Please try again!", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(ForgotPassOtp.this, "Please try again!", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NonNull Call<ForgotEmailModel> call, @NonNull Throwable t) {
                Toast.makeText(ForgotPassOtp.this, "Please try again!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void checkOtpStatus() {
        progressDialog.show();
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<ForgotEmailModel> checkOtpStatus = apiInterface.getOtpOnOffStatus();
        checkOtpStatus.enqueue(new Callback<ForgotEmailModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgotEmailModel> call, @NonNull retrofit2.Response<ForgotEmailModel> response) {
                if (response.isSuccessful()) {
                    ForgotEmailModel model = response.body();
                    if (model != null) {
                        if (model.getStatus().equalsIgnoreCase("0")) {
                            session_management.setOtpStatus("0");
                            cv_email.setVisibility(View.VISIBLE);
                            cvverify.setVisibility(View.VISIBLE);
                        } else {
                            session_management.setOtpStatus("1");
                            cv_email.setVisibility(View.GONE);
                            cvverify.setVisibility(View.VISIBLE);
                        }
                    }

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ForgotEmailModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });

    }
}