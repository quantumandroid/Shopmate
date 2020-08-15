package com.myshopmate.user.util;

import com.myshopmate.user.ModelClass.NewPendingDataModel;

import java.util.ArrayList;

public interface ForReorderListner {

    void onReorderClick(ArrayList<NewPendingDataModel> pastOrderSubModelArrayList);
}
