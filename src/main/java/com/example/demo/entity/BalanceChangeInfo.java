package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("balance_change_info")
public class BalanceChangeInfo {
    private long id;

    private int operation;

    @TableField("user_id")
    private long userId;

    private String extra;

    private Date time;


    public BalanceChangeInfo() {
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    /**
     * 获取用户充值/提现金额
     *
     * @return
     */
    public int getAmount() {
        if (operation != 0) {
            return 0;
        }

        return Integer.parseInt(extra.split(",")[0]);
    }

    /**
     * 获取用户充值金额的支付方式
     *
     * @return
     */
    public String getPayType() {
        if (operation != 0) {
            return null;
        }

        return extra.split(",")[0];
    }

    /**
     * 获取与支付宝等第三方机构进行交易的编号
     *
     * @return
     */
    public String getBusinessNo() {
        if (operation != 0 || operation != 3) {
            return null;
        }

        final String[] strArr = extra.split(",");
        return strArr[strArr.length - 1];
    }

    /**
     * 获取用户下单或申请退款的订单ID
     *
     * @return
     */
    public long getOrderId() {
        if (operation != 1 || operation != 2) {
            return 0;
        }

        return Integer.parseInt(extra.split(",")[0]);
    }

    /**
     * 获取用户提现金额的目标银行
     *
     * @return
     */
    public String getBank() {
        if (operation != 3) {
            return null;
        }
        return extra.split(",")[1];
    }


    public BalanceChangeInfoExtraBuilder extraBuilder() {
        return new BalanceChangeInfoExtraBuilder(operation);
    }

    public static class BalanceChangeInfoExtraBuilder {
        private int operation;
        private String bank;
        private long orderId;
        private String businessNo;
        private String payType;
        private long amount;


        public void setOperation(int operation) {
            this.operation = operation;
        }

        public String build() {
            switch (operation) {
                case 0:
                    return amount + "," + payType + "," + businessNo;
                case 1:
                case 2:
                    return String.valueOf(orderId);
                case 3:
                    return amount + "," + bank + "," + businessNo;
            }

            throw new RuntimeException("异常的操作：" + operation);
        }

        private BalanceChangeInfoExtraBuilder(int operation) {
            this.operation = operation;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public String getBusinessNo() {
            return businessNo;
        }

        public void setBusinessNo(String businessNo) {
            this.businessNo = businessNo;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }
    }
}
