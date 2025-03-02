package com.example.redcross.service.impl;

import com.example.redcross.mapper.TrainMapper;
import com.example.redcross.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainMapper trainMapper;

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
}
