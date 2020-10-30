package com.myshopmate.user.Adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.user.R;
import com.myshopmate.user.util.DatabaseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.myshopmate.user.Config.BaseURL.IMG_URL;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class ImageAdapterData extends RecyclerView.Adapter<ImageAdapterData.ProductHolder> {
    JSONArray list;
    Activity activity;
    String price_tx;
    SharedPreferences preferences;
    String language;

    int lastpostion;
    DatabaseHandler dbHandler;

    public ImageAdapterData(Activity activity, JSONArray list) {
        this.list = list;
        this.activity = activity;

        dbHandler = new DatabaseHandler(activity);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageadapter, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {

        JSONObject map = null;
        try {
            map = list.getJSONObject(position);
            Picasso.with(activity)
                    .load(IMG_URL+ map.getString("product_image"))
                    .into(holder.image_data);
            holder.tvPName.setText(map.getString("p_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.length();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        public ImageView image_data;
        TextView tvPName;


        public ProductHolder(View view) {
            super(view);


            image_data = view.findViewById(R.id.image_data);
            tvPName = view.findViewById(R.id.tv_p_name);

           ;

            //  tv_add.setText(R.string.tv_pro_update);

        }
    }



}

