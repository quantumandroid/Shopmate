package com.myshopmate.user.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.user.Activity.CategoryPage;
import com.myshopmate.user.ModelClass.PopularCategoryModel;
import com.myshopmate.user.R;

import java.util.ArrayList;

public class PopularCatsAdapter extends RecyclerView.Adapter<PopularCatsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PopularCategoryModel> popularCategoryModels;
    public PopularCatsAdapter(Context context, ArrayList<PopularCategoryModel> popularCategoryModels) {
        this.context = context;
        this.popularCategoryModels = popularCategoryModels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_layout_popular_cats,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PopularCategoryModel popularCategoryModel = popularCategoryModels.get(position);
        holder.tvCatName.setText(popularCategoryModel.getCategoryName());
        holder.rvProductsAdapter.setAdapter(new ProductListAdapter(context,popularCategoryModel.getProducts()));
        holder.tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryPage.class);
                intent.putExtra("cat_id", popularCategoryModel.getCategoryID());
                intent.putExtra("title", popularCategoryModel.getCategoryName());
                intent.putExtra("sub_title", "");
                intent.putExtra("store_id", "");
                intent.putExtra("is_from_category", true);
                // intent.putExtra("store_id", adapter1.getModelList().get(position).getStore_id());
                // intent.putExtra("title", adapter1.getModelList().get(position).getStore_name());
//                  intent.putExtra("image", store_modelList.get(position).getStore_image_url());
                ((Activity)context).startActivityForResult(intent, 24);
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularCategoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCatName;
        RecyclerView rvProductsAdapter;
        ImageView tvViewAll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCatName = itemView.findViewById(R.id.tv_name_category);
            rvProductsAdapter = itemView.findViewById(R.id.rv_product_list);
            tvViewAll = itemView.findViewById(R.id.tv_view_all);
        }
    }
}
