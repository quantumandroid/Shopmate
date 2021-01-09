package com.myshopmate.user.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.myshopmate.user.Config.BaseURL;
import com.myshopmate.user.Fragments.CartFragment;
import com.myshopmate.user.Fragments.CategoryFragment;
import com.myshopmate.user.Fragments.Contact_Us_fragment;
import com.myshopmate.user.Fragments.Edit_profile_fragment;
import com.myshopmate.user.Fragments.HomeFragment;
import com.myshopmate.user.Fragments.NotificationFragment;
import com.myshopmate.user.Fragments.Reward_fragment;
import com.myshopmate.user.Fragments.Terms_and_Condition_fragment;
import com.myshopmate.user.Fragments.Wallet_fragment;
import com.myshopmate.user.ModelClass.ForgotEmailModel;
import com.myshopmate.user.ModelClass.NewPendingDataModel;
import com.myshopmate.user.ModelClass.NotifyModelUser;
import com.myshopmate.user.ModelClass.PaymentVia;
import com.myshopmate.user.R;
import com.myshopmate.user.network.ApiInterface;
import com.myshopmate.user.util.DatabaseHandler;
import com.myshopmate.user.util.FetchAddressTask;
import com.myshopmate.user.util.FragmentClickListner;
import com.myshopmate.user.util.Session_management;
import com.myshopmate.user.util.Utils;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.myshopmate.user.Config.BaseURL.SupportUrl;
import static com.myshopmate.user.Config.BaseURL.TermsUrl;
import static com.myshopmate.user.Config.BaseURL.USERBLOCKAPI;
import static com.myshopmate.user.Config.BaseURL.currencyApi;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, FetchAddressTask.OnTaskCompleted, SharedPreferences.OnSharedPreferenceChangeListener {
    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 3000;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 222;
    public static boolean toCart = false;
    public BottomNavigationView navigation;
    int padding = 0;
    LinearLayout My_Order, My_Reward, My_Walllet, My_Cart;
    RelativeLayout loginSignUp, aboutUs, TermsPolicy, ContactUs, share, logout;
    NavigationView navigationView;
    LinearLayout viewpa;
    TextView mTitle, username;
    Button login, signup;
    TextView totalBudgetCount;
    Toolbar toolbar;
    ImageView sliderr;
    ImageView bell;
    List<Address> addresses = new ArrayList<>();
    String latituded, longitudea, address, city, state, country, postalCode;
    double latitude = 0.0, longitude = 0.0;
    boolean canGetLocation = false;
    SharedPreferences sharedPreferences;
    //    LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            Log.e(TAG, "onLocationResult: called");
//        }
//    };
    private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private DatabaseHandler dbcart;
    private Session_management sessionManagement;
    private ImageView profile;
    private Menu nav_menu;
    private ImageView iv_profile;
    private FusedLocationProviderClient mFusedLocationClient;
    private SharedPreferences pref;
    private BottomNavigationMenuView mbottomNavigationMenuView;
    private DrawerLayout drawer;
    private TextView addres;
    private LocationRequest locationRequest;
    private Location location;
    private boolean enterInFirst = false;
    private FragmentClickListner fragmentClickListner;
    private Fragment homeFragment;
    private Geocoder geocoder;
    private PlacesClient placesClient;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.BLACK);
        }*/

        homeFragment = new HomeFragment(fragmentClickListner, navigation);
        sessionManagement = new Session_management(MainActivity.this);
        dbcart = new DatabaseHandler(this);
        pref = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(this);
        navigation = findViewById(R.id.nav_view12);
        bell = findViewById(R.id.bell);
        profile = findViewById(R.id.profile);
        addres = findViewById(R.id.address);

        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        if (checkAndRequestPermissions()) {
            getLocationRequest();
        } else {
            setSupLocation();
        }
//        mbottomNavigationMenuView = (BottomNavigationMenuView) navigation.getChildAt(0);
//        View view = mbottomNavigationMenuView.getChildAt(4);
//
//        BottomNavigationItemView itemView = (BottomNavigationItemView) view;
//
//        View cart_badge = LayoutInflater.from(this)
//                .inflate(R.layout.bottom_badge_cart, mbottomNavigationMenuView, false);
//        itemView.addView(cart_badge);
//        totalBudgetCount = (TextView) cart_badge.findViewById(R.id.totalBudgetCount);

