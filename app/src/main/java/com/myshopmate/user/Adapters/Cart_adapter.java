package com.myshopmate.user.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.user.ModelClass.Store;
import com.myshopmate.user.util.Utils;
import com.squareup.picasso.Picasso;
import com.myshopmate.user.R;
import com.myshopmate.user.util.DatabaseHandler;
import com.myshopmate.user.util.Session_management;
import com.myshopmate.user.util.ViewNotifier;

import java.util.ArrayList;
import java.util.HashMap;

import static com.myshopmate.user.Config.BaseURL.IMG_URL;
import static com.myshopmate.user.Fragments.CartFragment.tv_total;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class Cart_adapter extends RecyclerView.Adapter<Cart_adapter.ProductHolder> {
    ArrayList<HashMap<String, String>> list;
    Activity activity;
    String price_tx;
    SharedPreferences preferences;
    String language;
    int lastpostion;
    DatabaseHandler dbHandler;
    private ViewNotifier notifier;
    private Session_management session_management;
    private Context context;

    public Cart_adapter(Activity activity, ArrayList<HashMap<String, String>> list, ViewNotifier viewNotifier) {
        this.list = list;
        this.activity = activity;
        notifier = viewNotifier;
        context = activity;
        dbHandler = new DatabaseHandler(activity);
        session_management = new Session_management(activity);
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart, parent, false);
        context = parent.getContext();
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        HashMap<String, String> map = list.get(position);
        holder.currency_indicator.setText(session_management.getCurrency());
        Picasso.with(activity)
                .load(IMG_URL + map.get("product_image"))
                .into(holder.iv_logo);

        holder.tv_title.setText(map.get("product_name"));
        holder.tvStoreName.setText(map.get("store_name"));
        Store store = Utils.stores.get(map.get("store_id"));
        if (store == null) {
            holder.tvStoreName.setText("");
        } else {
            holder.tvStoreName.setText(store.getStore_name());
        }
        holder.pDescrptn.setText(map.get("product_description"));
        int items = (int) Double.parseDouble(dbHandler.getInCartItemQty(map.get("varient_id"),map.get("store_id")));
        int sprice = (int) Double.parseDouble(map.get("price"));

        int qtyd = Integer.parseInt(dbHandler.getInCartItemQtys(map.get("varient_id"),map.get("store_id")));
        if (qtyd > 0) {
            holder.tv_add.setVisibility(View.GONE);
            holder.ll_addQuan.setVisibility(View.VISIBLE);
            holder.tv_contetiy.setText("" + qtyd);
            holder.pPrice.setText("" + (sprice * qtyd));
//            holder.pMrp.setText("" + (sprice * qtyd));
        } else {
            holder.tv_add.setVisibility(View.VISIBLE);
            holder.ll_addQuan.setVisibility(View.GONE);
            holder.pPrice.setText(""+sprice);
//            holder.pMrp.setText(cc.getpMrp());
            holder.tv_contetiy.setText("" + 0);
        }

//        holder.pPrice.setText("" + sprice * items);
//        holder.tv_contetiy.setText("" + items);
//        holder.minteger = items;
        holder.pQuan.setText("" + map.get("unit_value"));
//        holder.pMrp.setText("" + map.get("mrp"));
//        holder.pMrp.setPaintFlags(holder.pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.tv_add.setVisibility(View.GONE);
                holder.ll_addQuan.setVisibility(View.VISIBLE);

                dbHandler.setCart(map, Integer.valueOf(holder.tv_contetiy.getText().toString()));
                Double items = Double.parseDouble(dbHandler.getInCartItemQty(map.get("varient_id"),map.get("store_id")));
                Double price = Double.parseDouble(map.get("price"));
                Double reward = Double.parseDouble(map.get("rewards"));
                holder.pPrice.setText("" + price * items);
                //  holder.tv_reward.setText("" + reward * items);
                //   holder.tv_total.setText(activity.getResources().getString(R.string.tv_cart_total) + price * items + " " + activity.getResources().getString(R.string.currency));
                updateintent(dbHandler, view.getContext());
            }
        });

        holder.txt_close.setOnClickListener(view -> {
            dbHandler.removeItemFromCart(map.get("varient_id"),map.get("store_id"));
            list.remove(position);
            notifyDataSetChanged();
            tv_total.setText(session_management.getCurrency() + " " + dbHandler.getTotalAmount());
            updateintent(dbHandler, view.getContext());
        });

        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i = Integer.parseInt(dbHandler.getInCartItemQtys(map.get("varient_id"),map.get("store_id")));
                double price = Double.parseDouble(map.get("price"));
                if ((i - 1) < 0 || (i - 1) == 0) {
                    holder.tv_add.setVisibility(View.VISIBLE);
                    holder.ll_addQuan.setVisibility(View.GONE);
                    holder.tv_contetiy.setText("" + 0);
                    holder.pPrice.setText("" + price);
//                    holder.pMrp.setText("" + mrp);
                } else {
                    holder.tv_contetiy.setText("" + (i - 1));
                    holder.pPrice.setText("" + (price * (i - 1)));
//                    holder.pMrp.setText("" + (mrp * (i - 1)));
                }
                updateMultiply(position, (i - 1));


//                if (holder.minteger == 1) {
//                    holder.minteger = 1;
//                    display(holder.minteger, holder);
//                    holder.ll_addQuan.setVisibility(View.GONE);
//                    holder.tv_add.setVisibility(View.VISIBLE);
//                } else {
//                    holder.minteger = holder.minteger - 1;
//                    display(holder.minteger, holder);
//
//                }

