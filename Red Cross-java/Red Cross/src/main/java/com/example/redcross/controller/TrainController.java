package com.example.redcross.controller;


import com.example.redcross.common.Result;
import com.example.redcross.entity.Train;
import com.example.redcross.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/train")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @PostMapping("/publish")        //生成培训
    public Result PublishTrain(@RequestBody Train train){

        String trainTime = train.getTrainTime();
        Integer trainPeople = train.getTrainPeople();
        String trainPlace = train.getTrainPlace();

        trainService.PublishTrain(trainTime,trainPeople,trainPlace);
        return Result.success();
    }

    @PostMapping("/update")     //修改培训
    public Result UpdateTrain(@RequestBody Train train){

        Integer trainId = train.getTrainId();
        String trainTime = train.getTrainTime();
        Integer trainPeople = train.getTrainPeople();
        String trainPlace = train.getTrainPlace();
        Integer trainType = train.getTrainType();

        trainService.UpdateTrain(trainTime,trainPeople,trainPlace,trainType,trainId);
        return Result.success();
    }

    @PostMapping("/delete")     //删除培训
    public Result DeleteTrain(@RequestBody Train train){

        Integer trainId = train.getTrainId();

        trainService.DeleteTrain(trainId);
        return Result.success();
    }
}
