package com.example.redcross.service;

import com.example.redcross.entity.Train;

import java.util.List;

public interface TrainService {

    //生成培训
    Integer PublishTrain(String trainTime,Integer trainPeople,String trainPlace);


    //修改培训
    Integer UpdateTrain(String trainTime, Integer trainPeople, String trainPlace, Integer trainType,Integer trainId);

    //删除培训
    Integer DeleteTrain(Integer trainId);

    Integer CurrentTrainPeople(Integer trainId);

    Integer IsJoinTrain(String trainTime, Integer userId);

    Integer JoinTrain(Integer trainId, Integer userId);

    Integer MaxTrainPeople(Integer trainId);

    Integer CancelTrain(Integer trainId, Integer userId);

    Integer UpdateRetrain(Integer trainId, Integer userId, String trainPlace, String trainTime);

    List<Train> getAllTrains();

    List<Train> getCanJoinTrains();

    List<Train> getHistoryTrains(Integer userId);

    Integer DeleteRetrain(Integer trainId, Integer userId);

    Integer ParticipateTrain(Integer trainId, Integer userId);

    // 根据ID获取培训信息
    Train getTrainById(Integer trainId);
    
    // 检查用户是否已报名培训
    boolean isUserRegistered(Integer trainId, Integer userId);
    
    // 检查用户是否已签到
    boolean isUserParticipated(Integer trainId, Integer userId);
}
