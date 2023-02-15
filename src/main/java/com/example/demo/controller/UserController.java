package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.constant.BalanceChangeOperation;
import com.example.demo.entity.BalanceChangeInfo;
import com.example.demo.entity.User;
import com.example.demo.service.BalanceChangeInfoService;
import com.example.demo.service.UserService;
import com.example.demo.vo.BalanceChangeInfoVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Resource
    UserService userService;

    @Resource
    BalanceChangeInfoService balanceChangeInfoService;

    /**
     * 查询用户余额接口
     *
     * @return
     */
    @GetMapping("/balance")
    public Map<String, Object> getBalance() {
        // 默认用户已经登录，从JWT中或Session中获取用户ID
        long userId = getUserIdFromJwtOrSession();

        final User user = userService.getOne(new QueryWrapper<User>().eq("id", userId));

        // 图方便就不统一封装响应了
        return new HashMap<String, Object>() {{
            // 因为数据库内都是以分为单位存储，这里查询的余额要除100
            put("data", user.getBalance() / 100);
            put("success", true);
        }};
    }

    /**
     * 用户消费100元接口
     * 我理解就是用户下了一个100元的订单
     */
    @Transactional
    @PostMapping("/order")
    public Map<String, Object> consume() {
        // 默认用户已经登录，从JWT中或Session中获取用户ID
        long userId = getUserIdFromJwtOrSession();
        // 根据用户ID查询用户已选中的购物车，封装成OrderItem，创建订单
        // 因为一个订单可能包含多个商品，一个OrderDetail中包含多个OrderItem
        OrderDetail o = createOrder(userId);
        // 获取订单ID，订单总金额
        long orderAmount = o.getAmount();
        long orderId = o.getId();

        // 减少用户钱包余额，这里可以使用乐观锁，图个方便就没用
        User u = userService.getById(userId);
        u.setBalance(u.getBalance() - orderAmount);
        userService.updateById(u);

        final BalanceChangeInfo info = new BalanceChangeInfo();
        info.setOperation(BalanceChangeOperation.CONSUME);
        final BalanceChangeInfo.BalanceChangeInfoExtraBuilder extraBuilder = info.extraBuilder();
        extraBuilder.setOrderId(orderId);
        info.setExtra(extraBuilder.build());
        // 将记录写入变更日志表中
        balanceChangeInfoService.save(info);

        // 图方便就不统一封装响应了
        return new HashMap<String, Object>() {{
            // 因为数据库内都是以分为单位存储，这里查询的余额要除100
            put("success", true);
        }};
    }

    /**
     * 用户退款20元接口
     * 我理解就是用户对一个金额为20元的订单进行了退款操作
     */
    @Transactional
    @PostMapping("/refund/{orderId}")
    public Map<String, Object> consume(@PathVariable long orderId) {
        // 默认用户已经登录，从JWT中或Session中获取用户ID
        long userId = getUserIdFromJwtOrSession();
        // 根据订单ID查询订单
        OrderDetail o = selectOrderById(userId);
        // 将订单的状态设置为已退款
        o.setStatus("refund");
        orderService.updateById(o);

        // 获取订单交易额
        long orderAmount = o.getAmount();

        // 增加用户钱包余额，这里可以使用乐观锁，图个方便就没用
        User u = userService.getById(userId);
        u.setBalance(u.getBalance() + orderAmount);
        userService.updateById(u);

        // 创建余额更改日志
        final BalanceChangeInfo info = new BalanceChangeInfo();
        info.setOperation(BalanceChangeOperation.REFUND);
        final BalanceChangeInfo.BalanceChangeInfoExtraBuilder extraBuilder = info.extraBuilder();
        extraBuilder.setOrderId(orderId);
        info.setExtra(extraBuilder.build());
        // 将记录写入变更日志表中
        balanceChangeInfoService.save(info);

        // 图方便就不统一封装响应了
        return new HashMap<String, Object>() {{
            put("success", true);
        }};
    }

    /**
     * 查询用户钱包余额变更明细
     *
     * @return
     */
    @GetMapping("/balance/list")
    public Map<String, Object> showBalanceLogs() {
        // 默认用户已经登录，从JWT中或Session中获取用户ID
        long userId = getUserIdFromJwtOrSession();

        // 按时间倒序查询指定用户钱包的金额变动明细
        final List<BalanceChangeInfo> list = balanceChangeInfoService.list(
                new QueryWrapper<BalanceChangeInfo>()
                        .eq("user_id", userId)
                        .orderBy(true, false, "time")
        );


        // 图方便就不统一封装响应了
        return new HashMap<String, Object>() {{
            put("success", true);
            // list会调用内部每个元素的toString方法
            put("data", list.stream().map(BalanceChangeInfoVO::new).collect(Collectors.toList()));
        }};
    }
}
