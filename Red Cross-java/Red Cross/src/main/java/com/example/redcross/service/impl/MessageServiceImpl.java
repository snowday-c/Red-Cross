package com.example.redcross.service.impl;

import com.example.redcross.mapper.MessageMapper;
import com.example.redcross.service.MessageService;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper MessageMapper;

    @Override
    public List<Message> getPublicMessages() {

        return MessageMapper.getPublicMessages();

    }

//    @Override
//    public void addMessage(Message message) {
//
//    }
//
//    @Override
//    public void deleteMessage(int messageId) {
//
//    }
}
