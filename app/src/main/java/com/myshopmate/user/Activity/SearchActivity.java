package com.myshopmate.user.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myshopmate.user.Adapters.CategoryGridAdapter;
import com.myshopmate.user.Adapters.HomeAdapter;
import com.myshopmate.user.Adapters.StoreProductsPagerAdapter;
import com.myshopmate.user.Categorygridquantity;
import com.myshopmate.user.Config.BaseURL;
import com.myshopmate.user.Constans.RecyclerTouchListener;
import com.myshopmate.user.ModelClass.NewCategoryDataModel;
import com.myshopmate.user.ModelClass.NewCategoryVarientList;
import com.myshopmate.user.ModelClass.Store;
import com.myshopmate.user.R;
import com.myshopmate.user.util.AppController;
import com.myshopmate.user.util.CustomVolleyJsonRequest;
import com.myshopmate.user.util.Session_management;
import com.myshopmate.user.util.Utils;
import com.volley.simple_request.OnResponseListener;
import com.volley.simple_request.SimpleRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.myshopmate.user.Config.BaseURL.MyPrefreance;

public class SearchActivity extends AppCompatActivity {

    Context context;
    RecyclerView rv_items;
    SharedPreferences sharedPreferences;
    private List<Store> store_modelList = new ArrayList<>();
    private HomeAdapter adapter1;
    private Session_management session_management;
    private ProgressDialog progressDialog;
    private ViewPager viewPagerStoresProducts;
    private StoreProductsPagerAdapter storeProductsPagerAdapter;
    private RecyclerView rvProducts;
    private EditText etSearch;
    private TabLayout stores_products_tab_layout;
    List<NewCategoryDataModel> newCategoryDataModel = new ArrayList<>();
    CategoryGridAdapter categoryGridAdapter;
    List<NewCategoryVarientList> varientProducts = new ArrayList<>();
    ImageView iv_search_clear, iv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        sharedPreferences = getSharedPreferences(MyPrefreance, MODE_PRIVATE);
        session_management = new Session_management(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait while loading..");
        progressDialog.setCancelable(false);
        viewPagerStoresProducts = findViewById(R.id.viewPagerStoresProducts);
        etSearch = findViewById(R.id.et_search);
        stores_products_tab_layout = findViewById(R.id.stores_products_tab_layout);
        iv_search_clear = findViewById(R.id.iv_search_clear);
        iv_search = findViewById(R.id.iv_search);
        rvProducts = findViewById(R.id.rv_home_products);
        rv_items = findViewById(R.id.rv_home);
        stores_products_tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerStoresProducts.setCurrentItem(stores_products_tab_layout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupViewPager();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_items.setLayoutManager(linearLayoutManager);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        rv_items.setNestedScrollingEnabled(false);
        rv_items.addOnItemTouchListener(new RecyclerTouchListener(context, rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*String getid = category_modelList.get(position).getCat_id();
                Intent intent = new Intent(getActivity(), CategoryPage.class);
                intent.putExtra("cat_id", getid);
                intent.putExtra("title", category_modelList.get(position).getTitle());
                intent.putExtra("image", category_modelList.get(position).getImage());
                startActivityForResult(intent, 24);
*/

                // String getid = store_modelList.get(position).getStore_id();
                Intent intent = new Intent(context, CategoryPage.class);
                intent.putExtra("cat_id", "47");
                intent.putExtra("title", store_modelList.get(position).getStore_name());
                intent.putExtra("store_id", store_modelList.get(position).getStore_id());
                intent.putExtra("is_from_category", false);
                // intent.putExtra("store_id", adapter1.getModelList().get(position).getStore_id());
                // intent.putExtra("title", adapter1.getModelList().get(position).getStore_name());
//                  intent.putExtra("image", store_modelList.get(position).getStore_image_url());
                startActivityForResult(intent, 24);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        store_modelList = new ArrayList<>();
        adapter1 = new HomeAdapter(store_modelList, context,R.layout.row_home_rv1);
        rv_items.setAdapter(adapter1);

        LinearLayoutManager layoutManagerProducts = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvProducts.setLayoutManager(layoutManagerProducts);
        rvProducts.setItemAnimator(new DefaultItemAnimator());
        rvProducts.setNestedScrollingEnabled(false);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && !etSearch.getText().toString().trim().isEmpty()) {
                    if (viewPagerStoresProducts.getCurrentItem() == 1) {
                        product(etSearch.getText().toString().trim());
                    } else {
                        selectStores(etSearch.getText().toString().trim());
                    }
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    iv_search_clear.setVisibility(View.INVISIBLE);
                    iv_search.setVisibility(View.INVISIBLE);
                    if (viewPagerStoresProducts.getCurrentItem() == 1) {
                        product("");
                    } else {
                        selectStores("");
                    }
                } else {
                    iv_search_clear.setVisibility(View.VISIBLE);
                    iv_search.setVisibility(View.VISIBLE);
                }
            }
        });

