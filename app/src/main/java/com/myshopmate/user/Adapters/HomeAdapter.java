package com.myshopmate.user.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.user.ModelClass.Store;
import com.myshopmate.user.R;
import com.myshopmate.user.util.DistanceCalculator;
import com.myshopmate.user.util.Session_management;
import com.myshopmate.user.util.Utils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private List<Store> modelList;
    private Context context;
    String language;
    SharedPreferences preferences;
    private Session_management session_management;
    DistanceCalculator distanceCalculator;
    DecimalFormat decimalFormat;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,category;
        public ImageView image;
        LinearLayout linearLayout ;
        CardView cardview1;
        TextView tvTime;
        TextView tv_home_status, tv_home_distance;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_home_name);
            category = (TextView) view.findViewById(R.id.tv_home_cat);
            image = (ImageView) view.findViewById(R.id.iv_home_icon);
            linearLayout =  view.findViewById(R.id.ll1);
            cardview1 =  view.findViewById(R.id.cardview1);
            tvTime = view.findViewById(R.id.tv_home_time);
            tv_home_status = view.findViewById(R.id.tv_home_status);
            tv_home_distance = view.findViewById(R.id.tv_home_distance);
        }
    }

    public HomeAdapter(List<Store> modelList, FragmentActivity activity) {
        this.modelList = modelList;
        session_management = new Session_management(activity);
         distanceCalculator = new DistanceCalculator();
        decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home_rv1, parent, false);

        context = parent.getContext();

        return new HomeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.MyViewHolder holder, int position) {
        Store store = modelList.get(position);

        Random rnd = new Random();
        //  int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));  //bright colors
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + rnd.nextInt(256)) / 2;
        final int green = (baseGreen + rnd.nextInt(256)) / 2;
        final int blue = (baseBlue + rnd.nextInt(256)) / 2;
       // int clr1 = Color.rgb(red, green, blue);                                 //pastel colors
      //  holder.linearLayout.setBackgroundColor(clr1);

        if (store.getStore_image_url() != null && !store.getStore_image_url().isEmpty()) {
            Picasso.with(context)
                    .load(store.getStore_image_url())
                    .into(holder.image);
        }
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
            holder.title.setText(store.getStore_name());
            holder.category.setText(store.getCategory());
            holder.tvTime.setText(getTimeStr(store.getOpening_time(),store.getClosing_time()));

            if (isStoreOpen(store.getOpening_time(), store.getClosing_time())){
                holder.tv_home_status.setText("open");
                holder.tv_home_status.setTextColor(context.getResources().getColor(R.color.green_orl));
                holder.tv_home_status.setBackground(context.getResources().getDrawable(R.drawable.bg_rounded_corner_green));
            }else {
                holder.tv_home_status.setText("closed");
                holder.tv_home_status.setTextColor(context.getResources().getColor(R.color.quantum_error_light));
                holder.tv_home_status.setBackground(context.getResources().getDrawable(R.drawable.bg_rounded_corner_red));
            }

            holder.tv_home_distance.setText("around "+getDistance(store.getLat(),store.getLng())+" km");

    }

    private String getDistance(String lat, String lng) {

        double lat1 = Double.parseDouble(lat);
        double lng1 = Double.parseDouble(lng);
        double lat2 = Double.parseDouble(session_management.getLatPref());
        double lng2 = Double.parseDouble(session_management.getLangPref());

        double distance = distanceCalculator.distance(lat1, lng1, lat2, lng2);
        distance *= 2;

        return decimalFormat.format(distance);
    }

    private boolean isStoreOpen(String opening_time, String closing_time) {
        final Calendar calendar = Calendar.getInstance();

        int currentHr = calendar.get(Calendar.HOUR_OF_DAY);

       String ot = Utils.formatDateTimeString(opening_time,"hh:mm","HH");
       String ct = Utils.formatDateTimeString(closing_time,"hh:mm","HH");

        try {
            return Integer.parseInt(ot) <= currentHr && currentHr < Integer.parseInt(ct);
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private String getTimeStr(String opening_time, String closing_time) {
        String timeStr = "";
        timeStr = Utils.formatDateTimeString(opening_time,"hh:mm","hh:mm a");
        timeStr += "  to  " + Utils.formatDateTimeString(closing_time,"hh:mm","hh:mm a");
        return timeStr;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

