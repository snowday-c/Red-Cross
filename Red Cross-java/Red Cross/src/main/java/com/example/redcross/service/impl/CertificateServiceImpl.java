package com.example.redcross.service.impl;

import com.example.redcross.entity.Certificate;
import com.example.redcross.mapper.CertificateMapper;
import com.example.redcross.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {

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
}
