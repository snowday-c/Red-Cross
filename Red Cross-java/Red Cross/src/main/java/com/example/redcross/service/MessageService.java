package com.example.redcross.service;

import com.example.redcross.entity.Message;

import java.util.List;

public interface MessageService {

    //显示全部公共信息
    List<Message> getPublicMessages();//显示全部公共信息

    //添加公共信息
    void createPublicMessage(Message message);

    void deletePublicMessage(String title);

    void createPrivateMessage(Message message);

    void deletePrivateMessage(String title);


//    void deleteMessage(int messageId);//删除公共信息
}
