package com.myshopmate.user.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.user.Activity.ProductDetails;
import com.myshopmate.user.Categorygridquantity;
import com.myshopmate.user.ModelClass.NewCategoryDataModel;
import com.myshopmate.user.ModelClass.varient_product;
import com.myshopmate.user.R;
import com.myshopmate.user.util.DatabaseHandler;
import com.myshopmate.user.util.Session_management;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.myshopmate.user.Config.BaseURL.IMG_URL;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.MyViewHolder> {

    Context context;
    RecyclerView recyler_popup;
    LinearLayout cancl;
    Categorygridquantity categorygridquantity;
    private List<NewCategoryDataModel> CategoryGridList;
    private DatabaseHandler dbcart;
    private List<varient_product> varientProducts = new ArrayList<>();
    private Session_management session_management;

    public CategoryGridAdapter(List<NewCategoryDataModel> categoryGridList, Context context, Categorygridquantity categorygridquantity) {
        this.CategoryGridList = categoryGridList;
        this.dbcart = new DatabaseHandler(context);
        this.session_management = new Session_management(context);
        this.categorygridquantity = categorygridquantity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_category_list, parent, false);
        context = parent.getContext();
        dbcart = new DatabaseHandler(context);
        if (session_management == null){
            session_management = new Session_management(context);
        }
        return new MyViewHolder(itemView);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewCategoryDataModel cc = CategoryGridList.get(position);

        holder.currency_indicator.setText(session_management.getCurrency());
//        holder.currency_indicator_2.setText(session_management.getCurrency());
        holder.prodNAme.setText(cc.getProduct_name());
        holder.pPrice.setText(cc.getPrice());
        holder.txt_unitvalue.setText(cc.getUnit());
        holder.pQuan.setText(cc.getQuantity());
        holder.pMrp.setText(cc.getMrp());
        holder.pDescrptn.setText(cc.getDescription());
        session_management.setStoreId(cc.getStore_id());

       // if (Integer.parseInt(cc.getStock())>0){
        String inStock = cc.getIn_stock();
        if (inStock != null && inStock.equals("1")){
            holder.outofs.setVisibility(View.GONE);
            holder.outofs_in.setVisibility(View.VISIBLE);
        }else {
            holder.outofs_in.setVisibility(View.GONE);
            holder.outofs.setVisibility(View.VISIBLE);
        }

        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(cc.getVarient_id(),cc.getStore_id()));
        double priced = Double.parseDouble(cc.getPrice());
        double mrpd = Double.parseDouble(cc.getMrp());
        if (qtyd > 0) {
            holder.btn_Add.setVisibility(View.GONE);
            holder.ll_addQuan.setVisibility(View.VISIBLE);
            holder.txtQuan.setText("x" + qtyd);
           /* holder.pPrice.setText("" + (priced * qtyd));
            holder.pMrp.setText("" + (mrpd * qtyd));*/
        } else {
            holder.btn_Add.setVisibility(View.VISIBLE);
            holder.ll_addQuan.setVisibility(View.GONE);
            holder.pPrice.setText(cc.getPrice());
            holder.pMrp.setText(cc.getMrp());
            holder.txtQuan.setText("x0");
        }
        if (cc.getVarient_image() != null && !cc.getVarient_image().isEmpty()) {
            Picasso.get()
                    .load(IMG_URL + cc.getVarient_image())
                    .into(holder.image);
        }

        double discount = Math.round(mrpd - priced);
