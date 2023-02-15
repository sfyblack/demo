CREATE TABLE `user`
(
    `id`       bigint UNSIGNED NOT NULL AUTO_INCREMENT,
    `username` varchar(20)     NULL,
    `phone`    varchar(11)     NOT NULL,
    `balance`  bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '钱包余额，以分为单位',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `unique_user_phone` (`phone`)
);

CREATE TABLE `balance_change_info`
(
    `id`        bigint UNSIGNED  NOT NULL AUTO_INCREMENT,
    `operation` tinyint UNSIGNED NOT NULL COMMENT '0：用户往钱包充值；1：用户购买商品扣减钱包余额；2：用户申请退款；3：用户申请把余额提现到银行卡',
    `user_id`   bigint UNSIGNED  NULL COMMENT '此条记录对应的用户ID',
    `extra`     varchar(255)     NULL COMMENT '存储附加的数据：op为0时存储充值金额和支付方式和交易编号；为1时存储此次交易的订单ID；为2时存储申请退款的订单ID；为3时存储提现金额，目标银行，以及交易编号',
    `time`      datetime         NULL COMMENT '发生的时间',
    PRIMARY KEY (`id`),
    INDEX `bci_uid_idx` (`user_id`)
);