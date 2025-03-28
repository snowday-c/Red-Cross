package com.example.redcross.service.impl;

import com.example.redcross.entity.Train;
import com.example.redcross.mapper.TrainMapper;
import com.example.redcross.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainMapper trainMapper;
    //查询所有培训


    @Override
    public List<Train> getAllTrains() {
        return trainMapper.getAllTrains();
    }

    @Override
    public List<Train> getCanJoinTrains() {
        return trainMapper.getCanJoinTrains();
    }

    @Override
    public List<Train> getHistoryTrains(Integer userId) {
        return trainMapper.getHistoryTrains(userId);
    }

    //生成培训
    @Override
    public Integer PublishTrain(String trainTime, Integer trainPeople, String trainPlace) {
        return trainMapper.PublishTrain(trainTime,trainPeople,trainPlace);
    }

    //修改培训
    @Override
    public Integer UpdateTrain(String trainTime, Integer trainPeople, String trainPlace, Integer trainType,Integer trainId) {
        return trainMapper.UpdateTrain(trainTime,trainPeople,trainPlace,trainType,trainId);
    }

    //删除培训
    @Override
    public Integer DeleteTrain(Integer trainId) {
        return trainMapper.DeleteTrain(trainId);
    }

    @Override
    public Integer CurrentTrainPeople(Integer trainId) {
        return trainMapper.CurrentTrainPeople(trainId);
    }

    @Override
    public Integer IsJoinTrain(String trainTime, Integer userId) {
        return trainMapper.IsJoinTrain(trainTime,userId);
    }

    @Override
    public Integer JoinTrain(Integer trainId, Integer userId) {
        return trainMapper.JoinTrain(trainId,userId);
    }

    @Override
    public Integer MaxTrainPeople(Integer trainId) {
        return trainMapper.MaxTrainPeople(trainId);
    }

    @Override
    public Integer CancelTrain(Integer trainId, Integer userId) {
        return trainMapper.CancelTrain(trainId,userId);
    }

    @Override
    public Integer UpdateRetrain(Integer trainId, Integer userId, String trainPlace, String trainTime) {
        return trainMapper.UpdateRetrain(trainId,userId,trainPlace,trainTime);
    }

    @Override
    public Integer DeleteRetrain(Integer trainId, Integer userId) {
        return trainMapper.DeleteRetrain(trainId,userId);
    }

    @Override
    public Integer ParticipateTrain(Integer trainId, Integer userId) {
        return trainMapper.ParticipateTrain(trainId,userId);
    }
}
