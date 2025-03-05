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

    Integer IsJoinTrain(Integer trainId, Integer userId);

    Integer JoinTrain(Integer trainId, Integer userId);

    Integer MaxTrainPeople(Integer trainId);

    Integer CancelTrain(Integer trainId, Integer userId);

    Integer UpdateRetrain(Integer trainId, Integer userId, String trainPlace, String trainTime);

    List<Train> getAllTrains();
}
