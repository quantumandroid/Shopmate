package com.myshopmate.user.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.user.Activity.LoginActivity;
import com.myshopmate.user.Activity.MainActivity;
import com.myshopmate.user.Activity.OrderSummary;
import com.myshopmate.user.Adapters.Cart_adapter;
import com.myshopmate.user.Adapters.NewCartAdapter;
import com.myshopmate.user.ModelClass.CartModel;
import com.myshopmate.user.R;
import com.myshopmate.user.util.DatabaseHandler;
import com.myshopmate.user.util.Session_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartFragment extends Fragment {

    public TextView tv_total;
    Button btn_ShopNOw;
    RecyclerView recyclerView;
    LinearLayout ll_Checkout;
    NewCartAdapter cartAdapter;
    RelativeLayout noData, viewCart;
    TextView totalItems;
    private List<CartModel> cartList = new ArrayList<>();
    private DatabaseHandler db;
    private Session_management sessionManagement;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.recyclerCart);
        btn_ShopNOw = view.findViewById(R.id.btn_ShopNOw);
        viewCart = view.findViewById(R.id.viewCartItems);
        tv_total = view.findViewById(R.id.txt_totalamount);
        totalItems = view.findViewById(R.id.txt_totalQuan);
        noData = view.findViewById(R.id.noData);
        sessionManagement = new Session_management(getActivity());
        sessionManagement.cleardatetime();
        db = new DatabaseHandler(getActivity());

        ll_Checkout = view.findViewById(R.id.ll_Checkout);
        btn_ShopNOw.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        ll_Checkout.setOnClickListener(v -> {
            if (isOnline()) {
                if (sessionManagement.isLoggedIn()) {

                    if (sessionManagement.userBlockStatus().equalsIgnoreCase("2")) {
                        if (db.getCartCount() == 0) {
                            noData.setVisibility(View.VISIBLE);
                            viewCart.setVisibility(View.GONE);
                        } else {
                            Intent intent = new Intent(getActivity(), OrderSummary.class);
                            startActivity(intent);
                        }
                    } else {
                        showBloackDialog();
                    }
                } else {
//                if (db.getCartCount() == 0) {
//                    noData.setVisibility(View.VISIBLE);
//                    viewCart.setVisibility(View.GONE);
//                } else {
//                    Intent intent = new Intent(getActivity(), OrderSummary.class);
//                    startActivity(intent);
//                }

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        db = new DatabaseHandler(getActivity());

        if (sessionManagement.isLoggedIn()) {

            if (db.getCartCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                viewCart.setVisibility(View.GONE);
            }
        } else {
            if (db.getCartCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                viewCart.setVisibility(View.GONE);
            }
        }
        ArrayList<HashMap<String, String>> map = db.getCartAll();

        Cart_adapter adapter = new Cart_adapter(getActivity(), map, () -> {
            updateData();
            if (db.getCartCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                viewCart.setVisibility(View.GONE);
            }
        }, tv_total, totalItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateData();
        return view;


    }


    public void updateData() {
        tv_total.setText(sessionManagement.getCurrency() + " " + db.getTotalAmount());
        totalItems.setText("" + db.getCartCount() + "  Total Items:");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // ((MainActivity) getActivity()).setCartCounter("" + db.getCartCount());
//        }

    }

    private void showBloackDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setCancelable(true);
//        alertDialog.setMessage("You are blocked from backend.\n Please Contact with customer care!");
        alertDialog.setMessage(sessionManagement.getUserBlockStatusMsg());
//        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

//    private void minmax() {
//        String tag_json_obj = "json_order_detail_req";
//
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET, BaseURL.MinMaxOrder, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//
//                try {
//                    // Parsing json array response
//                    // loop through each json object
//
//                    String status = response.getString("status");
//                    String message = response.getString("message");
//                    if (status.contains("1")) {
//
//                        JSONObject jsonObject = response.getJSONObject("data");
//
//                       String min_value = jsonObject.getString("min_value");
//                        String max_value = jsonObject.getString("max_value");
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }


}
