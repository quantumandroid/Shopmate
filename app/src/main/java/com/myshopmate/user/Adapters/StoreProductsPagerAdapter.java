package com.myshopmate.user.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.myshopmate.user.R;

import java.util.ArrayList;

public class StoreProductsPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Integer> layouts;

    public StoreProductsPagerAdapter(Context context, ArrayList<Integer> layouts) {
        this.context = context;
        this.layouts = layouts;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        /*LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layouts.get(position), collection, false);

        ViewGroup layout = (ViewGroup) view;
        collection.addView(layout);*/

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.rv_home;
                break;
            case 1:
                resId = R.id.rv_home_products;
                break;
        }

        return ((Activity)context).findViewById(resId);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }

    @Override
    public int getCount() {
        return layouts.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