//        int badgeCount = pref.getInt("cardqnty", 0);
        /*int badgeCount = dbcart.getCartCount();
        if (badgeCount > 0) {
            navigation.getOrCreateBadge(R.id.navigation_notifications123).setNumber(badgeCount);
//            totalBudgetCount.setVisibility(View.VISIBLE);
//            totalBudgetCount.setText(""+badgeCount);
        } else {
            navigation.removeBadge(R.id.navigation_notifications123);
//            totalBudgetCount.setVisibility(View.GONE);
        }*/

        profile.setOnClickListener(v -> {
            if (sessionManagement.isLoggedIn()) {
                Edit_profile_fragment fm = new Edit_profile_fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            } else {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });


        fragmentClickListner = new FragmentClickListner() {
            @Override
            public void onFragmentClick(boolean open) {
                if (open) {
                    navigation.setSelectedItemId(R.id.navigation_notifications123);
                    loadFragment(new CartFragment());
                }
            }

            @Override
            public void onChangeHome(boolean open) {
                DecimalFormat dFormat = new DecimalFormat("##.#######");
                LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
                double latitude = Double.parseDouble(dFormat.format(latLng.latitude));
                double longitude = Double.parseDouble(dFormat.format(latLng.longitude));
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                getAddress();
                navigation.setSelectedItemId(R.id.navigation_home);
                //loadFragment(new HomeeeFragment(fragmentClickListner));
                loadFragment(homeFragment);
            }
        };

//        addres.setOnClickListener(v -> startActivityForResult(new Intent(MainActivity.this, AddressLocationActivity.class), 22));
//        addres.setOnClickListener(v -> onAddressSearchCalled());
        addres.setOnClickListener(v -> showPlacePicker());

//        bell.setOnClickListener(v -> {
//            navigation.setSelectedItemId(R.id.navigation_notifications123);
//            loadFragment(new CartFragment());
//        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(R.string.app_name);

        /*toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());

        setSupportActionBar(toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {

            View view = toolbar.getChildAt(i);


            if (view instanceof TextView) {
                TextView textView = (TextView) view;

            }
        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        drawer = findViewById(R.id.drawer_layout);
        ImageView menuSlider = findViewById(R.id.sliderr);

        menuSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
//                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
//            applyFontToMenuItem(mi);
        }

        View headerView = navigationView.getHeaderView(0);
        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        navigationView.setNavigationItemSelectedListener(this);
        nav_menu = navigationView.getMenu();
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        viewpa = header.findViewById(R.id.viewpa);
        if (sessionManagement.isLoggedIn()) {
            viewpa.setVisibility(View.VISIBLE);
        }


        My_Order = header.findViewById(R.id.my_orders);
        My_Reward = header.findViewById(R.id.my_reward);
        My_Walllet = header.findViewById(R.id.my_wallet);
        My_Cart = header.findViewById(R.id.my_cart);
        iv_profile = header.findViewById(R.id.iv_header_img);
        username = header.findViewById(R.id.tv_header_name);


       /* login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.sign);
        username=findViewById(R.id.tv_header_name);*/

        My_Order.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            if (sessionManagement.isLoggedIn()) {
                Intent intent = new Intent(MainActivity.this, My_Order_activity.class);
                startActivityForResult(intent, 4);
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        My_Reward.setOnClickListener(v -> {
            if (sessionManagement.isLoggedIn()) {

                drawer.closeDrawer(GravityCompat.START);

                Reward_fragment fm = new Reward_fragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.contentPanel, fm);
                transaction.commit();

//                    android.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });
        My_Walllet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManagement.isLoggedIn()) {
                    drawer.closeDrawer(GravityCompat.START);
                    if (sessionManagement.userBlockStatus().equalsIgnoreCase("2")) {
                        Wallet_fragment fm = new Wallet_fragment();
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.contentPanel, fm);
                        transaction.commit();
                    } else {
                        showBlockDialog();
                    }


//                    Wallet_fragment fm = new Wallet_fragment();
//                    android.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        My_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dbcart.getCartCount() > 0) {
                    navigation.setSelectedItemId(R.id.navigation_notifications123);
                    CartFragment favourite_fragment = new CartFragment();
                    FragmentManager manager1 = getSupportFragmentManager();
                    FragmentTransaction transaction1 = manager1.beginTransaction();
                    transaction1.replace(R.id.contentPanel, favourite_fragment);
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                    drawer.closeDrawer(navigationView);
                } else {
                    Toast.makeText(MainActivity.this, "No Item in Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
                    Edit_profile_fragment fm = new Edit_profile_fragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                } else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
            }
        });

        sideMenu();

        new Thread(this::getCurrency).start();
        new Thread(this::checkOtpStatus).start();
        new Thread(this::checkUserNotify).start();
        new Thread(this::checkUserPayNotify).start();

        // checkConnection();

        //loadFragment(new HomeeeFragment(fragmentClickListner));
        loadFragment(homeFragment);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    Fragment fr = getSupportFragmentManager().findFragmentById(R.id.contentPanel);

                    final String fm_name = fr.getClass().getSimpleName();
                    Log.e("backstack: ", ": " + fm_name);
                    if (fm_name.contentEquals("Home_fragment")) {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        toggle.setDrawerIndicatorEnabled(true);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        toggle.syncState();

                    } else if (fm_name.contentEquals("My_order_fragment") ||
                            fm_name.contentEquals("Thanks_fragment")) {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        }
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // HomeeeFragment fm = new HomeeeFragment(fragmentClickListner);
                                // HomeFragment fm = new HomeFragment(fragmentClickListner,navigation,false);
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.contentPanel, homeFragment)
                                        .addToBackStack(null).commit();
                            }
                        });
                    } else {

                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        }
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                onBackPressed();
                            }
                        });
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        initComponent();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCF36ce_XG_VegyZJyIp-tIW0l4d15Hhrs");
        }
        placesClient = Places.createClient(this);
    }

    public void onAddressSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS
        );

        RectangularBounds ratnagiri = RectangularBounds.newInstance(
                // Ratnagiri approx.
                new LatLng(16.865514, 73.256588), // SW lat, lng
                new LatLng(17.068452, 73.449536) // NE lat, lng
        );
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("IN")
                // .setLocationBias(ratnagiri)
                .setLocationRestriction(ratnagiri)
