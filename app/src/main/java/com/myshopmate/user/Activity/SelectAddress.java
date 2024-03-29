package com.myshopmate.user.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.myshopmate.user.Config.BaseURL;
import com.myshopmate.user.ModelClass.DeliveryModel;
import com.myshopmate.user.R;
import com.myshopmate.user.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.myshopmate.user.Config.BaseURL.SelectAddressURL;
import static com.myshopmate.user.Config.BaseURL.ShowAddress;

public class SelectAddress extends AppCompatActivity {
    private static final String TAG = SelectAddress.class.getName();
    Session_management session_management;
    LinearLayout back,addAddress,delieverHere;
    RecyclerView recycleraddressList;
    ProgressDialog progressDialog;
     List<DeliveryModel> dlist;
    DeliveryAdapter deliveryAdapter;
    String dName,dId ,addId;
    String user_id;
    private int lastSelectedPosition = -1;
    private boolean show_add = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        dlist = new ArrayList<>();
        init();
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        back=findViewById(R.id.back);
        addAddress=findViewById(R.id.addAdreess);
        delieverHere=findViewById(R.id.Btn_DeliverHere);
        recycleraddressList=findViewById(R.id.recycleraddressList);

        back.setOnClickListener(v -> {
            show_add = false;
            onBackPressed();
        });
        session_management = new Session_management(getApplicationContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);

        addAddress.setOnClickListener(v -> {
            if (!user_id.equalsIgnoreCase("")){
                Intent In=new Intent(getApplicationContext(),add_address.class);
                startActivityForResult(In,21);
            }else {
                Intent In=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(In);
                finish();
            }
        });
        delieverHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
            }
        });

        showAdreesUrl();
    }
    private void showAdreesUrl() {
        dlist.clear();
        progressDialog.show();
if (!user_id.equalsIgnoreCase("")){
    StringRequest stringRequest = new StringRequest(Request.Method.POST, ShowAddress, response -> {
        Log.d("adresssHoww",response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("message");
            if (status.equals("1")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String address_id = jsonObject1.getString("address_id");
                    String user_id = jsonObject1.getString("user_id");
                    String receiver_name = jsonObject1.getString("receiver_name");
                    String receiver_phone = jsonObject1.getString("receiver_phone");
                    String cityyyy = jsonObject1.getString("city");
                    String society = jsonObject1.getString("society");
                    String house_no = jsonObject1.getString("house_no");
                    String pincode = jsonObject1.getString("pincode");
                    String stateeee = jsonObject1.getString("state");
                    String landmark2 = jsonObject1.getString("landmark");
                    int select_status = jsonObject1.getInt("select_status");
                    DeliveryModel ss=new DeliveryModel(address_id,receiver_name,receiver_phone,house_no+", "+society
                            +","+cityyyy+", "+stateeee+", "+pincode);
                    ss.setCity_name(cityyyy);
                    ss.setHouse_no(house_no);
                    ss.setLandmark(landmark2);
                    ss.setPincode(pincode);
                    ss.setState(stateeee);
                    ss.setReceiver_phone(receiver_phone);
                    ss.setReceiver_name(receiver_name);
                    ss.setId(address_id);
                    ss.setSelect_status(select_status);

                    ss.setSociety(society);
                    dlist.add(ss);
                }
                deliveryAdapter = new DeliveryAdapter(dlist);
                recycleraddressList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recycleraddressList.setAdapter(deliveryAdapter);
                deliveryAdapter.notifyDataSetChanged();

            }
            else {
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    /*Save.setVisibility(View.VISIBLE);
                    EditBtn.setVisibility(View.GONE);
                    saveAddress(cityId,socetyId,landmaarkkkk);*/
            }
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            progressDialog.dismiss();
        }
    }, error -> progressDialog.dismiss()){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String,String> param = new HashMap<>();
            param.put("user_id",user_id);
            param.put("store_id",session_management.getStoreId());
            return param;
        }
    };

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
    RequestQueue requestQueue = Volley.newRequestQueue(SelectAddress.this);
    requestQueue.getCache().clear();
    requestQueue.add(stringRequest);
}else {
    progressDialog.dismiss();
}



    }

    private void selectAddrsUrl(String id) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SelectAddressURL, response -> {
            Log.d("selectAdres",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String msg = jsonObject.getString("message");
                if (status.equals("1")){

//                    Intent intent=new Intent(getApplicationContext(),OrderSummary.class);
//                    startActivity(intent);
//                    finish();

                    show_add = true;
                    onBackPressed();
                }
                else {
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }, error -> {
            Log.e(TAG, "onErrorResponse: "+error );
            Toast.makeText(SelectAddress.this, "Server error ", Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("address_id",id);



                return param;
            }
        };
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.MyViewHolder> {
        private List<DeliveryModel> dlist;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, phone, address;
            ImageView edit_address ;
            RadioButton radioButton;
            LinearLayout edit,layout;

            public MyViewHolder(View view) {
                super(view);
                radioButton=view.findViewById(R.id.radioButton);
                layout = view.findViewById(R.id.layout);
                edit_address = view.findViewById(R.id.edit_address);
                name = (TextView) view.findViewById(R.id.txt_name_myhistoryaddrss_item);
                phone = (TextView) view.findViewById(R.id.txt_mobileno_myaddrss_history);
                address = (TextView) view.findViewById(R.id.txt_address_myaddrss_history);
                edit=view.findViewById(R.id.edit);
            }}

        public DeliveryAdapter(List<DeliveryModel> dlist) {
            this.dlist = dlist;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_deliveryaddress, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            DeliveryModel dd = dlist.get(position);
            holder.name.setText(dd.getName());
            holder.address.setText(dd.getAddress());
            holder.phone.setText(dd.getPhone());

//            if (dlist.get(position).getSelect_status()==1){
//                //selected
//                holder.radioButton.setChecked(true);
//            } else {
//                holder.radioButton.setChecked(false);
//            }

            holder.radioButton.setChecked(lastSelectedPosition == position);

            holder.layout.setOnClickListener(v -> {

                Intent intent=new Intent(getApplicationContext(), OrderSummary.class);
                intent.putExtra("dId",dd.getId());
                intent.putExtra("dName",dd.getName());
//                intent.putExtra("selectedadrs",dd);
                startActivity(intent);
            });
            holder.edit.setOnClickListener(v -> {
                Intent i = new Intent(SelectAddress.this, AddAddress.class);
                i.putExtra("update","UPDATE");
                i.putExtra("addId",dd.getId());
                i.putExtra("city_name",dd.getCity_name());
                i.putExtra("society",dd.getSociety());
                i.putExtra("receiver_name",dd.getReceiver_name());
                i.putExtra("receiver_phone",dd.getReceiver_phone());
                i.putExtra("house_no",dd.getHouse_no());
                i.putExtra("landmark",dd.getLandmark());
                i.putExtra("state",dd.getState());
                i.putExtra("pincode",dd.getPincode());
                Log.d("ff",dd.getId());
                startActivity(i);
            });
            holder.radioButton.setOnClickListener(v -> {

                lastSelectedPosition = position;
//                dlist.get(position).getId();
                selectAddrsUrl(dlist.get(position).getId());
                notifyDataSetChanged();
            });
        }
        @Override
        public int getItemCount() {
            return dlist.size();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("show_address",show_add);
        setResult(2,intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        }else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 21){
            if (data!=null && data.getBooleanExtra("select_address",false)){
                showAdreesUrl();
            }
        }
    }
}
