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
    public boolean gainCertificate(Integer userId,String certificateTitle, String certificateContent) {
        return certificateMapper.gainCertificate(userId,certificateTitle, certificateContent);
    }

    @Override
    public List<Certificate> listCertificate(Integer userId) {
        return certificateMapper.listCertificate(userId);
    }

    @Override
    public void deleteCertificate(Integer certificateId) {
        certificateMapper.deleteCertificate(certificateId);
    }
}
