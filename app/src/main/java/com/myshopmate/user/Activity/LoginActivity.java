package com.myshopmate.user.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.myshopmate.user.Config.BaseURL;
import com.myshopmate.user.ModelClass.ForgotEmailModel;
import com.myshopmate.user.ModelClass.NotifyModelUser;
import com.myshopmate.user.R;
import com.myshopmate.user.network.ApiInterface;
import com.myshopmate.user.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.myshopmate.user.Config.BaseURL.Login;

public class LoginActivity extends AppCompatActivity {

    Button SignIn;
    TextView forgotPAss, btnignUp;
    EditText etMob, etPass;
    String androidID, token;
    ProgressDialog progressDialog;
    LinearLayout skip;
    LinearLayout flag_view;
    TextView country_c;
    private Session_management sessionManagement;
    private int countryFlag = -1;
    private String countryCode = "";

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        newBase = LocaleHelper.onAttach(newBase);
//        super.attachBaseContext(newBase);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        token = FirebaseInstanceId.getInstance().getToken();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        String s = task.getException().getMessage();
                        Toast.makeText(this, s, Toast.LENGTH_LONG).show();

                        token = "";
                        Log.i("Login", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    token = task.getResult().getToken();
                    if (token == null){
                        Toast.makeText(this, "Token : null", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(this, "Token : " + token, Toast.LENGTH_LONG).show();
                    }

                });
        sessionManagement = new Session_management(LoginActivity.this);
        new Thread(this::checkUserNotify).start();
        init();
    }

    private void init() {

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);


        etMob = findViewById(R.id.etMob);
        etPass = findViewById(R.id.etPass);
        SignIn = findViewById(R.id.btn_Login);
        flag_view = findViewById(R.id.flag_view);
        country_c = findViewById(R.id.country_c);
        forgotPAss = findViewById(R.id.btn_ForgotPass);
        btnignUp = findViewById(R.id.btn_Signup);
        skip = findViewById(R.id.skip);

//        country_c.setText("");

        checkOtpStatus();

        flag_view.setOnClickListener(view -> startActivityForResult(new Intent(LoginActivity.this, FlagActivity.class), 15));

        skip.setOnClickListener(v -> {
            sessionManagement.createLoginSession("", "", "", "", "", true);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        forgotPAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassOtp.class);
                startActivity(intent);

            }
        });
        btnignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SignIn.setOnClickListener(v -> {
            if (etMob.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Mobile Number required!", Toast.LENGTH_SHORT).show();
            } else if (etMob.getText().toString().trim().length() < 9) {
                Toast.makeText(getApplicationContext(), "Valid Mobile Number required!", Toast.LENGTH_SHORT).show();
            } else if (etPass.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Password required!", Toast.LENGTH_SHORT).show();
            } else if (!isOnline()) {
                Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
            } else {
//                if (country_c.getText().toString() != null && !country_c.getText().toString().equalsIgnoreCase("")) {
//
//
//                } else {
//                    Toast.makeText(LoginActivity.this, "Please select a country code!..", Toast.LENGTH_SHORT).show();
//                }
                progressDialog.show();
                loginUrl();


            }
        });
    }

    private void loginUrl() {


        if (token != null && !token.equalsIgnoreCase("")) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Login, response -> {
                Log.d("Login", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);
                            String user_id = obj.getString("user_id");
                            String user_fullname = obj.getString("user_name");
                            String user_email = obj.getString("user_email");
                            String user_phone = obj.getString("user_phone");
                            String password = obj.getString("user_password");
                            String block = obj.getString("block");

                            progressDialog.dismiss();
                            SharedPreferences.Editor editor = getSharedPreferences(BaseURL.MyPrefreance, MODE_PRIVATE).edit();
                            editor.putString(BaseURL.KEY_MOBILE, user_phone);
                            editor.putString(BaseURL.KEY_PASSWORD, password);
                            editor.apply();
                            sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, password);
                            sessionManagement.setUserBlockStatus(block);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }, error -> progressDialog.dismiss()) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
//String cc = countryCode.replace("+","");
                    param.put("user_phone", etMob.getText().toString());
                    param.put("user_password", etPass.getText().toString());
                    param.put("device_id", token);
                    Log.d("ff", token);
                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        } else {

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            token = "";
                            Log.i("Login", "getInstanceId failed", task.getException());
                            Toast.makeText(this, "getInstanceId failed ", Toast.LENGTH_LONG).show();
                            if (progressDialog!=null && progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }

                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        loginUrl();
                    });
        }

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
                            sessionManagement.setOtpStatus("0");
                        } else {
                            sessionManagement.setOtpStatus("1");
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


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void checkUserNotify() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<NotifyModelUser> checkOtpStatus = apiInterface.getNotifyUser(sessionManagement.userId());

        checkOtpStatus.enqueue(new Callback<NotifyModelUser>() {
            @Override
            public void onResponse(@NonNull Call<NotifyModelUser> call, @NonNull Response<NotifyModelUser> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        NotifyModelUser modelUser = response.body();
                        if (modelUser.getStatus().equalsIgnoreCase("1")) {
                            sessionManagement.setEmailServer(modelUser.getData().getEmail());
                            sessionManagement.setUserSMSService(modelUser.getData().getSms());
                            sessionManagement.setUserInAppService(modelUser.getData().getApp());
                        }
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<NotifyModelUser> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 15) {
            if (data != null && data.getBooleanExtra("flagSelected", false)) {
                countryCode = data.getStringExtra("countrycode");
                countryFlag = data.getIntExtra("countryflag", -1);
                if (countryCode.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Please select a vaild country code!..", Toast.LENGTH_SHORT).show();
                } else {
                    country_c.setText(countryCode);
                }
            }
        }
    }
}