//                .setTypeFilter(TypeFilter.CITIES)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    private void showPlacePicker() {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("AIzaSyCF36ce_XG_VegyZJyIp-tIW0l4d15Hhrs")
                .setMapsApiKey("AIzaSyDGBez6ZOe4W80UrtxOGYdfl5vgya7gTMI");

        try {
            Intent placeIntent = builder.build(this);
            startActivityForResult(placeIntent, AUTOCOMPLETE_REQUEST_CODE);
        } catch (Exception ex) {
            // Google Play services is not available...
        }
    }


    private void showBlockDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setCancelable(true);
//        alertDialog.setMessage("You are blocked from backend.\n Please Contact with customer care!");
        alertDialog.setMessage(sessionManagement.getUserBlockStatusMsg());
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onStart() {
        new Thread(this::fetchBlockStatus).start();
        super.onStart();
    }

    private void fetchBlockStatus() {

        if (!sessionManagement.userId().equalsIgnoreCase("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, USERBLOCKAPI, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("adresssHoww", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");
                        if (status.equals("2")) {
//                            JSONObject jsonArray = jsonObject.getJSONObject("data");
//                            String userStatusValue = jsonArray.getString("block");
                            sessionManagement.setUserBlockStatus("2");
                        } else {
//                            JSONObject jsonArray = jsonObject.getJSONObject("data");
//                            String userStatusValue = jsonArray.getString("block");
                            sessionManagement.setUserBlockStatus("1");
                            sessionManagement.setUserBlockStatusMsg(msg);
//                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                        }
//                        Toast.makeText(MainActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("user_id", sessionManagement.userId());
                    return param;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }

    }

    private void getLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(300000L);
        locationRequest.setFastestInterval(180000L);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        location = getLocation();
        if (location != null) {
            if (sessionManagement != null) {
                sessionManagement.setLocationPref(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                getAddress();
            }
        } else {
            setSupLocation();
        }
    }

    private void getAddress() {
        List<Address> addresses = null;
        DecimalFormat dFormat = new DecimalFormat("#.######");


        try {
            double latitude = Double.parseDouble(dFormat.format(location.getLatitude()));
            double longitude = Double.parseDouble(dFormat.format(location.getLongitude()));
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Address returnedAddress = addresses.get(0);
            StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
            }
            String city = addresses.get(0).getLocality();
//
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
//
//            String address = addresses.get(0).getAddressLine(0);
//            String knownName = addresses.get(0).getSubAdminArea();

            sessionManagement.setLocationCity(city);
            sessionManagement.setLocationPref(String.valueOf(latitude), String.valueOf(longitude));
            //runOnUiThread(() -> addres.setText(returnedAddress.getAddressLine(0)));
            addres.setText(returnedAddress.getAddressLine(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {// If the permission is granted, get the location,
            // otherwise, show a Toast
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    getLocation();
                location = getLocation();
                if (location != null) {
                    getAddress();
                }
                Log.e(TAG, "Granted");
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
////                                Log.e(TAG, "location create" + location.getLatitude() + " , " + location.getLongitude());
//                            new FetchAddressTask(MainActivity.this, MainActivity.this).execute(location);
//                        }
//                    }
//                });


            } else {
//                    Log.e(TAG, "permission denied" );

                Toast.makeText(MainActivity.this, "Location permission is necessary", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }

//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]
//                            {Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_LOCATION_PERMISSION);
//        } else {
//            Log.d(TAG, "getLocation: permissions granted");
//        }
//    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                setSupLocation();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    private void setSupLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
//                    new FetchAddressTask(AddressLocationActivity.this, AddressLocationActivity.this).execute(location);
                getAddress();
            }
        });
    }

    private boolean checkAndRequestPermissions() {

        int locationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_LOCATION_PERMISSION);
            Toast.makeText(MainActivity.this, "Go to settings and enable Location permissions", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
            public void onResponse(@NonNull Call<NotifyModelUser> call, @NonNull retrofit2.Response<NotifyModelUser> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        NotifyModelUser modelUser = response.body();
                        if (modelUser.getStatus().equalsIgnoreCase("1")) {
                            sessionManagement.setEmailServer(modelUser.getData().getEmail());
                            sessionManagement.setUserSMSService(modelUser.getData().getSms());
                            sessionManagement.setUserInAppService(modelUser.getData().getApp());
                        } else {
                            sessionManagement.setEmailServer("0");
                            sessionManagement.setUserSMSService("0");
                            sessionManagement.setUserInAppService("0");
                        }
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<NotifyModelUser> call, @NonNull Throwable t) {

            }
        });

    }

    private void checkUserPayNotify() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<PaymentVia> checkOtpStatus = apiInterface.getPaymentVia();

        checkOtpStatus.enqueue(new Callback<PaymentVia>() {
            @Override
            public void onResponse(@NonNull Call<PaymentVia> call, @NonNull retrofit2.Response<PaymentVia> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        PaymentVia modelUser = response.body();
                        if (modelUser.getStatus().equalsIgnoreCase("1")) {
                            sessionManagement.setPaymentMethodOpt(modelUser.getData().getRazorpay(), modelUser.getData().getPaypal());
                        }
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<PaymentVia> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        //stop
//        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

    }

    @Override
    public boolean onSupportNavigateUp() {
       /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();*/
        return false;
    }

    private void loadFragment(Fragment fragment) {
        try {
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentPanel, fragment)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponent() {

        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // loadFragment(new HomeeeFragment(fragmentClickListner));
                    if (homeFragment != null) {
                        HomeFragment home = (HomeFragment) homeFragment;
                        if (home.isSearchOpen()) {
                            home.closeSearch();
                        } else {
                            loadFragment(homeFragment);
                        }
                    }
//                    loadFragment(homeFragment);
//                        HomeeeFragment appNewsHome1Fragment = new HomeeeFragment();
//                        FragmentManager manager = getSupportFragmentManager();
//                        FragmentTransaction transaction = manager.beginTransaction();
//                        transaction.replace(R.id.contentPanel, appNewsHome1Fragment);
//                        transaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    loadFragment(new CategoryFragment(fragmentClickListner));
//                        CategoryFragment category_fragment = new CategoryFragment();
//                        FragmentManager manager2 = getSupportFragmentManager();
//                        FragmentTransaction transaction2 = manager2.beginTransaction();
//                        transaction2.replace(R.id.contentPanel, category_fragment);
//                        transaction2.commit();
                    return true;

                /*case R.id.navigation_notifications1:
                    //loadFragment(new SearchFragment());
                    //startActivity(new Intent(MainActivity.this, GameWebActivity.class));
                   // loadFragment(new HomeFragment(fragmentClickListner,navigation));
                    try {
                        *//*loadFragment(homeFragment);
                        ((HomeFragment)homeFragment).setSearch();*//*
                        Fragment fr = getSupportFragmentManager().findFragmentById(R.id.contentPanel);
                        final String fm_name = fr.getClass().getSimpleName();
                        if (!fm_name.contentEquals("HomeFragment")) {
                            //navigation.setSelectedItemId(R.id.navigation_home);
                            loadFragment(homeFragment);
                        }
                        ((HomeFragment)homeFragment).setSearch();
                    } catch (Exception e) {
                        e.printStackTrace();
                        navigation.setSelectedItemId(R.id.navigation_home);
                    }
                    return true;*/

                case R.id.navigation_my_orders:
                    if (sessionManagement.isLoggedIn()) {
                        Intent intent = new Intent(MainActivity.this, My_Order_activity.class);
                        startActivityForResult(intent, 4);
                    } else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    return true;


                case R.id.navigation_notifications12:
                    loadFragment(new NotificationFragment());
//                        NotificationFragment nf = new NotificationFragment();
//                        FragmentManager m4 = getSupportFragmentManager();
//                        FragmentTransaction fragmentTransactionnn = m4.beginTransaction();
//                        fragmentTransactionnn.replace(R.id.contentPanel, nf);
//                        fragmentTransactionnn.commit();
                    return true;
//                    case R.id.navigation_newsstand:
////                        mTextMessage.setText(item.getTitle());
//                        navigation.setBackgroundColor(getResources().getColor(R.color.teal_800));
//                        window.setStatusBarColor(getResources().getColor(R.color.teal_800));
//                        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.teal_800));
//
//                        EditorPicked_Fragment editorPicked_fragment = new EditorPicked_Fragment();
//                        FragmentManager manager3 = getSupportFragmentManager();
//                        FragmentTransaction transaction3 = manager3.beginTransaction();
//                        transaction3.replace(R.id.contentPanel, editorPicked_fragment);
//                        transaction3.commit();
//                        return true;

//                case R.id.nav_game:
//                    startActivity(new Intent(MainActivity.this, GameWebActivity.class));
//                    return true;

                case R.id.navigation_notifications123:
                    loadFragment(new CartFragment());
//                        CartFragment favourite_fragment = new CartFragment();
//                        FragmentManager manager1 = getSupportFragmentManager();
//                        FragmentTransaction transaction1 = manager1.beginTransaction();
//                        transaction1.replace(R.id.contentPanel, favourite_fragment);
//                        transaction1.commit();
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fm = null;
        Bundle args = new Bundle();
        if (id == R.id.sign) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        } else if (id == R.id.sign) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }

       /* else if (id == R.id.nav_shop_now) {
            fm = new Shop_Now_fragment();
        } */
        else if (id == R.id.nav_my_profile) {
            fm = new Edit_profile_fragment();
//
//            startActivity(new Intent(this, MidtransPaymentGateway.class));

//        } else if (id == R.id.nav_support) {
//            String smsNumber = "9889887711";
//            Intent sendIntent = new Intent("android.intent.action.MAIN");
//            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
//            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
//            startActivity(sendIntent);

        } else if (id == R.id.nav_aboutus) {
//            toolbar.setTitle("About");
            startActivity(new Intent(getApplicationContext(), About_us.class));
        } else if (id == R.id.nav_policy) {
            fm = new Terms_and_Condition_fragment();
            args.putString("url", TermsUrl);
            args.putString("title", getResources().getString(R.string.nav_terms));
            fm.setArguments(args);
        }
//        else if (id == R.id.nav_review) {
//            //reviewOnApp();
//        }
        else if (id == R.id.nav_contact) {
            fm = new Contact_Us_fragment();
            args.putString("url", SupportUrl);
            args.putString("title", getResources().getString(R.string.nav_terms));
            fm.setArguments(args);

        }
//        else if (id == R.id.nav_review) {
//            reviewOnApp();
//        }
        else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_logout) {
//            dbcart.clearCart();
            sessionManagement.logoutSession();
//            login.setVisibility(View.VISIBLE);
            finish();

        }
//        else if (id == R.id.nav_powerd) {
//            // stripUnderlines(textView);
//            String url = "http://sameciti.com";
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//            finish();
//        }

        if (fm != null) {


            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP"); //getPackageName()
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void sideMenu() {

        if (sessionManagement.isLoggedIn()) {
            //  tv_number.setVisibility(View.VISIBLE);
            nav_menu.findItem(R.id.nav_logout).setVisible(true);
            nav_menu.findItem(R.id.nav_my_profile).setVisible(true);
            //   nav_menu.findItem(R.id.login).setVisible(true);
            nav_menu.findItem(R.id.sign).setVisible(false);
//            nav_menu.findItem(R.id.nav_powerd).setVisible(true);

            username.setText(sessionManagement.getUserDetails().get(BaseURL.KEY_NAME));

//            nav_menu.findItem(R.id.signup).setVisible(false);

//            nav_menu.findItem(R.id.nav_user).setVisible(true);
        } else {

            //tv_number.setVisibility(View.GONE);
//            tv_name.setText(getResources().getString(R.string.btn_login));
//            tv_name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(i);
//                }
//            });
            nav_menu.findItem(R.id.login).setVisible(false);
            nav_menu.findItem(R.id.nav_my_profile).setVisible(false);
            nav_menu.findItem(R.id.nav_logout).setVisible(false);
            nav_menu.findItem(R.id.sign).setVisible(true);


            //            nav_menu.findItem(R.id.nav_user).setVisible(false);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ");
    }

    @Override
    public void onLocationChanged(Location locations) {
        if (locations != null) {
//            Log.e(TAG, "onLocationChanged: " + locations.getLatitude() + "\n" + locations.getLongitude());
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (!sessionManagement.getLatPref().equalsIgnoreCase("") && !sessionManagement.getLangPref().equalsIgnoreCase("")) {
                        DecimalFormat dFormat = new DecimalFormat("##.#######");
                        LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
                        double latitude = Double.valueOf(dFormat.format(latLng.latitude));
                        double longitude = Double.valueOf(dFormat.format(latLng.longitude));
//                        Log.i("TAG", latitude + "\n" + longitude);
                        Location locationA = new Location("cal 1");
                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);
                        double disInMetter = locationA.distanceTo(locations);
                        double disData = disInMetter / 1000;
                        DecimalFormat dFormatt = new DecimalFormat("#.#");
                        disData = Double.parseDouble(dFormatt.format(disData));
//                        Log.i(TAG, "in" + disData);
                        if (disData > 5.0) {
                            if (!enterInFirst) {
                                enterInFirst = true;
                                location = locations;
                                getAddress();
                                if (navigation.getSelectedItemId() == R.id.navigation_home) {
                                    //loadFragment(new HomeeeFragment(fragmentClickListner));
                                    loadFragment(homeFragment);
                                }
                            }
                        } else {
                            enterInFirst = true;
                            if (addres.getText().toString().equalsIgnoreCase("")) {
                                if (navigation.getSelectedItemId() == R.id.navigation_home) {
                                    //loadFragment(new HomeeeFragment(fragmentClickListner));
                                    loadFragment(homeFragment);
                                }
                                getAddress();
                            }
                        }
                    } else {
                        enterInFirst = true;
                        location = locations;
                        if (navigation.getSelectedItemId() == R.id.navigation_home) {
                            //loadFragment(new HomeeeFragment(fragmentClickListner));
                            loadFragment(homeFragment);
                        }
                        getAddress();
                    }
                }
            }).start();

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onTaskCompleted(String result) {
//        Log.e(TAG, "onTaskCompleted: " + result);
        ((TextView) findViewById(R.id.address)).setText(result);
    }

//    public void setCartCounter(String totalitem) {
//        try {
//            totalBudgetCount.setText(totalitem);
//        }catch (Exception e){}
//    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase("cardqnty")) {
//            totalBudgetCount.setText(pref.getInt("cardqnty",0));
//            int badgeCount = pref.getInt("cardqnty", 0);
            int badgeCount = dbcart.getCartCount();
            if (badgeCount > 0) {
//                totalBudgetCount.setVisibility(View.VISIBLE);
//                totalBudgetCount.setText(""+badgeCount);
                navigation.getOrCreateBadge(R.id.navigation_notifications123).setNumber(badgeCount);
            } else {
//                totalBudgetCount.setVisibility(View.GONE);
                navigation.removeBadge(R.id.navigation_notifications123);
            }
        }
    }

    @Override
    protected void onDestroy() {
        pref.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    //    public void updateHeader() {
//        if (sessionManagement.isLoggedIn()) {
//            String getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
//            String getimage = sessionManagement.getUserDetails().get(BaseURL.KEY_IMAGE);
//            String getemail = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
//            SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
//            String previouslyEncodedImage = shre.getString("image_data", "");
//            if (!previouslyEncodedImage.equalsIgnoreCase("")) {
//                byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//                iv_profile.setImageBitmap(bitmap);
//            }
//            Glide.with(this)
//                    .load(BaseURL.IMG_PROFILE_URL + getimage)
//                    .placeholder(R.drawable.icon)
//
//                    .into(iv_profile);
    //  tv_name.setText(getname);

//        }
//    }


   /*

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_cart);
        item.setVisible(true);

       View count = item.getActionView();
        totalBudgetCount = (TextView) count.findViewById(R.id.actionbar_notifcation_textview);
        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.performIdentifierAction(item.getItemId(), 0);
            }
        });


        totalBudgetCount.setText("" + dbcart.getCartCount());
        return true;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == RESULT_OK){
//
//        }
        if (requestCode == 4) {
            if (data != null && data.getExtras() != null) {
//                String activityIdentify = data.getExtras().getString("actIdentfy");
//                if (activityIdentify != null && activityIdentify.equalsIgnoreCase("past")) {
//
//                }
                ArrayList<NewPendingDataModel> orderSubModels = (ArrayList<NewPendingDataModel>) data.getSerializableExtra("datalist");
                if (orderSubModels != null) {
                    dbcart.clearCart();
                    for (int i = 0; i < orderSubModels.size(); i++) {
                        NewPendingDataModel odModel = orderSubModels.get(i);
                        if (odModel.getDescription() != null && !odModel.getDescription().equalsIgnoreCase("")) {
                            double price = Double.parseDouble(odModel.getPrice()) / Double.parseDouble(odModel.getQty());
                            HashMap<String, String> map = new HashMap<>();
                            map.put("varient_id", odModel.getVarient_id());
                            map.put("product_name", odModel.getProduct_name());
                            map.put("category_id", odModel.getVarient_id());
                            map.put("title", odModel.getProduct_name());
                            map.put("price", String.valueOf(price));
                            map.put("mrp", odModel.getTotal_mrp());
                            map.put("product_image", odModel.getVarient_image());
                            map.put("status", "1");
                            map.put("in_stock", "");
                            map.put("unit_value", odModel.getQuantity() + "" + odModel.getUnit());
                            map.put("unit", "");
                            map.put("increament", "0");
                            map.put("rewards", "0");
                            map.put("stock", "0");
                            map.put("product_description", odModel.getDescription());

                            if (!odModel.getQty().equalsIgnoreCase("0")) {
                                dbcart.setCart(map, Integer.parseInt(odModel.getQty()));
                            } else {
                                dbcart.removeItemFromCart(map.get("varient_id"), map.get("store_id"));
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                pref.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
                            }
                        }
                    }

                    loadFragment(new CartFragment());

                }

            }

        } else if (requestCode == 22) {
            DecimalFormat dFormat = new DecimalFormat("##.#######");
            LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
            double latitude = Double.valueOf(dFormat.format(latLng.latitude));
            double longitude = Double.valueOf(dFormat.format(latLng.longitude));
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            Log.i("TAG 22", latitude + "\n" + longitude);
//            Location locationA = new Location("cal 1");
//                        Location locationB = new Location("cal 2");
//            locationA.setLatitude(latitude);
//            locationA.setLongitude(longitude);
//            location = locationA;
            getAddress();
            if (navigation.getSelectedItemId() == R.id.navigation_home) {
                //loadFragment(new HomeeeFragment(fragmentClickListner));
                loadFragment(homeFragment);
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
                Place place = PingPlacePicker.getPlace(data);
                if (place != null && place.getAddress() != null) {
//                    AddressComponents addressComponents = place.getAddressComponents();
                    String[] addressArray = place.getAddress().split(",");
                    int addressIndex = addressArray.length - 3;
                    if (addressIndex > -1 && addressIndex < addressArray.length) {
                        sessionManagement.setLocationCity(addressArray[addressIndex]);
                    }
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                    addres.setText(place.getAddress());
                    LatLng latLng = place.getLatLng();
                    if (latLng != null) {
                        location.setLatitude(latLng.latitude);
                        location.setLongitude(latLng.longitude);
                        sessionManagement.setLocationPref(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));
                    }
                    /*if (addressComponents != null) {
                        sessionManagement.setLocationCity(addressComponents.asList().get(1).getName());
                    }*/
                }
            } /*else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(MainActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, status.getStatusMessage());
            }*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getCurrency() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, currencyApi, response -> {
            Log.d("currency api", response);

            try {

                JSONObject currencyObject = new JSONObject(response);
                if (currencyObject.getString("status").equalsIgnoreCase("1") && currencyObject.getString("message").equalsIgnoreCase("currency")) {

                    JSONObject dataObject = currencyObject.getJSONObject("data");

                    sessionManagement.setCurrency(dataObject.getString("currency_name"), dataObject.getString("currency_sign"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void checkOtpStatus() {

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
            }

            @Override
            public void onFailure(@NonNull Call<ForgotEmailModel> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (toCart) {
            navigation.setSelectedItemId(R.id.navigation_notifications123);
        } else {
            navigation.setSelectedItemId(R.id.navigation_home);
        }
        toCart = false;
        int badgeCount = dbcart.getCartCount();
        if (badgeCount > 0) {
//                totalBudgetCount.setVisibility(View.VISIBLE);
//                totalBudgetCount.setText(""+badgeCount);
            navigation.getOrCreateBadge(R.id.navigation_notifications123).setNumber(badgeCount);
        } else {
//                totalBudgetCount.setVisibility(View.GONE);
            navigation.removeBadge(R.id.navigation_notifications123);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        try {
            Fragment fr = getSupportFragmentManager().findFragmentById(R.id.contentPanel);
            final String fm_name = fr.getClass().getSimpleName();
            HomeFragment home = (HomeFragment) homeFragment;
            if (drawer.isDrawerOpen(navigationView)) {
                drawer.closeDrawer(navigationView);
            } else if (home != null && home.isSearchOpen()) {
                home.closeSearch();
            } else if (!fm_name.contentEquals("HomeFragment")) {
                navigation.setSelectedItemId(R.id.navigation_home);
            } else {
                Utils.doubleBackExit(this, navigation);
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }
}
