package com.myshopmate.user.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
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

import com.squareup.picasso.Picasso;
import com.myshopmate.user.ModelClass.NewCategoryVarientList;
import com.myshopmate.user.R;
import com.myshopmate.user.util.Communicator;
import com.myshopmate.user.util.DatabaseHandler;
import com.myshopmate.user.util.Session_management;

import java.util.HashMap;
import java.util.List;

import static com.myshopmate.user.Config.BaseURL.IMG_URL;

public class Adapter_popup extends RecyclerView.Adapter<Adapter_popup.holder> {

    List<NewCategoryVarientList> varientProductList;
    Context context;
    String id;
    int minteger = 0;
    private DatabaseHandler dbcart;
    private Session_management session_management;
    private Communicator communicator;

    public Adapter_popup(Context context, List<NewCategoryVarientList> varientProductList, String id, Communicator communicator) {
        this.varientProductList = varientProductList;
        this.id = id;
        this.dbcart = new DatabaseHandler(context);
        this.context = context;
        this.communicator = communicator;
        session_management = new Session_management(context);
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_popup, viewGroup, false);
        context = viewGroup.getContext();
        if (session_management == null) {
            session_management = new Session_management(context);
        }
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        final NewCategoryVarientList selectAreaModel = varientProductList.get(i);


        if (Integer.parseInt(selectAreaModel.getStock()) > 0) {
            holder.outofs.setVisibility(View.GONE);
            holder.outofs_in.setVisibility(View.VISIBLE);
        } else {
            holder.outofs_in.setVisibility(View.GONE);
            holder.outofs.setVisibility(View.VISIBLE);
        }

        holder.unit.setText(selectAreaModel.getQuantity());

        holder.unitvalue.setText(selectAreaModel.getUnit());
//        holder.mrp.setText("" + selectAreaModel.getVariant_mrp());
        Picasso.with(context).load(IMG_URL + selectAreaModel.getVarient_image()).into(holder.prodImage);

        double price = Double.parseDouble(varientProductList.get(i).getPrice());
        double mrp = Double.parseDouble(varientProductList.get(i).getMrp());

        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(selectAreaModel.getVarient_id(),selectAreaModel.getStore_id()));
        if (qtyd > 0) {
            holder.btn_Add.setVisibility(View.GONE);
            holder.ll_addQuan.setVisibility(View.VISIBLE);
            holder.txtQuan.setText("" + qtyd);
            holder.mprice.setText(session_management.getCurrency() + " " + (price * qtyd));
            holder.mrp.setText(session_management.getCurrency() + " " + (mrp * qtyd));
            holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.btn_Add.setVisibility(View.VISIBLE);
            holder.ll_addQuan.setVisibility(View.GONE);
            holder.mprice.setText(session_management.getCurrency() + " " + selectAreaModel.getPrice());
            holder.mrp.setText(session_management.getCurrency() + " " + selectAreaModel.getMrp());
            holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtQuan.setText("" + 0);
        }
        holder.plus.setOnClickListener(v -> {
            int qnty = (int) Double.parseDouble(dbcart.getInCartItemQty(selectAreaModel.getVarient_id(),selectAreaModel.getStore_id()));
            if (qnty < Integer.parseInt(selectAreaModel.getStock())) {
                holder.btn_Add.setVisibility(View.GONE);
                holder.ll_addQuan.setVisibility(View.VISIBLE);
                holder.txtQuan.setText("" + (qnty + 1));
                holder.mprice.setText("" + (price * (qnty + 1)));
                holder.mrp.setText("" + (mrp * (qnty + 1)));
                updateMultiply(i, holder);
            }
        });
        holder.minus.setOnClickListener(v -> {
            int qnty = (int) Double.parseDouble(dbcart.getInCartItemQty(selectAreaModel.getVarient_id(),selectAreaModel.getStore_id()));
            //            cartList.get(position).setpQuan(String.valueOf(i-1));
//            holder.pMrp.setText(""+(mrp*(qnty -1)) );
            if ((qnty - 1) < 0 || (qnty - 1) == 0) {
                holder.btn_Add.setVisibility(View.VISIBLE);
                holder.ll_addQuan.setVisibility(View.GONE);
                holder.txtQuan.setText("" + 0);
                holder.mprice.setText("" + price);
                holder.mrp.setText("" + mrp);
            } else {
                holder.txtQuan.setText("" + (qnty - 1));
                holder.mprice.setText("" + (price * (qnty - 1)));
                holder.mrp.setText("" + (mrp * (qnty - 1)));
            }
            updateMultiply(i, holder);
        });
        holder.btn_Add.setOnClickListener(v -> {
            holder.btn_Add.setVisibility(View.GONE);
            holder.ll_addQuan.setVisibility(View.VISIBLE);
//            cartList.get(position).setpQuan("1");
            holder.txtQuan.setText("1");
            updateMultiply(i, holder);
        });


