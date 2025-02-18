package com.example.redcross.service;

import org.apache.logging.log4j.message.Message;

import java.util.List;

public interface MessageService {

    List<Message> getPublicMessages();//显示全部公共信息

//    void addMessage(Message message);//添加公共信息
//
//    void deleteMessage(int messageId);//删除公共信息
}
