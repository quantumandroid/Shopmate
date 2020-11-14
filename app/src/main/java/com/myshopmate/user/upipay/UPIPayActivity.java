package com.myshopmate.user.upipay;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.myshopmate.user.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.regex.Pattern;

import static com.myshopmate.user.upipay.UPIPay.upiPayListener;

public class UPIPayActivity extends AppCompatActivity {

    private final int UPI_PAYMENT_REQUEST_CODE = 1011;
    private final String TAG = "UPIPay";
    private String errorMsg;
    private TransactionDetails transactionDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_p_i_pay);

        errorMsg = "";
        transactionDetails = (TransactionDetails) getIntent().getSerializableExtra("transaction_details");
        if (transactionDetails == null && upiPayListener == null) {
            //showToastMessage("Something went wrong");
            Log.e(TAG, "TransactionDetails and/or UPIPayListener not set");
        } else {
            buildPaymentRequest();
        }
    }

    public void buildPaymentRequest() {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("upi");
        uriBuilder.authority("pay");
        uriBuilder.appendQueryParameter("cu", "INR");
        if (isNotValidPayeeName()) {
            //showToastMessage(errorMsg);
            upiPayListener.onError(errorMsg);
            errorMsg = "";
            finish();
            return;
        } else {
            uriBuilder.appendQueryParameter("pn", transactionDetails.getPayeeName());
        }
        if (isNotValidPayeeUPIAddress()) {
            //showToastMessage(errorMsg);
            upiPayListener.onError(errorMsg);
            errorMsg = "";
            finish();
            return;
        } else {
            //uriBuilder.appendQueryParameter("pa", transactionDetails.getPayeeUPIAddress());
            uriBuilder.appendQueryParameter("pa", "7350119381@upi");
        }
        if (isNotValidAmount()) {
            //showToastMessage(errorMsg);
            upiPayListener.onError(errorMsg);
            errorMsg = "";
            finish();
            return;
        } else {
            try {
                //uriBuilder.appendQueryParameter("am", String.valueOf(Double.parseDouble(transactionDetails.getAmount())));
                uriBuilder.appendQueryParameter(
                        "am",
                        new DecimalFormat("0.00").format(Double.parseDouble(transactionDetails.getAmount()))
                );
            } catch (NumberFormatException e) {
                upiPayListener.onError("Enter valid amount");
                finish();
                return;
            }
        }
        if (isTransactionRefIDPresent()) {
            uriBuilder.appendQueryParameter("tr", transactionDetails.getTransactionRefID());
        } else {
            Log.e(TAG, "Required transaction reference id");
            upiPayListener.onError("Required transaction reference id");
            finish();
            return;
        }
        if (isMerchantCodePresent()) {
            uriBuilder.appendQueryParameter("mc", transactionDetails.getMerchantCode());
        } else {
            Log.e(TAG, "Required merchant code");
            upiPayListener.onError("Required merchant code");
            finish();
            return;
        }
        if (isTransactionIDPresent()) {
            uriBuilder.appendQueryParameter("tid", transactionDetails.getTransactionID());
        }
        if (isDescriptionPresent()) {
            uriBuilder.appendQueryParameter("tn", transactionDetails.getDescription());
        }
        startPaymentFlow(uriBuilder);
    }

    private void startPaymentFlow(Uri.Builder builder) {
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(builder.build());
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null == chooser.resolveActivity(getPackageManager())) {
            //showToastMessage("No UPI app found, please install one to continue");
            upiPayListener.onUPIAppNotFound();
        } else {
            startActivityForResult(chooser, UPI_PAYMENT_REQUEST_CODE);
        }
    }

    private void showToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        errorMsg = "";
    }

    private boolean isDescriptionPresent() {
        return transactionDetails.getDescription() != null && !transactionDetails.getDescription().isEmpty();
    }

    private boolean isTransactionIDPresent() {
        return transactionDetails.getTransactionID() != null && !transactionDetails.getTransactionID().isEmpty();
    }

    private boolean isTransactionRefIDPresent() {
        return transactionDetails.getTransactionRefID() != null && !transactionDetails.getTransactionRefID().isEmpty();
    }

    private boolean isMerchantCodePresent() {
        return transactionDetails.getMerchantCode() != null; //&& !transactionDetails.getMerchantCode().isEmpty();
    }

    private boolean isNotValidAmount() {
        if (transactionDetails.getAmount() == null || transactionDetails.getAmount().isEmpty()) {
            errorMsg = "Required amount";
            return true;
        }
        errorMsg = "";
        return false;
    }

    private boolean isNotValidPayeeName() {
        if (transactionDetails.getPayeeName() == null || transactionDetails.getPayeeName().isEmpty()) {
            errorMsg = "Required payee name";
            return true;
        }
        errorMsg = "";
        return false;
    }

    private boolean isNotValidPayeeUPIAddress() {
        if (transactionDetails.getPayeeUPIAddress() == null || transactionDetails.getPayeeUPIAddress().isEmpty()) {
            errorMsg = "Required payee UPI address";
            return true;
        } else if (Pattern.matches("[\\w-.]+@([\\w-])+", transactionDetails.getPayeeUPIAddress())) {
            errorMsg = "";
            return false;
        } else {
            errorMsg = "Enter valid UPI address (For e.g. example@vpa)";
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPI_PAYMENT_REQUEST_CODE) {
            if ((RESULT_OK == resultCode)) {
                if (data == null) {
                    upiPayListener.onCancelledByUser();
                    Log.e(TAG, "intent data is null");
                } else {
                    try {
                        TransactionDetails transactionDetails = getTransactionDetails(data.getStringExtra("response"));
                        switch (transactionDetails.getStatus().toUpperCase()) {
                            case "SUBMITTED":
                                upiPayListener.onTransactionSubmitted(transactionDetails);
                                break;
                            case "SUCCESS":
                                upiPayListener.onTransactionSuccess(transactionDetails);
                                break;
                            case "FAILURE":
                                upiPayListener.onTransactionFailed(transactionDetails);
                                break;
                        }
                    } catch (Exception e) {
                        upiPayListener.onError("Something went wrong");
                        Log.e(TAG, e.getMessage());
                    }
                }
            } else {
                upiPayListener.onCancelledByUser();
                Log.e(TAG, "resultCode = " + resultCode);
            }
            finish();
        }
    }

    private TransactionDetails getTransactionDetails(String response) {
        HashMap<String, String> responseData = getMapFromQuery(response);
        transactionDetails.setTransactionID(responseData.get("txnId"));
        transactionDetails.setStatus(responseData.get("Status"));
        transactionDetails.setApprovalRefNo(responseData.get("ApprovalRefNo"));
        transactionDetails.setTransactionRefID(responseData.get("txtRef"));
        transactionDetails.setResponseCode(responseData.get("responseCode"));
        return transactionDetails;
    }

    private HashMap<String, String> getMapFromQuery(String queryString) {
        HashMap<String, String> map = new HashMap<>();
        String[] keyValuePairs = queryString.split("&");
        for (String pair : keyValuePairs) {
            String[] params = pair.split("=");
            if (params.length > 1) {
                map.put(params[0], params[1]);
            }
        }
        return map;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        upiPayListener = null;
    }
}