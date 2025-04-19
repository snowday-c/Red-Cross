package com.example.redcross.controller;

import com.example.redcross.common.Result;
import com.example.redcross.entity.Advice;
import com.example.redcross.service.AdviceService;
import com.example.redcross.utils.LogUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/advice")
public class AdviceController {

    private final Logger logger = LogUtils.getBusinessLogger(AdviceController.class);

    @Autowired
    private AdviceService adviceService;

    //将用户反馈信息插入数据库
    @PostMapping("/insert")
    public Result insertAdvice(@RequestBody Advice advice) {
        logger.info("接收到用户反馈: {}", advice.getAdviceContent());
        if (adviceService.insertAdvice(advice)) {
            return Result.success();
        }
        return Result.error("提交反馈失败，请稍后再试");
    }
    
    //获取所有的用户反馈信息
    @GetMapping("/all")
    public Result getAllAdvice() {
        logger.info("获取所有用户反馈");
        List<Advice> adviceList = adviceService.getAllAdvice();
        return Result.success(adviceList);
    }

    //获取未处理的用户反馈信息
    @GetMapping("/unhandled")
    public Result getUnhandledAdvice() {
        logger.info("获取未处理的用户反馈");
        List<Advice> adviceList = adviceService.getUnhandledAdvice();
        return Result.success(adviceList);
    }
    
    //更新反馈状态
    @PostMapping("/updateType")
    public Result updateAdviceType(@RequestBody Advice advice) {
        logger.info("更新反馈状态: ID={}, 状态={}", advice.getAdviceId(), advice.getAdviceType());
        if (adviceService.updateAdviceType(advice.getAdviceId(), advice.getAdviceType(), advice.getAdviceHandler())) {
            return Result.success();
        }
        return Result.error("更新反馈状态失败，请稍后再试");
    }
}
