package com.example.redcross.controller;

import com.example.redcross.common.Result;
import com.example.redcross.entity.Train;
import com.example.redcross.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/train")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @GetMapping("/all")     //查询所有培训
    public Result getAllTrains(){
        List<Train> trains = trainService.getAllTrains();
        return Result.success(trains);
    }

    @GetMapping("/canJoin")     //查询可报名的培训
    public Result canJoinTrain(){
        List<Train> trains = trainService.getCanJoinTrains();
        return Result.success(trains);
    }

    @PostMapping("/publish")        //发布培训
    public Result PublishTrain(@RequestBody Train train){

        String trainTime = train.getTrainTime();
        Integer trainPeople = train.getTrainPeople();
        List<String> trainPlaces = train.getTrainPlaces();

        // 遍历培训地点列表，为每个地点插入一条记录
        for (String trainPlace : trainPlaces) {
            trainService.PublishTrain(trainTime, trainPeople, trainPlace);
        }
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

    @PostMapping("/join")     //报名培训
    public Result JoinTrain(@RequestBody Train train){
        Integer trainId = train.getTrainId();
        Integer userId = train.getUserId();
        String trainPlace = train.getTrainPlace();
        String trainTime = train.getTrainTime();
        //根据培训id查询培训城市可报名人数
        int maxPeople = trainService.MaxTrainPeople(trainId);
        int currentPeople = trainService.CurrentTrainPeople(trainId);    //查询报名城市的当前培训人数是否已满
        if(currentPeople >= maxPeople){
            return Result.error("该城市的培训人数已满，请选择其他城市！");
        }
        //查询用户是否已经报名
        if((trainService.IsJoinTrain(trainTime,userId)) == 1) {
            return Result.error("您已经报名过该次培训，请勿重复报名！");
        }
        //进行报名
        if(trainService.JoinTrain(trainId,userId) == 1){
            trainService.UpdateRetrain(trainId,userId,trainPlace,trainTime);    //报名成功将培训记录更新到复训表中
            return Result.success("报名成功！");
        }
        return Result.error("报名失败，请稍后再试！");
    }

    @PostMapping("/cancel")     //取消报名
    public Result CancelTrain(@RequestBody Train train){
        Integer trainId = train.getTrainId();
        Integer userId = train.getUserId();
        if(trainService.CancelTrain(trainId,userId) == 1){
//            trainService.DeleteRetrain(trainId,userId);    //取消报名成功将培训记录从复训表中删除
            return Result.success("取消报名成功！");
        }
        return Result.error("取消报名失败，请稍后再试！");

    }

    @PostMapping("/historyTrain")     //查询用户培训记录
    public Result getHistoryTrain(@RequestBody Train train){
        Integer userId = train.getUserId();
        List<Train> trains = trainService.getHistoryTrains(userId);
        return Result.success(trains);
    }
}
