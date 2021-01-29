package com.myshopmate.user.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.myshopmate.user.Config.ConfigData;
import com.myshopmate.user.R;
import com.myshopmate.user.util.Session_management;
import com.volley.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.myshopmate.user.Config.BaseURL.MY_BASE_URL;
import static com.myshopmate.user.Config.BaseURL.USERBLOCKAPI;

public class Splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 500;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    SharedPreferences sharedPreferences;
    Session_management session_management;
    public static ConfigData configData = new ConfigData();
    private LocationManager manager;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progress_bar = findViewById(R.id.progress_bar);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Config.SIMPLE_REQUEST_URL = MY_BASE_URL;
        setFinishOnTouchOutside(true);
        session_management = new Session_management(Splash.this);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       /* if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            //Toast.makeText(getApplicationContext(), "Gps already enabled", Toast.LENGTH_SHORT).show();
            redirectionScreen();
        } else {
            if (!hasGPSDevice(this)) {
                Toast.makeText(getApplicationContext(), "Gps not Supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
                Toast.makeText(getApplicationContext(), "Gps not enabled", Toast.LENGTH_SHORT).show();
                enableLoc();
            }
        }*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    progress_bar.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },1500);
        getConfigData();
    }

    private void getConfigData() {
        RequestQueue queue;
        String URL = "https://www.myshopmate.in/user_app_config_data.json";

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                 configData = gson.fromJson(response, ConfigData.class);
                 String version = null;
                try {
                     version = String.valueOf(getPackageManager().getPackageInfo(getPackageName(),0).versionCode);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                //if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(Splash.this)) {
                    if (version != null && Integer.parseInt(version) < configData.getApp_version()) {
                        if ((configData.getApp_version() - Integer.parseInt(version)) > 1 || configData.getApp_update().equals("1")){
                            showUpdateDialog("Please update the app to proceed further.",true);
                        } else {
                            showUpdateDialog("New update of this app is available.",false);
                        }
                    } else {
                        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(Splash.this)) {
                            //Toast.makeText(getApplicationContext(), "Gps already enabled", Toast.LENGTH_SHORT).show();
                            redirectionScreen();
                        } else {
                            if (!hasGPSDevice(Splash.this)) {
                                Toast.makeText(getApplicationContext(), "Gps not Supported", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(Splash.this)) {
                                Toast.makeText(getApplicationContext(), "Gps not enabled", Toast.LENGTH_SHORT).show();
                                enableLoc();
                            }
                        }
                        fetchBlockStatus();
                    }
                //}


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
                Toast.makeText(Splash.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    private void showUpdateDialog(String msg, boolean isMandatory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
        builder.setTitle("App Update!");
        builder.setMessage(msg); //"Update app now ?"
        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }finally {
                    finishAffinity();
                }

            }
        });
        builder.setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isMandatory) {
                    finishAffinity();
                    return;
                }
                dialog.dismiss();
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(Splash.this)) {
                    //Toast.makeText(getApplicationContext(), "Gps already enabled", Toast.LENGTH_SHORT).show();
                    redirectionScreen();
                } else {
                    if (!hasGPSDevice(Splash.this)) {
                        Toast.makeText(getApplicationContext(), "Gps not Supported", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(Splash.this)) {
                        Toast.makeText(getApplicationContext(), "Gps not enabled", Toast.LENGTH_SHORT).show();
                        enableLoc();
                    }
                }
                fetchBlockStatus();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void fetchBlockStatus() {

        if (!session_management.userId().equalsIgnoreCase("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, USERBLOCKAPI, response -> {
                Log.d("adresssHoww", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("2")) {
                        session_management.setUserBlockStatus("2");
                    } else {
                        session_management.setUserBlockStatus("1");
                        session_management.setUserBlockStatusMsg(msg);
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                  //  Toast.makeText(Splash.this, "" + msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Toast.makeText(Splash.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("user_id", session_management.userId());
                    return param;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Splash.this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }

    }


    private void openDialogFunction() {

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.e("connected", "connected");

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.e("suspended", "suspended");
                        googleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                        Log.e("Location error", "Location error " + connectionResult.getErrorCode());
                    }
                }).build();


    }


    private void redirectionScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (session_management.isLoggedIn()) {
                    Intent intent1 = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                } else {
                    if (session_management.isSkip()) {
                        Intent intent1 = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent1);
                        finish();
                    } else {
                        Intent intent1 = new Intent(Splash.this, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                }

            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers.size() < 1)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Log.e("connected", "connected");
                }

                @Override
                public void onConnectionSuspended(int i) {
                    Log.e("suspended", "suspended");
                    googleApiClient.connect();
                }
            })
                    .addOnConnectionFailedListener(connectionResult -> Log.e("Location error", "Location error " + connectionResult.getErrorCode())).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(7 * 1000);  //30 * 1000
            locationRequest.setFastestInterval(5 * 1000); //5 * 1000
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(result1 -> {

                Log.e("result", "result");
                final Status status = result1.getStatus();
                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    Log.e("RESOLUTION_REQUIRED", "");
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(Splash.this, REQUEST_LOCATION);

                        // getActivity().finish();
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                        Log.e("error", "error");
                    }
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        redirectionScreen();

                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }
}

