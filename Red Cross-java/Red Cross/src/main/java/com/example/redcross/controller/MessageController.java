package com.example.redcross.controller;

import com.example.redcross.common.Result;
import com.example.redcross.service.MessageService;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/public")
    public Result getPublicMessages() {
        List<Message> messages = messageService.getPublicMessages();
        return Result.success(messages);
    }

}
