package com.example.redcross.task;

import com.example.redcross.entity.Train;
import com.example.redcross.mapper.TrainMapper;
import com.example.redcross.utils.LogUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TrainStatusTask {

    private final Logger logger = LogUtils.getSystemLogger();

    @Autowired
    private TrainMapper trainMapper;

    // 使用cron表达式，每5分钟执行一次（0,5,10,15...55分时执行）
    @Scheduled(cron = "0 0/5 * * * ?")
    public void updateTrainStatus() {
        logger.info("开始执行培训状态更新任务");
        
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        String currentTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        // 更新培训状态为"进行中"
        int updatedToOngoing = trainMapper.updateTrainToOngoing(currentTime);
        if (updatedToOngoing > 0) {
            logger.info("已将{}个培训状态更新为进行中", updatedToOngoing);
        }
        
        // 更新培训状态为"已结束"
        int updatedToFinished = trainMapper.updateTrainToFinished(currentTime);
        if (updatedToFinished > 0) {
            logger.info("已将{}个培训状态更新为已结束", updatedToFinished);
        }
    }
}