        Categorygridquantity categorygridquantity = new Categorygridquantity() {
            @Override
            public void onClick(View view, int position, String ccId, String id) {
                varientProducts.clear();
                // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                // TextView txt = view.findViewById(R.id.txt);
                // txt.setText(id);
                // LinearLayout cancl = view.findViewById(R.id.cancl);
                //  cancl.setOnClickListener(v -> behavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
                /*RecyclerView recyler_popup = view.findViewById(R.id.recyclerVarient);
                recyler_popup.setLayoutManager(new LinearLayoutManager(context));*/
                varientProducts.addAll(newCategoryDataModel.get(position).getVarients());
                /*Adapter_popup selectCityAdapter = new Adapter_popup(context, varientProducts, id, position1 -> {
                    if (varientProducts.get(position1).getVarient_id().equalsIgnoreCase(newCategoryDataModel.get(position).getVarient_id())) {
                        categoryGridAdapter.notifyItemChanged(position);
                    }
                }, null, null, null);
                recyler_popup.setAdapter(selectCityAdapter);*/

//                Varient_product(ccId, recyler_popup, id);

            }

            @Override
            public void onCartItemAddOrMinus() {
                /*if (dbcart.getCartCount() > 0) {
                    bottom_lay_total.setVisibility(View.VISIBLE);
                    total_price.setText(session_management.getCurrency() + " " + dbcart.getTotalAmount());
                    total_count.setText("Total Items " + dbcart.getCartCount());
                } else {
                    bottom_lay_total.setVisibility(View.GONE);
                }*/
            }
        };

