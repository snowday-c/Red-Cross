package com.example.redcross.service.impl;

import com.example.redcross.entity.Certificate;
import com.example.redcross.mapper.CertificateMapper;
import com.example.redcross.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CertificateServiceImpl implements CertificateService {

    // 预定义的证书标题数组
    private static final String[] CERTIFICATE_TITLES = {
            "红十字救生员培训合格证书",
            "红十字水上安全救生员资格证书",
            "红十字急救与救生技能认证证书",
            "红十字水上救援能力认证证书"
    };

    // 预定义的证书内容模板数组
    private static final String[] CERTIFICATE_CONTENTS = {
            "兹证明%s同志已完成红十字救生员全部培训课程，经考核合格，具备红十字救生员资格，特发此证。",
            "%s学员已完成红十字水上安全救生员培训计划，掌握相关救生技能，准予颁发此资格证书。",
            "经考核，%s同志已达到红十字救生员技能标准，具备水上救援能力，特授予此认证证书。",
            "本证书确认%s已完成红十字救生员培训并通过考核，具备在紧急情况下实施水上救援的资格和能力。"
    };

    private Random random = new Random();

    @Autowired
    private CertificateMapper certificateMapper;

    @Override
    public Integer canApplyCertificate(Integer userId) {
        return certificateMapper.canApplyCertificate(userId);
    }

    @Override
    public Integer applyCertificate(Integer userId) {
        return certificateMapper.applyCertificate(userId);
    }

    @Override
    public boolean gainCertificate(Integer certificateId,String certificateTitle, String certificateContent,String approver) {
        return certificateMapper.gainCertificate(certificateId,certificateTitle, certificateContent,approver);
    }

    @Override
    public List<Certificate> listCertificate(Integer userId) {
        return certificateMapper.listCertificate(userId);
    }

    @Override
    public void deleteCertificate(Integer certificateId) {
        certificateMapper.deleteCertificate(certificateId);
    }

    @Override
    public List<Certificate> getWaitCertificate() {
        return certificateMapper.getWaitCertificate();
    }

    @Override
    public void rejectCertificate(Integer certificateId, String approver) {
        certificateMapper.rejectCertificate(certificateId, approver);
    }

    @Override
    public List<Certificate> getApprovedCertificate() {
        return certificateMapper.getApprovedCertificate();
    }

    @Override
    public String gainCertificateTitle() {
        // 随机选择一个标题
        int index = random.nextInt(CERTIFICATE_TITLES.length);
        return CERTIFICATE_TITLES[index];
    }

    @Override
    public String gainCertificateContent(String userName) {
        // 随机选择一个内容模板
        int index = random.nextInt(CERTIFICATE_CONTENTS.length);
        // 将用户名插入到内容中
        return String.format(CERTIFICATE_CONTENTS[index], userName);
    }
}
