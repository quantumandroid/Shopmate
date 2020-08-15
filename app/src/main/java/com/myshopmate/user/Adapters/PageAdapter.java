package com.myshopmate.user.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.myshopmate.user.Fragments.Deals_Fragment;
import com.myshopmate.user.Fragments.Recent_Details_Fragment;
import com.myshopmate.user.Fragments.Top_Deals_Fragment;
import com.myshopmate.user.Fragments.Whats_New_Fragment;

public class PageAdapter extends FragmentPagerAdapter {
    private int numsoftabs;
//    private PagerNotifier pagerNotifier;
//    private boolean data = true;

    public PageAdapter(FragmentManager fm, int numsoftabs) {
        super(fm);
        this.numsoftabs = numsoftabs;
//        this.pagerNotifier = pagerNotifier;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0 :
                return new Top_Deals_Fragment();
            case 1:
                return new Recent_Details_Fragment();
            case 2:
                return new Deals_Fragment();
            case 3:
                return new Whats_New_Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numsoftabs;
    }

//    public void setDataNotifier(boolean data) {
//        this.data = data;
//    }
}