//        holder.pdiscountOff.setText(session_management.getCurrency() + "" + ((int) (mrpd - priced)) + " " + "Off");
        if (discount > 0) {
            holder.pdiscountOff.setText(session_management.getCurrency() + "" + discount + " " + "Off");
            holder.pMrp.setPaintFlags(holder.pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.pdiscountOff.setVisibility(View.INVISIBLE);
            holder.pMrp.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("sId", cc.getProduct_id());
                intent.putExtra("store_id", cc.getStore_id());
                intent.putExtra("sName", cc.getProduct_name());
                intent.putExtra("descrip", cc.getDescription());
                intent.putExtra("price", cc.getPrice());
                intent.putExtra("mrp", cc.getMrp());
                intent.putExtra("unit", cc.getUnit());
                intent.putExtra("qty", cc.getQuantity());
                intent.putExtra("stock", cc.getStock());
                intent.putExtra("image", cc.getVarient_image());
                intent.putExtra("sVariant_id", cc.getVarient_id());
                intent.putExtra("variants", cc.getVarients());
                intent.putExtra("in_stock", cc.getIn_stock());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        /*Picasso.with(context)
                .load(IMG_URL + cc.getProduct_image())
                .into(holder.image);*/


        holder.rlQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorygridquantity.onClick(view, position, cc.getProduct_id(), cc.getProduct_name());
//                final Dialog dialog = new Dialog(context);
//                Window window = dialog.getWindow();
//                WindowManager.LayoutParams wlp = window.getAttributes();
//
//                wlp.gravity = Gravity.BOTTOM;
//                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//                window.setAttributes(wlp);
//                dialog.setContentView(R.layout.layout_dialog_varient);
//                dialog.setCanceledOnTouchOutside(false);
//                TextView txt = dialog.findViewById(R.id.txt);
//                txt.setText(cc.getName());
//                cancl = dialog.findViewById(R.id.cancl);
//                recyler_popup = dialog.findViewById(R.id.recyclerVarient);
//                recyler_popup.setLayoutManager(new GridLayoutManager(context.getApplicationContext(), 1));
//
//                Varient_product(cc.getId());
//                cancl.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialog.dismiss();
//                    }
//                });
//                recyler_popup.addOnItemTouchListener(new RecyclerTouchListener(context, recyler_popup, new RecyclerTouchListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                        String mrp = varientProducts.get(position).getVariant_mrp();
//                        String price = varientProducts.get(position).getVariant_price();
//                        String vQuan = varientProducts.get(position).getVariant_unit_value();
//                        Log.d("asdff", cc.getPrice());
//
//                        cc.setPrice(price);
//
//                        Log.d("asdff", cc.getPrice());
//                        holder.pMrp.setText(" " + mrp);
//                        holder.pPrice.setText(" " + price);
//                        holder.pQuan.setText(varientProducts.get(position).getVariant_unit());
////                            holder.txtQuan.setText(varientProducts.get(position).getVariant_unit());
//                        holder.txt_unitvalue.setText(varientProducts.get(position).getVariant_unit_value());
//
//                        cc.setVarient_image(varientProducts.get(position).getVarient_imqge());
//                        Glide.with(context)
//                                .load(IMG_URL + varientProducts.get(position).getVarient_imqge())
//                                .centerCrop()
//
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .dontAnimate()
//                                .into(holder.image);
//                        dialog.dismiss();
//
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
//
//                    }
//                }));
//
//
//                dialog.show();
            }
        });

        holder.plus.setOnClickListener(v -> {
            holder.btn_Add.setVisibility(View.GONE);
            holder.ll_addQuan.setVisibility(View.VISIBLE);
            int i = Integer.parseInt(dbcart.getInCartItemQtys(cc.getVarient_id(),cc.getStore_id()));
            if (i<Integer.parseInt(cc.getStock())){
                holder.txtQuan.setText("x" + (i + 1));
               /* holder.pPrice.setText("" + (priced * (i + 1)));
                holder.pMrp.setText("" + (mrpd * (i + 1)));*/
                updateMultiply(position, i + 1);
            }
        });
        holder.minus.setOnClickListener(v -> {
//            int i = Integer.parseInt(cc.getQuantity());
            int i = Integer.parseInt(dbcart.getInCartItemQtys(cc.getVarient_id(),cc.getStore_id()));
//            CategoryGridList.get(position).setQuantity(String.valueOf(i - 1));
            if ((i - 1) < 0 || (i - 1) == 0) {
                holder.btn_Add.setVisibility(View.VISIBLE);
                holder.ll_addQuan.setVisibility(View.GONE);
                holder.txtQuan.setText("x" + 0);
               /* holder.pPrice.setText("" + priced);
                holder.pMrp.setText("" + mrpd);*/
            } else {
                holder.txtQuan.setText("x" + (i - 1));
              /*  holder.pPrice.setText("" + (priced * (i - 1)));
                holder.pMrp.setText("" + (mrpd * (i - 1)));*/
            }
            updateMultiply(position, i - 1);
        });
        holder.btn_Add.setOnClickListener(v -> {
            holder.btn_Add.setVisibility(View.GONE);
            holder.ll_addQuan.setVisibility(View.VISIBLE);
//            CategoryGridList.get(position).setQuantity("1");
            holder.txtQuan.setText("x1");
            updateMultiply(position, 1);
        });

        /*try {
            holder.tvStore.setText(Utils.stores.get(cc.getStore_id()).getStore_name());
        } catch (Exception e) {
            holder.tvStore.setText("");
        }*/

    }

    private void updateMultiply(int pos, int i) {
        HashMap<String, String> map = new HashMap<>();
//            map.put("varient_id",cartList.get(position).getpId());
        map.put("varient_id", CategoryGridList.get(pos).getVarient_id());
        map.put("store_id", CategoryGridList.get(pos).getStore_id());
        map.put("product_name", CategoryGridList.get(pos).getProduct_name());
        map.put("category_id", CategoryGridList.get(pos).getProduct_id());
        map.put("title", CategoryGridList.get(pos).getProduct_name());
        map.put("price", CategoryGridList.get(pos).getPrice());
        map.put("mrp", CategoryGridList.get(pos).getMrp());
//        Log.d("fd",CategoryGridList.get(pos).getImage());
        map.put("product_image", CategoryGridList.get(pos).getVarient_image());
        map.put("status", "0");
        map.put("in_stock", "");
        map.put("unit_value", CategoryGridList.get(pos).getQuantity()+""+(CategoryGridList.get(pos).getUnit() != null ? CategoryGridList.get(pos).getUnit() : ""));
        map.put("unit", CategoryGridList.get(pos).getUnit() != null ? CategoryGridList.get(pos).getUnit() : "");
        map.put("increament", "0");
        map.put("rewards", "0");
        map.put("stock", "0");
        map.put("product_description", CategoryGridList.get(pos).getDescription());

//        Log.d("fgh",cartList.get(position).getUnit()+cartList.get(position).getpQuan());
//        Log.d("fghfgh",cartList.get(position).getpPrice());
        if (i > 0) {
            if (dbcart.isInCart(map.get("varient_id"),map.get("store_id"))) {
                dbcart.setCart(map, i);
            } else {
                dbcart.setCart(map, i);
            }
        } else {
            dbcart.removeItemFromCart(map.get("varient_id"),map.get("store_id"));
        }
        try {
//            int items = (int) Double.parseDouble(dbcart.getInCartItemQty(map.get("varient_id")));
//            double price = Double.parseDouble(Objects.requireNonNull(map.get("price")).trim());
//            double mrp = Double.parseDouble(Objects.requireNonNull(map.get("mrp")).trim());
            //  Double reward = Double.parseDouble(map.get("rewards"));
            // tv_reward.setText("" + reward * items);
//            pDescrptn.setText(""+cartList.get(position).getpDes());
//            pPrice.setText("" +price* items);
//            txtQuan.setText("" + items);
//            pMrp.setText("" + mrp* items );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
                categorygridquantity.onCartItemAddOrMinus();
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("qwer", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return CategoryGridList.size();
    }


//    private void Varient_product(String pId) {
//        varientProducts.clear();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, ProductVarient, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("Prod detail", response);
//                try {
//                    varientProducts.clear();
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//                    if (status.equals("1")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                            String product_id = jsonObject1.getString("product_id");
//                            String varient_id = jsonObject1.getString("varient_id");
//                            String price = jsonObject1.getString("price");
//                            String quantity = jsonObject1.getString("quantity");
//                            String varient_image = jsonObject1.getString("varient_image");
//                            String mrp = jsonObject1.getString("mrp");
//                            String unit = jsonObject1.getString("unit");
//                            String description = jsonObject1.getString("description");
//
//
//                            // Picasso.get().load(IMG_URL+varient_image).into(pImage);
//                            //prodMrp.setText(mrp);
//
//                            varient_product selectCityModel = new varient_product();
//                            selectCityModel.setVarient_imqge(IMG_URL + varient_image);
//                            selectCityModel.setVariant_unit_value(unit);
//                            selectCityModel.setVariant_price(price);
//                            selectCityModel.setVariant_mrp(mrp);
//                            selectCityModel.setVariant_unit(quantity);
//                            selectCityModel.setVariant_id(varient_id);
//
//
//                            varientProducts.add(selectCityModel);
//
//                            Adapter_popup selectCityAdapter = new Adapter_popup(varientProducts);
//                            recyler_popup.setAdapter(selectCityAdapter);
//
//
//                        }
//
//                    } else {
//                        varientProducts.clear();
//                        //JSONObject resultObj = jsonObject.getJSONObject("results");
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<>();
//                params.put("product_id", pId);
//                Log.d("kj", pId);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.getCache().clear();
//        requestQueue.add(stringRequest);
//    }

    //    public CategoryGridAdapter(Context context, List<CategoryGrid> categoryGridList, Categorygridquantity categorygridquantity) {
//        CategoryGridList = categoryGridList;
//        dbcart = new DatabaseHandler(context);
//        this.categorygridquantity = categorygridquantity;
//
//    }
//
//
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView prodNAme, pDescrptn, pQuan, pPrice, pdiscountOff, pMrp, minus, plus, txtQuan, txt_unitvalue, currency_indicator; //, currency_indicator_2;
        ImageView image;
        LinearLayout btn_Add, ll_addQuan,outofs_in,outofs;
        int minteger = 0;
        RelativeLayout rlQuan;
        String catId, catName;
       // TextView tvStore;

        public MyViewHolder(View view) {
            super(view);
            prodNAme = view.findViewById(R.id.txt_pName);
            currency_indicator = view.findViewById(R.id.currency_indicator);
//            currency_indicator_2 = view.findViewById(R.id.currency_indicator_2);
            pDescrptn = view.findViewById(R.id.txt_pInfo);
            pQuan = view.findViewById(R.id.txt_unit);
            pPrice = view.findViewById(R.id.txt_Pprice);
            image = view.findViewById(R.id.prodImage);
            pdiscountOff = view.findViewById(R.id.txt_discountOff);
            pMrp = view.findViewById(R.id.txt_Mrp);
            rlQuan = view.findViewById(R.id.rlQuan);
            btn_Add = view.findViewById(R.id.btn_Add);
            ll_addQuan = view.findViewById(R.id.ll_addQuan);
            txtQuan = view.findViewById(R.id.txtQuan);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
            txt_unitvalue = view.findViewById(R.id.txt_unitvalue);
            ll_addQuan = view.findViewById(R.id.ll_addQuan);
            outofs_in = view.findViewById(R.id.outofs_in);
            outofs = view.findViewById(R.id.outofs);
//            btn_Add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    btn_Add.setVisibility(View.GONE);
//                    ll_addQuan.setVisibility(View.VISIBLE);
//                    txtQuan.setText("1");
//                    updateMultiply();
//                }
//            });
//            plus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    increaseInteger();
//                    updateMultiply();
//
//                    if (Float.parseFloat(txtQuan.getText().toString()) == 1) {
//                       /* minus.setClickable(false);
//                        minus.setFocusable(false);*/
//                    } else if (Float.parseFloat(txtQuan.getText().toString()) > 1) {
//                        minus.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                decreaseInteger();
//                                updateMultiply();
//                            }
//                        });
//                    }
//                }
//            });
//              minus.setOnClickListener(this);
//               plus.setOnClickListener(this);

           // tvStore = view.findViewById(R.id.tv_store);

        }


    }
}
