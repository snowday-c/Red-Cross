package com.example.redcross.service;

import com.example.redcross.entity.Certificate;

import java.util.List;

public interface CertificateService {
    boolean gainCertificate(Integer userId,String certificateTitle, String certificateContent);

    List<Certificate> listCertificate(Integer userId);

    void deleteCertificate(Integer certificateId);
}
