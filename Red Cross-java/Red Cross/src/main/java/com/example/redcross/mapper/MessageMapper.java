package com.example.redcross.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.redcross.entity.Message;

import java.util.List;

@Mapper
public interface MessageMapper {

    //显示全部公共消息
    List<Message> getPublicMessages();

    //显示全部私人消息
    List<Message> getPrivateMessages();

    //显示指定用户的私人消息
    List<Message> getPrivateMessagesByReceiver(String receiver);

    //添加消息
    void createMessage(Message message);

    //删除信息
    void deleteMessage(Integer messageId);

    //修改信息
    void updateMessage(Message message);
}