        rvProducts.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        categoryGridAdapter = new CategoryGridAdapter(newCategoryDataModel, context, categorygridquantity);
        rvProducts.setAdapter(categoryGridAdapter);
    }

    private void setupViewPager() {
        viewPagerStoresProducts.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stores_products_tab_layout.getTabAt(position).select();
                etSearch.setText("");
                if (position == 1) {
                    etSearch.setHint("What are you looking for?");
                    Utils.hideKeyboard((Activity)context);
                    etSearch.clearFocus();
                } else {
                    etSearch.setHint("Find stores");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ArrayList<Integer> layouts = new ArrayList<>();
        layouts.add(R.id.rv_home);
        layouts.add(R.id.rv_home_products);
        storeProductsPagerAdapter = new StoreProductsPagerAdapter(context, layouts);
        viewPagerStoresProducts.setAdapter(storeProductsPagerAdapter);
    }

    private boolean isUserInDelRange() {
        try {
            // DistanceCalculator distanceCalculator = new DistanceCalculator();
            //double delRange = Double.valueOf(store.getDel_range());
            double delRange = Double.parseDouble(Splash.configData.getDelivery_range());

        /*double lat1 = Double.parseDouble(store.getLat());
        double lng1 = Double.parseDouble(store.getLng());*/
            double lat1 = Double.parseDouble(Splash.configData.getCentre_lat());
            double lng1 = Double.parseDouble(Splash.configData.getCentre_lng());
            double lat2 = Double.parseDouble(session_management.getLatPref());
            double lng2 = Double.parseDouble(session_management.getLangPref());

            //double distance = distanceCalculator.distance(lat1, lng1, lat2, lng2);
            double distance = Utils.calculateMapDistance(lat1, lng1, lat2, lng2);
            // distance = distance * 2;
            // Toast.makeText(contexts, "Distance : " + distance, Toast.LENGTH_LONG).show();

            return distance <= delRange;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private void selectStores(String search_key) {
        //store_modelList.clear();
        //adapter1.notifyDataSetChanged();
        if (!isUserInDelRange()) {
            Toast.makeText(context, "Delivery is not available for your location", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "select * from store where store_name like '%" + search_key + "%'";
        //String sql = "select * from store";
        SimpleRequest simpleRequest = new SimpleRequest(context);
        simpleRequest.get(sql, new OnResponseListener() {
            @Override
            public void onSuccess(com.volley.response.Response response) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    store_modelList.clear();
                    //store_modelList = getStores(response.getString());
                    store_modelList.addAll(getStores(response.getString()));
                    for (Store store : store_modelList) {
                        Utils.stores.put(store.getStore_id(), store);
                    }
                   /*adapter1 = new HomeAdapter(store_modelList, getActivity());
                    rv_items.setAdapter(adapter1);*/
                    // adapter1.setSearch(etSearch);
                    adapter1.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    // showAlert(e.getMessage());
                }


            }

            @Override
            public void onFailure(String error) {
                store_modelList.clear();
                adapter1.notifyDataSetChanged();
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ArrayList<Store> getStores(String JSON_STRING) {

        ArrayList<Store> arrayList = new ArrayList();

        try {
            JSONObject main_object = new JSONObject(JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); ++i) {
                JSONObject jo = result.getJSONObject(i);

                Gson gson = new Gson();
                Store store = gson.fromJson(jo.toString(), Store.class);
                arrayList.add(store);
                /*if (isUserInDelRange()) {
                    arrayList.add(store);
                }*/

            }
        } catch (JSONException var8) {
            var8.printStackTrace();
        }

        return arrayList;
    }

    private void product(String search) {
        newCategoryDataModel.clear();
        categoryGridAdapter.notifyDataSetChanged();
        if (!isUserInDelRange()) {
            Toast.makeText(context, "Delivery is not available for your location", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("search_key", search);
        params.put("is_from_category", "false");
        params.put("cat_id", "");
        /*params.put("user_lat", session_management.getLatPref());
        params.put("user_lng", session_management.getLangPref());
        params.put("centre_lat", Splash.configData.getCentre_lat());
        params.put("centre_lng", Splash.configData.getCentre_lng());
        params.put("delivery_range", Splash.configData.getDelivery_range());*/
        /*params.put("store_id", store_id);
        params.put("cat_id", cat_id);
        params.put("lat", session_management.getLatPref());
        params.put("lng", session_management.getLangPref());
        params.put("city", session_management.getLocationCity());*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.cat_product_all, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApi", response.toString());
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    String status = response.getString("status");

//                    String message = response.getString("message");

                    if (status.contains("1")) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewCategoryDataModel>>() {
                        }.getType();
                        List<NewCategoryDataModel> listorl = gson.fromJson(response.getString("data"), listType);
                        for (NewCategoryDataModel categoryDataModel : listorl) {

                            if (categoryDataModel.getIn_stock().equals("1")){

                                newCategoryDataModel.add(categoryDataModel);
                            }
                        }
//                        newCategoryDataModel.addAll(listorl);

//                        for (int i = 0; i < listorl.size(); i++) {
//                            List<NewCategoryVarientList> listddd = listorl.get(i).getVarients();
//                            for (int j = 0; j < listddd.size(); j++) {
//                                NewCategoryShowList newCategoryShowList = new NewCategoryShowList(listorl.get(i).getProduct_id(), listorl.get(i).getProduct_name(), listorl.get(i).getProduct_image(), listddd.get(j));
//                                newModelList.add(newCategoryShowList);
//                            }
//                        }

                        categoryGridAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                newCategoryDataModel.clear();
                categoryGridAdapter.notifyDataSetChanged();
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                error.printStackTrace();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (Utils.isOnline((Activity) context)) {
            selectStores("");
            product("");
        }
    }
}