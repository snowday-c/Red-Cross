package com.example.redcross.controller;

import com.example.redcross.common.Result;
import com.example.redcross.entity.Certificate;
import com.example.redcross.entity.Message;
import com.example.redcross.entity.User;
import com.example.redcross.service.CertificateService;
import com.example.redcross.service.MessageService;
import com.example.redcross.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @PostMapping("/apply")   //申请证书
    public Result applyCertificate(@RequestBody Certificate certificate) {
        Integer userId = certificate.getUserId();

        if (certificateService.canApplyCertificate(userId) == 1){
            return Result.error("证书审核中，无法重复申请");
        }
        else if (certificateService.applyCertificate(userId) ==1) {
            return Result.success();
        }
        else {
            return Result.error("申请证书失败");
        }
    }


    @PostMapping("/approve") // 审核通过
    public Result approveCertificate(@RequestBody Certificate certificate) {
        Integer certificateId = certificate.getCertificateId();
        Integer userId = certificate.getUserId();
        String approver = certificate.getApprover();
        // 获取用户信息
        User user = userService.getUserById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        //生成证书标题和内容
        String userName = user.getUserName();
        String certificateTitle =  certificateService.gainCertificateTitle();
        String certificateContent = certificateService.gainCertificateContent(userName);
        // 生成证书
        certificateService.gainCertificate(certificateId, certificateTitle, certificateContent, approver);
        // 发送消息通知用户
        Message message = new Message();
        message.setSender("系统");
        message.setReceiver(user.getUserName());
        message.setTitle("证书申请结果");
        message.setContent("您申请的证书已通过审核！");
        message.setMessageType(1);
        messageService.createMessage(message);
        return Result.success();
    }

    @PostMapping("/reject") // 拒绝审核
    public Result rejectCertificate(@RequestBody Certificate certificate) {
        Integer certificateId = certificate.getCertificateId();
        Integer userId = certificate.getUserId();
        String approver = certificate.getApprover();
        // 获取用户信息
        User user = userService.getUserById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // 拒绝审核
        certificateService.rejectCertificate(certificateId, approver);
        // 发送消息通知用户
        Message message = new Message();
        message.setSender("系统");
        message.setReceiver(user.getUserName());
        message.setTitle("证书申请结果");
        message.setContent("您申请的证书未通过审核！如有疑问请联系管理员。");
        message.setMessageType(1);
        messageService.createMessage(message);
        return Result.success();
    }

    @GetMapping("/list/wait")    //查询待审核证书
    public Result getWaitCertificate() {
        List<Certificate> certificates = certificateService.getWaitCertificate();
        return Result.success(certificates);
    }

    @GetMapping("/list/approved")   //查询已发放证书
    public Result getApprovedCertificate() {
        List<Certificate> certificates = certificateService.getApprovedCertificate();
        return Result.success(certificates);
    }

    @PostMapping("/list/user")   //通过用户id查找全部证书
    public Result listCertificate(@RequestBody Certificate certificate) {
        Integer userId = certificate.getUserId();
        return Result.success(certificateService.listCertificate(userId));
    }

    @PostMapping("/delete")     //删除证书
    public Result deleteCertificate(@RequestBody Certificate certificate) {
        Integer certificateId = certificate.getCertificateId();
        certificateService.deleteCertificate(certificateId);
        return Result.success();
    }



}
