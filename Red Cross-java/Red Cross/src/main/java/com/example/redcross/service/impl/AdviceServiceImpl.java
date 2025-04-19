package com.example.redcross.service.impl;

import com.example.redcross.entity.Advice;
import com.example.redcross.mapper.AdviceMapper;
import com.example.redcross.service.AdviceService;
import com.example.redcross.utils.LogUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdviceServiceImpl implements AdviceService {

    private final Logger logger = LogUtils.getBusinessLogger(AdviceServiceImpl.class);
    
    @Autowired
    private AdviceMapper adviceMapper;

    @Override
    public Boolean insertAdvice(Advice advice) {
        try {
            // 设置当前时间
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            advice.setAdviceTime(now.format(formatter));
            
            // 设置默认状态为未处理
            if (advice.getAdviceType() == null || advice.getAdviceType().isEmpty()) {
                advice.setAdviceType("未处理");
            }
            
            logger.info("插入用户反馈: {}", advice.getAdviceContent());
            return adviceMapper.insertAdvice(advice) > 0;
        } catch (Exception e) {
            logger.error("插入用户反馈失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Advice> getAllAdvice() {
        logger.info("获取所有用户反馈");
        return adviceMapper.getAllAdvice();
    }

    @Override
    public List<Advice> getUnhandledAdvice() {
        logger.info("获取未处理的用户反馈");
        return adviceMapper.getUnhandledAdvice();
    }

    @Override
    public Boolean updateAdviceType(Integer adviceId, String adviceType, String adviceHandler) {
        logger.info("更新反馈状态: ID={}, 状态={}, 处理人={}", adviceId, adviceType, adviceHandler);
        return adviceMapper.updateAdviceType(adviceId, adviceType, adviceHandler) > 0;
    }
}