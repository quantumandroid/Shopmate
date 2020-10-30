package com.myshopmate.user.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.user.ModelClass.Store;
import com.myshopmate.user.R;
import com.myshopmate.user.util.DatabaseHandler;
import com.myshopmate.user.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ProductHolder> {
    ArrayList<JSONArray> list;
    Activity activity;
    DatabaseHandler dbHandler;

    public BasketAdapter(Activity activity, ArrayList<JSONArray> list) {
        this.list = list;
        this.activity = activity;
        dbHandler = new DatabaseHandler(activity);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_basket_item, parent, false);
        return new ProductHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        JSONArray basket = list.get(position);
        holder.tvItemsCount.setText(String.valueOf(basket.length()) + " items");
        double totalPrice = 0;
        try {
            Store store = Utils.stores.get(basket.getJSONObject(0).getString("store_id"));
            if (store != null) {
                holder.tvStoreName.setText(store.getStore_name());
            } else {
                holder.tvStoreName.setText("");
            }
        } catch (JSONException e) {
            holder.tvStoreName.setText("");
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < basket.length(); i++) {
                totalPrice += basket.getJSONObject(i).getDouble("total_price");
            }
        } catch (JSONException e) {
            totalPrice = 0;
            e.printStackTrace();
        }
        holder.tvTotalPrice.setText("₹" + totalPrice);
        ImageAdapterData imageAdapterData = new ImageAdapterData(activity, basket);
        holder.rvBasket.setAdapter(imageAdapterData);
        holder.rvBasket.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView tvStoreName, tvTotalPrice, tvItemsCount;
        RecyclerView rvBasket;

        public ProductHolder(View view) {
            super(view);
            tvStoreName = view.findViewById(R.id.tv_store_name);
            rvBasket = view.findViewById(R.id.rv_basket);
            tvTotalPrice = view.findViewById(R.id.tv_total_price);
            tvItemsCount = view.findViewById(R.id.tv_items_count);

        }
    }


}

