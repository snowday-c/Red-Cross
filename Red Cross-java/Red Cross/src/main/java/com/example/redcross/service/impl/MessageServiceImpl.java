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
    public void createPublicMessage(Message message) {

        MessageMapper.createPublicMessage(message);

    }

    @Override
    public void deletePublicMessage(String title) {

        MessageMapper.deletePublicMessage(title);
    }

    @Override
    public void createPrivateMessage(Message message) {
        MessageMapper.createPrivateMessage(message);
    }

    @Override
    public void deletePrivateMessage(String title) {
        MessageMapper.deletePrivateMessage(title);
    }


}
