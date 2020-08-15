package com.myshopmate.user.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.user.ModelClass.Country;
import com.myshopmate.user.R;
import com.myshopmate.user.util.CommunicatorFlag;

import java.util.List;

public class FlagAdapter extends RecyclerView.Adapter<FlagAdapter.MySubView> {

    private Context context;
    private List<Country> countryList;
    private List<Country> searchCountryList;
    private CommunicatorFlag communicator;

    public FlagAdapter(Context context, List<Country> countryList, List<Country> searchCountryList, CommunicatorFlag communicator) {
        this.context = context;
        this.countryList = countryList;
        this.searchCountryList = searchCountryList;
        this.communicator = communicator;
    }

    @NonNull
    @Override
    public MySubView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flag_country_itemview, parent, false);
        return new MySubView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MySubView holder, int position) {
        Country country = countryList.get(position);

        holder.flagView.setImageDrawable(context.getResources().getDrawable(country.getFlag()));
        holder.countryName.setText(country.getName());
        holder.countryCode.setText(""+country.getDialCode());

        holder.itemView.setOnClickListener(view -> communicator.onClick(country.getDialCode(),country.getFlag()));

    }

    public void filterList(String charSequence){
        countryList.clear();
        if (charSequence.length()==0){
            countryList.addAll(searchCountryList);
        }else {
            for (int i = 0; i < searchCountryList.size(); i++) {
                if (containsChar(searchCountryList.get(i).getDialCode(), charSequence) || containsChar(searchCountryList.get(i).getName(), charSequence)) {
                    countryList.add(searchCountryList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    private boolean containsChar(String s, String charText) {

        if (s.length() == 0)
            return false;
        else
            return s.toLowerCase().contains(charText);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class MySubView extends RecyclerView.ViewHolder {
        ImageView flagView;
        TextView countryName;
        TextView countryCode;

        public MySubView(@NonNull View itemView) {
            super(itemView);

            flagView = itemView.findViewById(R.id.country_flag);
            countryName = itemView.findViewById(R.id.country_name);
            countryCode = itemView.findViewById(R.id.country_code);
        }
    }
}
