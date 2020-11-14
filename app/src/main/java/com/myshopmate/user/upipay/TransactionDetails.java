package com.myshopmate.user.upipay;

import java.io.Serializable;

public class TransactionDetails implements Serializable {
    private String transactionID;
    private String transactionRefID;
    private String payeeName;
    private String payeeUPIAddress;
    private String merchantCode;
    private String description;
    private String currency;
    private String amount;
    private String status;
    private String approvalRefNo;
    private String responseCode;

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionRefID() {
        return transactionRefID;
    }

    public void setTransactionRefID(String transactionRefID) {
        this.transactionRefID = transactionRefID;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeUPIAddress() {
        return payeeUPIAddress;
    }

    public void setPayeeUPIAddress(String payeeUPIAddress) {
        this.payeeUPIAddress = payeeUPIAddress;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovalRefNo() {
        return approvalRefNo;
    }

    public void setApprovalRefNo(String approvalRefNo) {
        this.approvalRefNo = approvalRefNo;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
