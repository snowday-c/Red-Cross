package com.example.redcross.mapper;

import com.example.redcross.entity.Advice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdviceMapper {
    
    /**
     * 插入用户反馈
     * @param advice 反馈信息
     * @return 影响行数
     */
    Integer insertAdvice(Advice advice);
    
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
     *
     * @param adviceId      反馈ID
     * @param adviceType    反馈状态
     * @param adviceHandler
     * @return 影响行数
     */
    Integer updateAdviceType(@Param("adviceId") Integer adviceId, @Param("adviceType") String adviceType, @Param("adviceHandler") String adviceHandler);
}