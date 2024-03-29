package com.myshopmate.user.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
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
import com.myshopmate.user.util.Session_management;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class FireOtpPageAuthentication extends AppCompatActivity {

    Button verify;
    TextView txtMobile;
    TextView countryCode;
    LinearLayout llEdit;
    EditText etOtp;
    String mobileNO;
    private Session_management sessionManagement;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks changedCallbacks;
    private String mVerificationId = "";
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_otp_page_authentication);
        FirebaseApp.initializeApp(FireOtpPageAuthentication.this);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(view -> Log.i("TAG","not work"));
        countryCode = findViewById(R.id.country_code);
        show();
        sessionManagement = new Session_management(FireOtpPageAuthentication.this);
        changedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    etOtp.setText(code);
                    onCodeSents(code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(FireOtpPageAuthentication.this, "Verification failed!", Toast.LENGTH_SHORT).show();
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                e.printStackTrace();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                mVerificationId = s;
            }
        };
        init();
    }

    private void show() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void getCountryCode() {
        sessionManagement.setCountryCode("91");
        countryCode.setText("+" + sessionManagement.getCountryCode());
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder()
                .setPhoneNumber("+91"+mobileNO)
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





    private void init() {
        txtMobile = findViewById(R.id.txt_mobile);
        etOtp = findViewById(R.id.et_otp);

        verify = findViewById(R.id.btnVerify);
        llEdit = findViewById(R.id.ll_edit);

        mobileNO = getIntent().getStringExtra("MobNo");
        txtMobile.setText(mobileNO);

        verify.setOnClickListener(v -> {
            if (etOtp.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "OTP required!", Toast.LENGTH_SHORT).show();
            } else if (!isOnline()) {
                Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
            } else {
                show();
                onCodeSents(etOtp.getText().toString().trim());
            }
        });
        /*llEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
            finish();
        });*/

        getCountryCode();

    }

    private void onCodeSents(String code) {
        if (mVerificationId!=null && !mVerificationId.equalsIgnoreCase("")){
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }else {
            show();
            Toast.makeText(FireOtpPageAuthentication.this, "Please try again later!", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            try {
                if (task.isSuccessful()) {
                    updateStatusLogin("success", mobileNO);
                } else {
                    updateStatusLogin("failed", mobileNO);
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
                    sessionManagement.setLogin(true);
                    sessionManagement.setIsVerified("1");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(FireOtpPageAuthentication.this, "Verification failed!", Toast.LENGTH_SHORT).show();
                }
                FirebaseAuth.getInstance().signOut();
                show();
            }

            @Override
            public void onFailure(@NonNull Call<VerifyOtp> call, @NonNull Throwable t) {
                FirebaseAuth.getInstance().signOut();
                show();
                t.printStackTrace();
            }
        });

    }


    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            return true;
        }
    }
}