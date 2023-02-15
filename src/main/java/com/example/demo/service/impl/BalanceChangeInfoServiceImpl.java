package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.BalanceChangeInfoMapper;
import com.example.demo.entity.BalanceChangeInfo;
import com.example.demo.service.BalanceChangeInfoService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BalanceChangeInfoServiceImpl extends ServiceImpl<BalanceChangeInfoMapper, BalanceChangeInfo> implements BalanceChangeInfoService {

    @Override
    public boolean save(BalanceChangeInfo entity) {
        entity.setTime(new Date());
        return super.save(entity);
    }
}