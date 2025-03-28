package com.example.redcross.mapper;

import com.example.redcross.entity.Train;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TrainMapper {

    //生成培训
    Integer PublishTrain(String trainTime, Integer trainPeople, String trainPlace);

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
}