//        int varientprice= Integer.valueOf(selectAreaModel.getVariant_price());
//        int varientmrp= Integer.valueOf(selectAreaModel.getVariant_mrp());
//
//        String savingprice=String.valueOf(varientmrp-varientprice);
//
//        holder.varientdiscount.setText("Rs" + " " + savingprice +" " + "off");


    }

    @Override
    public int getItemCount() {
        return varientProductList.size();
    }

    private void updateMultiply(int pos, holder holder) {
        HashMap<String, String> map = new HashMap<>();
        map.put("varient_id", varientProductList.get(pos).getVarient_id());
        map.put("product_name", id);
        map.put("title", id);
        map.put("price", varientProductList.get(pos).getPrice());
//            Log.d("dsfa", CategoryGridList.get(position).getPrice());
        map.put("mrp", varientProductList.get(pos).getMrp());
//            Log.d("fd", CategoryGridList.get(position).getImage());
        map.put("product_image", varientProductList.get(pos).getVarient_image());
//            map.put("status",CategoryGridList.get(position).get());
//            map.put("in_stock",CategoryGridList.get(position).getIn_stock());
        map.put("unit_value", varientProductList.get(pos).getUnit());
        map.put("unit", varientProductList.get(pos).getUnit());
        map.put("increament", "0");
        map.put("rewards", "0");
        map.put("stock", "0");
        map.put("product_description", varientProductList.get(pos).getDescription());
        if (!holder.txtQuan.getText().toString().equalsIgnoreCase("0")) {
            if (dbcart.isInCart(map.get("varient_id"),map.get("store_id"))) {
                dbcart.setCart(map, Integer.valueOf(holder.txtQuan.getText().toString()));
                //  Toast.makeText(context, "Product quantity is updated in your cart", Toast.LENGTH_SHORT).show();

            } else {
                dbcart.setCart(map, Integer.valueOf(holder.txtQuan.getText().toString()));
                //   Toast.makeText(context, "Product quantity is added in your cart", Toast.LENGTH_SHORT).show();

            }
        } else {
            dbcart.removeItemFromCart(map.get("varient_id"),map.get("store_id"));
        }
        try {
//            int items = Integer.parseInt(dbcart.getInCartItemQty(map.get("varient_id")));
//            Double price = Double.parseDouble(map.get("price").trim());
//            Double mrp = Double.parseDouble(map.get("mrp").trim());
            //  Double reward = Double.parseDouble(map.get("rewards"));
            // tv_reward.setText("" + reward * items);
//                pDescrptn.setText(""+CategoryGridList.get(position).getpDes());
//            mprice.setText("" + String.valueOf(price * items));
//            txtQuan.setText("" + items);
//                pMrp.setText("" + mrp * items);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (communicator != null) {
                    communicator.onClick(pos);
                }
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("qwer", e.toString());
        }
    }

    public class holder extends RecyclerView.ViewHolder {

        public TextView prodNAme, pDescrptn, pQuan, pPrice, pdiscountOff, pMrp, minus, plus, txtQuan, txt_unitvalue;
        TextView unit, unitvalue, mprice, mrp;
        ImageView prodImage;
        LinearLayout btn_Add, ll_addQuan, outofs_in, outofs;

        public holder(@NonNull View itemView) {
            super(itemView);

            unit = itemView.findViewById(R.id.unit);
            unitvalue = itemView.findViewById(R.id.unitvalue);

            mprice = itemView.findViewById(R.id.price);
//            mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
            mrp = itemView.findViewById(R.id.producrmrp);
            prodImage = itemView.findViewById(R.id.prodImage);
            btn_Add = itemView.findViewById(R.id.btn_Add);
            outofs_in = itemView.findViewById(R.id.outofs_in);
            outofs = itemView.findViewById(R.id.outofs);
//            rlQuan = itemView.findViewById(R.id.rlQuan);
//            btn_Add = itemView.findViewById(R.id.btn_Add);
            ll_addQuan = itemView.findViewById(R.id.ll_addQuan);
            txtQuan = itemView.findViewById(R.id.txtQuan);
            minus = itemView.findViewById(R.id.minus);
            plus = itemView.findViewById(R.id.plus);
//            btn_Add.setOnClickListener(v -> {
//                btn_Add.setVisibility(View.GONE);
//                ll_addQuan.setVisibility(View.VISIBLE);
//                txtQuan.setText("1");
//                updateMultiply();
//            });
//            plus.setOnClickListener(v -> {
//                increaseInteger();
//                updateMultiply();
//                if (Float.parseFloat(txtQuan.getText().toString()) == 1) {
//                   /* minus.setClickable(false);
//                    minus.setFocusable(false);*/
//                } else if (Float.parseFloat(txtQuan.getText().toString()) > 1) {
//
//                }
//            });
//
//            minus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    decreaseInteger();
//                    updateMultiply();
//                }
//            });

//              minus.setOnClickListener(this);
//               plus.setOnClickListener(this);


        }


        public void increaseInteger() {
            minteger = minteger + 1;
            display(minteger);
        }

        public void decreaseInteger() {
            if (minteger == 1) {
                minteger = 1;
                display(minteger);
                ll_addQuan.setVisibility(View.GONE);
                btn_Add.setVisibility(View.VISIBLE);
            } else {
                minteger = minteger - 1;
                display(minteger);

            }
        }

        private void display(Integer number) {

            txtQuan.setText("" + number);
        }

    }
}
