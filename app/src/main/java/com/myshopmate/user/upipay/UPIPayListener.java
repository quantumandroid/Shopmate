package com.myshopmate.user.upipay;

public interface UPIPayListener {
    void onTransactionSubmitted(TransactionDetails transactionDetails);
    void onTransactionSuccess(TransactionDetails transactionDetails);
    void onTransactionFailed(TransactionDetails transactionDetails);
    void onCancelledByUser();
    void onUPIAppNotFound();
    void onError(String errorMsg);
}