//                decreaseInteger(holder);
//                updateMultiply(holder, map, position);
            }
        });

        holder.iv_plus.setOnClickListener(v -> {
//            increaseInteger(holder);
//            updateMultiply(holder, map, position);

            try {
                holder.tv_add.setVisibility(View.GONE);
                holder.ll_addQuan.setVisibility(View.VISIBLE);
                if (dbHandler == null) {
                    dbHandler = new DatabaseHandler(v.getContext());
                }
                double price = Double.parseDouble(map.get("price"));
                int i = Integer.parseInt(dbHandler.getInCartItemQtys(map.get("varient_id"),map.get("store_id")));
//            cartList.get(position).setpQuan(String.valueOf(i+1));
                holder.tv_contetiy.setText("" + (i + 1));
                holder.pPrice.setText("" + (price * (i + 1)));
//                holder.pMrp.setText("" + (mrp * (i + 1)));
                updateMultiply(position, (i + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }

    private void updateMultiply(int pos, int i) {
        try {

            if (i > 0) {
                dbHandler.setCart(list.get(pos), i);
            } else {
                dbHandler.removeItemFromCart(list.get(pos).get("varient_id"),list.get(pos).get("store_id"));
                list.remove(pos);
                notifyDataSetChanged();
            }
            tv_total.setText(session_management.getCurrency() + " " + dbHandler.getTotalAmount());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
            }
            updateintent(dbHandler, context);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void updateMultiply(ProductHolder holder, HashMap<String, String> map, int position) {




        dbHandler.setCart(map, Integer.valueOf(holder.tv_contetiy.getText().toString()));
        Log.d("asfd", holder.tv_contetiy.getText().toString());


        int items = (int) Double.parseDouble(dbHandler.getInCartItemQty(map.get("varient_id"),map.get("store_id")));
        //  Double price = Double.parseDouble(map.get("price"));
//        Double reward = Double.parseDouble(dbHandler.getInCartItemQty(map.get("price")));
        if (items == 1) {
            holder.tv_contetiy.setText("" + Integer.valueOf(String.valueOf(items)));
//            holder.pPrice.setText("" + holder.price);
            dbHandler.removeItemFromCart(map.get("varient_id"),map.get("store_id"));
            list.remove(position);
            notifyDataSetChanged();
        } else {
            holder.tv_contetiy.setText("" + Integer.valueOf(String.valueOf(items)));
//            holder.pPrice.setText("" + holder.price * items);
        }
        tv_total.setText(session_management.getCurrency() + " " + dbHandler.getTotalAmount());

        //   holder.tv_total.setText(activity.getResources().getString(R.string.tv_cart_total) + price * items + " " + activity.getResources().getString(R.string.currency));
        updateintent(dbHandler, activity.getApplicationContext());
    }

    public void increaseInteger(ProductHolder holder) {
        holder.minteger = holder.minteger + 1;
        display(holder.minteger, holder);
    }

    public void decreaseInteger(ProductHolder holder) {
        if (holder.minteger == 1) {
            holder.minteger = 1;
            display(holder.minteger, holder);
            holder.ll_addQuan.setVisibility(View.GONE);
            holder.tv_add.setVisibility(View.VISIBLE);
        } else {
            holder.minteger = holder.minteger - 1;
            display(holder.minteger, holder);

        }
    }

    private void display(Integer number, ProductHolder holder) {

        holder.tv_contetiy.setText("" + number);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void updateintent(DatabaseHandler dbHandler, Context context) {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();

                if (dbHandler.getCartCount() == 0) {
                    notifier.onViewNotify();
                }
            }
            Intent updates = new Intent("Grocery_cart");
            updates.putExtra("type", "update");
            activity.sendBroadcast(updates);
        }catch (Exception ep){
            ep.printStackTrace();
        }

    }

    class ProductHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, txt_close, tv_contetiy, iv_plus, iv_minus, pDescrptn, pQuan, pPrice, pdiscountOff, pMrp, tv_unit, tv_unit_value, currency_indicator;
        public ImageView iv_logo;
        LinearLayout tv_add, ll_addQuan;
        int minteger = 0;
        int price = 0;
        public TextView tvStoreName;

        public ProductHolder(View view) {
            super(view);

            tvStoreName = view.findViewById(R.id.tv_store_name_cart);

            tv_title = view.findViewById(R.id.txt_pName);
            currency_indicator = view.findViewById(R.id.currency_indicator);
            iv_logo = view.findViewById(R.id.prodImage);

            tv_contetiy = view.findViewById(R.id.txtQuan);
            tv_add = view.findViewById(R.id.btn_Add);
            ll_addQuan = view.findViewById(R.id.ll_addQuan);
            iv_plus = view.findViewById(R.id.plus);
            iv_minus = view.findViewById(R.id.minus);

            pDescrptn = view.findViewById(R.id.txt_pInfo);
            pQuan = view.findViewById(R.id.txt_unit);
            pPrice = view.findViewById(R.id.txt_Pprice);
            pdiscountOff = view.findViewById(R.id.txt_discountOff);
            pMrp = view.findViewById(R.id.txt_Mrp);

            txt_close = view.findViewById(R.id.txt_close);

            //  tv_add.setText(R.string.tv_pro_update);

        }
    }

}

