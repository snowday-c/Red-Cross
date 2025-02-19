package com.example.redcross.controller;

import com.example.redcross.common.Result;
import com.example.redcross.service.MessageService;
import com.example.redcross.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;  // 引入StringUtils工具类

@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    //获取公共消息
    @GetMapping("/public")
    public Result getPublicMessages() {
        List<Message> messages = messageService.getPublicMessages();
        return Result.success(messages);
    }
    //新建公共消息
    @PostMapping("/addpublic")
    public Result createPublicMessage(@RequestBody Message message) {
        // 使用StringUtils工具类判断标题，内容，发送者是否为空或空字符串
        if (StringUtils.isEmpty(message.getTitle()) || StringUtils.isEmpty(message.getContent())
                || StringUtils.isEmpty(message.getSender())) {
            return Result.error("标题，内容，发送者不能为空");
        }

        messageService.createPublicMessage(message);
        return Result.success();
    }
    //删除公共消息
    @DeleteMapping("/deletepublic")
//    public Result deletePublicMessage(@RequestParam String title) {  // title参数是String类型
//        messageService.deletePublicMessage(title);
//        return Result.success();
//    }
    public Result deletePublicMessage(@RequestBody Map<String, String> request) {   //title参数是json类型
        String title = request.get("title");
        if (title == null) {
            return Result.error("标题不能为空");
        }
        messageService.deletePublicMessage(title);
        return Result.success();
    }
    //新建私人消息

    @PostMapping("/addprivate")
    public Result createPrivateMessage(@RequestBody Message message) {
        // 使用StringUtils工具类判断标题，内容，发送者，接收者是否为空或空字符串
        if (StringUtils.isEmpty(message.getTitle()) || StringUtils.isEmpty(message.getContent())
                || StringUtils.isEmpty(message.getSender()) || StringUtils.isEmpty(message.getReceiver())) {
            return Result.error("标题，内容，发送者，接收者不能为空");
        }
        try {
            messageService.createPrivateMessage(message);
            return Result.success();
        } catch (Exception e) {
            // 记录日志（假设已经有了日志记录机制）
            e.printStackTrace();  // 这里可以替换为更优雅的日志记录方式，如使用slf4j
            return Result.error("创建私人消息失败");
        }
    }

    //删除私人消息
    @DeleteMapping("/deleteprivate")
//    public Result deletePrivateMessage(@RequestParam String title) {
//        messageService.deletePrivateMessage(title);
//        return Result.success();
//    }
    public Result deletePrivateMessage(@RequestBody Map<String, String> request) {   //title参数是json类型
        String title = request.get("title");
        if (title == null) {
            return Result.error("标题不能为空");
            }
        messageService.deletePrivateMessage(title);
        return Result.success();
        }
}
