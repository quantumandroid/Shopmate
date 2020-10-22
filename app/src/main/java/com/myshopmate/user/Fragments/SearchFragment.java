package com.myshopmate.user.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.myshopmate.user.Activity.ProductDetails;
import com.myshopmate.user.Adapters.SearchProdcutAdapter;
import com.myshopmate.user.Constans.RecyclerTouchListener;
import com.myshopmate.user.ModelClass.SearchProductModel;
import com.myshopmate.user.ModelClass.SubProductList;
import com.myshopmate.user.R;
import com.myshopmate.user.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.myshopmate.user.Config.BaseURL.Search;

public class SearchFragment extends Fragment {

    RecyclerView recyclerSearch;
    EditText txtSearch;
    ProgressDialog progressDialog;
    SearchProdcutAdapter searchAdapter;
    List<SearchProductModel> searchlist = new ArrayList<>();
    String seaarchId, seaarchName;
    private Session_management session_management;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerSearch = view.findViewById(R.id.recyclerSearch);
        txtSearch = view.findViewById(R.id.txtSearch);
        session_management = new Session_management(container.getContext());
        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        if (isOnline()) {
            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (txtSearch.getText().length() > 2) {
                        searchlist.clear();
                        searchUrl(txtSearch.getText().toString().trim());
                    }
//                    if (s.toString().length() >= 2) {
//
//                    } else if (s.toString().length() < 2) {
//                        searchlist.clear();
//                        if (searchAdapter != null) {
//                            searchAdapter.notifyDataSetChanged();
//                        }
//                    }
                }
            });
        }

        recyclerSearch.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerSearch, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                seaarchId = searchlist.get(position).getId();
                seaarchName = searchlist.get(position).getpNAme();
                Intent intent = new Intent(getActivity(), ProductDetails.class);
                intent.putExtra("sId", seaarchId);
                intent.putExtra("sName", seaarchName);
                intent.putExtra("sVariant_id", searchlist.get(position).getSubprodList().getVarient_id());
                intent.putExtra("store_id", searchlist.get(position).getSubprodList().getStore_id());
                intent.putExtra("descrip", searchlist.get(position).getSubprodList().getDescription());
                intent.putExtra("price", searchlist.get(position).getSubprodList().getPrice());
                intent.putExtra("mrp", searchlist.get(position).getSubprodList().getMrp());
                intent.putExtra("unit", searchlist.get(position).getSubprodList().getUnit());
                intent.putExtra("stock", searchlist.get(position).getSubprodList().getStock());
                intent.putExtra("qty", searchlist.get(position).getSubprodList().getQuantity());
                intent.putExtra("image", searchlist.get(position).getSubprodList().getVarient_image());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }

    private void searchUrl(final String name) {
        progressDialog.show();
        searchlist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Search, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Seach state..", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        searchlist.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String product_id = jsonObject1.getString("product_id");
                            String product_name = jsonObject1.getString("product_name");
//                            String cat_id = jsonObject1.getString("cat_id");
//                            String product_image = jsonObject1.getString("product_image");

                            JSONArray arra1 = jsonObject1.getJSONArray("varients");

                            for (int j = 0; j < arra1.length(); j++) {
                                JSONObject jsonObject12 = arra1.getJSONObject(j);
                                String varient_id = jsonObject12.getString("varient_id");
//                                String product_id1 = jsonObject12.getString("product_id");
                                String quantity = jsonObject12.getString("quantity");
                                String unit = jsonObject12.getString("unit");
                                String mrp = jsonObject12.getString("mrp");
                                String price = jsonObject12.getString("price");
                                String description = jsonObject12.getString("description");
                                String stock = jsonObject12.getString("stock");
                                String varient_image = jsonObject12.getString("varient_image");
                                String store_id = jsonObject12.getString("store_id");

                                SearchProductModel ss = new SearchProductModel(product_id, product_name, new SubProductList(varient_id, product_id, quantity, unit, mrp, price, description, varient_image, stock, store_id));
                                searchlist.add(ss);
                            }

                        }
                        searchAdapter = new SearchProdcutAdapter(searchlist);
                        recyclerSearch.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                        recyclerSearch.setAdapter(searchAdapter);
                        searchAdapter.notifyDataSetChanged();

                    } else {
                        searchAdapter = new SearchProdcutAdapter(searchlist);
                        recyclerSearch.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                        recyclerSearch.setAdapter(searchAdapter);
                        searchAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                searchAdapter = new SearchProdcutAdapter(searchlist);
                recyclerSearch.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                recyclerSearch.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("keyword", name);
                params.put("lat", session_management.getLatPref());
                params.put("lng", session_management.getLangPref());
                params.put("city", session_management.getLocationCity());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 90000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
