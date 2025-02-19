package com.example.redcross.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.redcross.entity.Message;

import java.util.List;

@Mapper
public interface MessageMapper {

    //显示全部公共消息
    List<Message> getPublicMessages();

    //添加公共消息
    void createPublicMessage(Message message);

    void deletePublicMessage(String title);

    void createPrivateMessage(Message message);

    void deletePrivateMessage(String title);


//    void deleteMessage(int messageId);//删除公共信息
}
