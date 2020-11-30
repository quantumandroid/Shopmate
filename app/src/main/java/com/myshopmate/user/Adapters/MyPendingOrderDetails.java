package com.myshopmate.user.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.myshopmate.user.Config.BaseURL;
import com.myshopmate.user.ModelClass.NewPastOrderSubModel;
import com.myshopmate.user.R;
import com.myshopmate.user.util.Session_management;

import java.util.List;

public class MyPendingOrderDetails extends RecyclerView.Adapter<MyPendingOrderDetails.MyDataViewHolder> {
    private Context context;
    private List<NewPastOrderSubModel> modelList;
    private Session_management session_management;

    public MyPendingOrderDetails(Context context, List<NewPastOrderSubModel> modelList) {
        this.context = context;
        this.modelList = modelList;
        session_management = new Session_management(context);
    }

    @NonNull
    @Override
    public MyDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_order_detail_rv, parent, false);
        context = parent.getContext();
        session_management = new Session_management(context);
        return new MyDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDataViewHolder holder, int position) {
        NewPastOrderSubModel mList = modelList.get(position);
        Picasso.get()
                .load(BaseURL.IMG_URL + mList.getVarient_image())
                .placeholder(R.drawable.splashicon)
                .into(holder.iv_img);

        if (mList.getDescription() != null && !mList.getDescription().equalsIgnoreCase("")) {
            holder.tv_order_descrp.setVisibility(View.VISIBLE);
            holder.tv_order_descrp.setText(mList.getDescription());
        } else {
            holder.tv_order_descrp.setVisibility(View.GONE);
        }

        holder.tv_title.setText(mList.getProduct_name());
        holder.tv_price.setText(session_management.getCurrency() + " " + mList.getPrice());
        holder.txtQty.setText("Qty - " + "" + mList.getQuantity() + " " + mList.getUnit());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyDataViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_price, tv_qty, txtQty, tv_order_descrp;
        public ImageView iv_img;

        public MyDataViewHolder(@NonNull View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_order_Detail_title);
            tv_order_descrp = view.findViewById(R.id.tv_order_descrp);
            tv_price = view.findViewById(R.id.tv_order_Detail_price);
//            tv_qty = (TextView) view.findViewById(R.id.tv_order_Detail_qty);
            txtQty = view.findViewById(R.id.txtQty);
            iv_img = view.findViewById(R.id.iv_order_detail_img);
        }
    }
}
