package com.myshopmate.user.upipay;

import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;

public class UPIPay implements Serializable {
    private final String TAG = "UPIPay";
    private TransactionDetails transactionDetails;
    public static UPIPayListener upiPayListener;
    private Activity context;

    public UPIPay(Activity context, TransactionDetails transactionDetails, UPIPayListener upiPayListener_) {
        this.transactionDetails = transactionDetails;
        upiPayListener = upiPayListener_;
        this.context = context;
    }

    public void startPayment() throws Exception {
        Intent upiPayIntent = new Intent(context, UPIPayActivity.class);
        upiPayIntent.putExtra("transaction_details", transactionDetails);
        context.startActivity(upiPayIntent);
        //upiPayActivity.buildPaymentRequest(context,upiPayListener,transactionDetails);
    }
}
