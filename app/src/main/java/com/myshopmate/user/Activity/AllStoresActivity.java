package com.myshopmate.user.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myshopmate.user.Adapters.HomeAdapter;
import com.myshopmate.user.Constans.RecyclerTouchListener;
import com.myshopmate.user.ModelClass.Store;
import com.myshopmate.user.R;
import com.volley.simple_request.OnResponseListener;
import com.volley.simple_request.SimpleRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllStoresActivity extends AppCompatActivity {

    Context context;
    private RecyclerView rvAllStores;
    private HomeAdapter storesAdapter;
    private List<Store> allStores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_stores);
        context = this;
        rvAllStores = findViewById(R.id.rv_stores_all);
        allStores = new ArrayList<>();
        storesAdapter = new HomeAdapter(allStores, this, R.layout.row_home_rv1);
        rvAllStores.setAdapter(storesAdapter);
        rvAllStores.addOnItemTouchListener(new RecyclerTouchListener(context, rvAllStores, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, CategoryPage.class);
                intent.putExtra("cat_id", "47");
                intent.putExtra("title", allStores.get(position).getStore_name());
                intent.putExtra("store_id", allStores.get(position).getStore_id());
                intent.putExtra("is_from_category", false);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void selectStores(String search_key) {
        String sql = "select * from store where store_name like '%" + search_key + "%'";
        SimpleRequest simpleRequest = new SimpleRequest(context);
        simpleRequest.get(sql, new OnResponseListener() {
            @Override
            public void onSuccess(com.volley.response.Response response) {
                try {
                    allStores.clear();
                    allStores.addAll(getStores(response.getString()));
                    storesAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                allStores.clear();
                storesAdapter.notifyDataSetChanged();
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectStores("");
    }

    public ArrayList<Store> getStores(String JSON_STRING) {

        ArrayList<Store> arrayList = new ArrayList<>();

        try {
            JSONObject main_object = new JSONObject(JSON_STRING);
            JSONObject jsonObject = main_object.getJSONObject("output");
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); ++i) {
                JSONObject jo = result.getJSONObject(i);
                Gson gson = new Gson();
                Store store = gson.fromJson(jo.toString(), Store.class);
                arrayList.add(store);
            }
        } catch (JSONException var8) {
            var8.printStackTrace();
        }

        return arrayList;
    }
}