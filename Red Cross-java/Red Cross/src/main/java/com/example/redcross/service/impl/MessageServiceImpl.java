package com.example.redcross.service.impl;

import com.example.redcross.entity.Message;
import com.example.redcross.mapper.MessageMapper;
import com.example.redcross.service.MessageService;
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

    @Override
    public List<Message> getPrivateMessages() {
        return MessageMapper.getPrivateMessages();
    }

    @Override
    public List<Message> getPrivateMessagesByReceiver(String receiver) {
        return MessageMapper.getPrivateMessagesByReceiver(receiver);
    }

    @Override
    public void createMessage(Message message) {

        MessageMapper.createMessage(message);

    }

    @Override
    public void deleteMessage(Integer messageId) {

        MessageMapper.deleteMessage(messageId);
    }

    @Override
    public void updateMessage(Message message) {
        MessageMapper.updateMessage(message);
    }


}
