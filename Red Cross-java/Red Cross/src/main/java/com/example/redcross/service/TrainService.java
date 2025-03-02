package com.example.redcross.service;

public interface TrainService {

    //生成培训
    Integer PublishTrain(String trainTime,Integer trainPeople,String trainPlace);


    //修改培训
    Integer UpdateTrain(String trainTime, Integer trainPeople, String trainPlace, Integer trainType,Integer trainId);

    //删除培训
    Integer DeleteTrain(Integer trainId);

}
