package com.example.redcross.service;

import com.example.redcross.entity.Advice;

import java.util.List;

public interface AdviceService {
    
    /**
     * 插入用户反馈
     * @param advice 反馈信息
     * @return 是否成功
     */
    Boolean insertAdvice(Advice advice);
    
    /**
     * 获取所有反馈信息
     * @return 反馈信息列表
     */
    List<Advice> getAllAdvice();
    
    /**
     * 获取未处理的反馈信息
     * @return 未处理的反馈信息列表
     */
    List<Advice> getUnhandledAdvice();
    
    /**
     * 更新反馈信息状态
     * @param adviceId 反馈ID
     * @param adviceType 反馈状态
     * @return 是否成功
     */
    Boolean updateAdviceType(Integer adviceId, String adviceType, String adviceHandler);
}