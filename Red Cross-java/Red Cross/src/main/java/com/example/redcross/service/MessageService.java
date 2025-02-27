package com.example.redcross.service;

import com.example.redcross.entity.Message;

import java.util.List;

public interface MessageService {

    //显示全部公共信息
    List<Message> getPublicMessages();

    //显示私人信息
    List<Message> getPrivateMessages();

    //显示接收者的私人信息
    List<Message> getPrivateMessagesByReceiver(String receiver);

    //添加信息
    void createMessage(Message message);

    //删除消息
    void deleteMessage(Integer messageId);

    //修改消息
    void updateMessage(Message message);
}



