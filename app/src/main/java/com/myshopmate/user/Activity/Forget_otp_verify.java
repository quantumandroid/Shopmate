package com.myshopmate.user.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.myshopmate.user.Config.BaseURL;
import com.myshopmate.user.ModelClass.VerifyOtp;
import com.myshopmate.user.R;
import com.myshopmate.user.network.ApiInterface;
import com.myshopmate.user.util.AppController;
import com.myshopmate.user.util.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class Forget_otp_verify extends AppCompatActivity {
    public static String TAG = "Otp";
    CardView submit;
    EditText edtotp;
    TextView number;
    String getuserphone;
    ProgressDialog progressDialog;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks changedCallbacks;
    private String mVerificationId;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_otp_verify);

        FirebaseApp.initializeApp(Forget_otp_verify.this);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login");

        edtotp = findViewById(R.id.et_otp);


        number = findViewById(R.id.txnm);
        submit = findViewById(R.id.cvLogin);
        getuserphone = getIntent().getStringExtra("user_phone");

        number.setText(getuserphone);
        setCallback();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtotp.getText().toString().isEmpty()) {
                    Toast.makeText(Forget_otp_verify.this, "Enter Otp", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
//                    setotpverify();
                    onCodeSents(edtotp.getText().toString().trim());
                }
            }
        });
        getCountryCode();
    }

    @SuppressLint("SetTextI18n")
    private void getCountryCode() {
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder()
                .setPhoneNumber("+91"+number.getText().toString().trim())
                .setCallbacks(changedCallbacks)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
      /*  PhoneAuthProvider.getInstance(firebaseAuth).verifyPhoneNumber(
                "+" + sessionManagement.getCountryCode() + mobileNO,
                60,
                TimeUnit.SECONDS,
                FireOtpPageAuthentication.this,
                changedCallbacks);*/
//        firebaseAuth.setLanguageCode(Locale.getDefault().getLanguage());

    }

    private void setCallback() {
        changedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    progressDialog.show();
                    edtotp.setText(code);
                    onCodeSents(code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(Forget_otp_verify.this, "your verification failed!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressDialog.dismiss();
                mVerificationId = s;
            }
        };
    }

    private void onCodeSents(String code) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            try {
                if (task.isSuccessful()) {
                    updateStatusLogin("success", getuserphone);
                } else {
                    updateStatusLogin("failed", getuserphone);
                    Toast.makeText(Forget_otp_verify.this, "Verification failed!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                FirebaseAuth.getInstance().signOut();
                e.printStackTrace();
            }
        });
    }


    private void updateStatusLogin(String status, String userNumber) {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<VerifyOtp> checkOtpStatus = apiInterface.getVerifyOtpStatus(status, userNumber);
        checkOtpStatus.enqueue(new Callback<VerifyOtp>() {
            @Override
            public void onResponse(@NonNull Call<VerifyOtp> call, @NonNull retrofit2.Response<VerifyOtp> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equalsIgnoreCase("1") && status.equalsIgnoreCase("success")) {
                    Intent intent = new Intent(Forget_otp_verify.this, NewPassword.class);
                    intent.putExtra("user_phone", getuserphone);
                    startActivity(intent);
                }
                FirebaseAuth.getInstance().signOut();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<VerifyOtp> call, @NonNull Throwable t) {
                FirebaseAuth.getInstance().signOut();
                progressDialog.dismiss();
                t.printStackTrace();
            }
        });

    }

    public void setotpverify() {

        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_phone", getuserphone);

        params.put("otp", edtotp.getText().toString());


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,

                BaseURL.verify_otp, params, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());


                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {


                        progressDialog.dismiss();


                        Intent intent = new Intent(Forget_otp_verify.this, NewPassword.class);
                        intent.putExtra("user_phone", getuserphone);
                        startActivity(intent);

//

                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(Forget_otp_verify.this, message, Toast.LENGTH_SHORT).show();

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
//                    Toast.makeText(Forget_otp_verify.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}