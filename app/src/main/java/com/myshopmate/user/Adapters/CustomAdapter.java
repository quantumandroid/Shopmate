package com.myshopmate.user.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myshopmate.user.ModelClass.SearchModel;
import com.myshopmate.user.R;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int flags[];
    String[] countryNames;
    LayoutInflater inflter;
    List<SearchModel> citylist;
    public CustomAdapter(Context applicationContext, List<SearchModel> citylist) {
        this.context = applicationContext;
        this.flags = flags;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
        this.citylist=citylist;
    }



    @Override
    public int getCount() {
        return citylist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
//        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
//        view=inflter.inflate(R.layout.cus)
        TextView textcities = (TextView) view.findViewById(R.id.textcities);
//        icon.setImageResource(flags[i]);
        textcities.setText(citylist.get(i).getpNAme());

        return view;
    }
}
