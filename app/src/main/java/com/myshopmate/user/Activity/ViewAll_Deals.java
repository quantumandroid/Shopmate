package com.myshopmate.user.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.myshopmate.user.Adapters.ViewAll_Adapter;
import com.myshopmate.user.ModelClass.CartModel;
import com.myshopmate.user.R;
import com.myshopmate.user.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.myshopmate.user.Config.BaseURL.HomeDeal;

public class ViewAll_Deals extends AppCompatActivity {
    ProgressDialog progressDialog;
    ViewAll_Adapter DealAdapter;
    private List<CartModel> dealList = new ArrayList<>();
    ShimmerRecyclerView shimmerRecyclerView;
    String catId,catName;
    private Session_management session_management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all__deals);

        progressDialog=new ProgressDialog(ViewAll_Deals.this);
        session_management = new Session_management(ViewAll_Deals.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        shimmerRecyclerView= findViewById(R.id.recyclerDealsDay);
        if(isOnline()){
            progressDialog.show();
            DealUrl();}

    }

    private void DealUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HomeDeal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("HomeDeal",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")){
                        dealList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String product_id = jsonObject1.getString("product_id");
                            String varient_id = jsonObject1.getString("varient_id");
                            String product_name = jsonObject1.getString("product_name");
                            String description= jsonObject1.getString("description");
                            String pprice= jsonObject1.getString("price");
                            String quantity = jsonObject1.getString("quantity");
                            String product_image = jsonObject1.getString("varient_image");
                            String mmrp = jsonObject1.getString("mrp");
                            String unit = jsonObject1.getString("unit");
                            String storeId = jsonObject1.getString("store_id");
//                            String count = jsonObject1.getString("count");
                            String totalOff= String.valueOf(Integer.parseInt(String.valueOf(mmrp))-Integer.parseInt(String.valueOf(pprice)));

                            CartModel recentData= new CartModel(product_id,product_name,description,pprice,quantity+" "+unit,product_image,session_management.getCurrency()+totalOff+" "+"Off",mmrp," ",unit,storeId);
                            recentData.setVarient_id(varient_id);
                            dealList.add(recentData);

                        }
//                        DealAdapter = new ViewAll_Adapter(dealList,getApplicationContext());
                        shimmerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        shimmerRecyclerView.setAdapter(DealAdapter);
                        DealAdapter.notifyDataSetChanged();

                    }else {
                        JSONObject resultObj = jsonObject.getJSONObject("results");
                        String msg = resultObj.getString("message");
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat",session_management.getLatPref());
                params.put("lng",session_management.getLangPref());
                params.put("city",session_management.getLocationCity());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    
    }
}
