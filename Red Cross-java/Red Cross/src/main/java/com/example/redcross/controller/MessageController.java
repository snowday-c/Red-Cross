package com.example.redcross.controller;

import com.example.redcross.common.Result;
import com.example.redcross.entity.Message;
import com.example.redcross.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    //获取全部私人消息
    @GetMapping("/private")
    public Result getPrivateMessages() {
        List<Message> messages = messageService.getPrivateMessages();
        return Result.success(messages);
    }
    //根据接收者获取私人消息
    @GetMapping("/private/receiver")
    public Result getPrivateMessagesByReceiver(@RequestParam String receiver) {
        List<Message> messages = messageService.getPrivateMessagesByReceiver(receiver);
        return Result.success(messages);
    }
    //新建消息
    @PostMapping("/add")
    public Result createMessage(@RequestBody Message message) {
        // 使用StringUtils工具类判断标题，发送者是否为空或空字符串
        if (StringUtils.isEmpty(message.getTitle()) || StringUtils.isEmpty(message.getSender())) {
            return Result.error("标题，发送者不能为空");
        }
        messageService.createMessage(message);
        return Result.success();
    }
    //删除消息
    @PostMapping("/delete")
    public Result deleteMessage(@RequestBody Message message) {
        Integer id = message.getMessageId();
        messageService.deleteMessage(id);
        return Result.success();
    }
    //修改消息
    @PostMapping("/update")
    public Result updateMessage(@RequestBody Message message) {
        messageService.updateMessage(message);
        return Result.success();
    }



}
