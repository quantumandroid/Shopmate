package com.myshopmate.user.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.myshopmate.user.Fragments.My_Past_Order;
import com.myshopmate.user.Fragments.My_Pending_Order;
import com.myshopmate.user.util.CallToDeliveryBoy;
import com.myshopmate.user.util.ForReorderListner;
import com.myshopmate.user.util.MyPendingReorderClick;


public class PagerOrderAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private ForReorderListner forReorderListner;
    private MyPendingReorderClick myPendingReorderClick;
    private CallToDeliveryBoy callToDeliveryBoy;

    public PagerOrderAdapter(FragmentManager fm, int NumOfTabs, ForReorderListner forReorderListner, MyPendingReorderClick myPendingReorderClick, CallToDeliveryBoy callToDeliveryBoy) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.forReorderListner = forReorderListner;
        this.myPendingReorderClick = myPendingReorderClick;
        this.callToDeliveryBoy = callToDeliveryBoy;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new My_Pending_Order(myPendingReorderClick,callToDeliveryBoy);
            case 1:
                return new My_Past_Order(forReorderListner);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}