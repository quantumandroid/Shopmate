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
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.user.Config.BaseURL;
import com.myshopmate.user.ModelClass.Store;
import com.myshopmate.user.R;
import com.squareup.picasso.Picasso;

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
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,category;
        public ImageView image;
        LinearLayout linearLayout ;
        CardView cardview1;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_home_name);
            category = (TextView) view.findViewById(R.id.tv_home_cat);
            image = (ImageView) view.findViewById(R.id.iv_home_icon);
            linearLayout =  view.findViewById(R.id.ll1);
            cardview1 =  view.findViewById(R.id.cardview1);
        }
    }

    public HomeAdapter(List<Store> modelList) {
        this.modelList = modelList;
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
        int clr1 = Color.rgb(red, green, blue);                                 //pastel colors
      //  holder.linearLayout.setBackgroundColor(clr1);

       /* Picasso.with(context)
                .load(BaseURL.IMG_URL + mList.getImage())
                .into(holder.image);*/
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
            holder.title.setText(store.getStore_name());
            holder.category.setText(store.getCategory());



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

