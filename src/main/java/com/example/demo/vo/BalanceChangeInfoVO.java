package com.example.demo.vo;

import com.example.demo.entity.BalanceChangeInfo;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BalanceChangeInfoVO {
    Integer operation;
    Integer amount;
    String payType;
    String businessNo;
    Long orderId;
    String bank;

    public BalanceChangeInfoVO(BalanceChangeInfo balanceChangeInfo) {
        this.operation = balanceChangeInfo.getOperation();
        switch (operation) {
            case 0:
                amount = balanceChangeInfo.getAmount();
                payType = balanceChangeInfo.getPayType();
                businessNo = balanceChangeInfo.getBusinessNo();
                break;
            case 1:
            case 2:
                orderId = balanceChangeInfo.getOrderId();
                break;
            case 3:
                bank = balanceChangeInfo.getBank();
                break;
        }
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
