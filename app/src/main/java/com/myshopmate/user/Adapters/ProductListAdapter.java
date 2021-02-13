package com.myshopmate.user.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.user.Activity.ProductDetails;
import com.myshopmate.user.ModelClass.NewCategoryDataModel;
import com.myshopmate.user.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.myshopmate.user.Config.BaseURL.IMG_URL;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NewCategoryDataModel> products;
    public ProductListAdapter(Context context, ArrayList<NewCategoryDataModel> products) {
        this.context = context;
        this.products = products;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout_product_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewCategoryDataModel product = products.get(position);
        if (product.getVarient_image() != null && !product.getVarient_image().isEmpty()) {
            Picasso.get()
                    .load(IMG_URL + product.getVarient_image())
                    .into(holder.ivPhoto);
        }
        holder.tvProductName.setText(product.getProduct_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("sId", product.getProduct_id());
                intent.putExtra("store_id", product.getStore_id());
                intent.putExtra("sName", product.getProduct_name());
                intent.putExtra("descrip", product.getDescription());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("mrp", product.getMrp());
                intent.putExtra("unit", product.getUnit());
                intent.putExtra("qty", product.getQuantity());
                intent.putExtra("stock", product.getStock());
                intent.putExtra("image", product.getVarient_image());
                intent.putExtra("sVariant_id", product.getVarient_id());
                intent.putExtra("variants", product.getVarients());
                intent.putExtra("in_stock", product.getIn_stock());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        ImageView ivPhoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvProductName = itemView.findViewById(R.id.tv_name);
        }
    }
}
