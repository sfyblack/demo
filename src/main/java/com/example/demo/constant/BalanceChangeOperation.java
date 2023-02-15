package com.example.demo.constant;

public abstract class BalanceChangeOperation {

    /**
     * 余额充值
     */
    public static int RECHARGE = 0;

    /**
     * 余额消费（购物）
     */
    public static int CONSUME = 1;

    /**
     * 余额增加（退款）
     */
    public static int REFUND = 2;

    /**
     * 余额减少（提现）
     */
    public static int WITHDRAW = 3;
}
