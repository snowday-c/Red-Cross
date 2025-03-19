package com.example.redcross.service;

import com.example.redcross.entity.Certificate;

import java.util.List;

public interface CertificateService {
    boolean gainCertificate(Integer certificateId,String certificateTitle, String certificateContent,String approver);

    List<Certificate> listCertificate(Integer userId);

    void deleteCertificate(Integer certificateId);

    Integer applyCertificate(Integer userId);

    Integer canApplyCertificate(Integer userId);

    List<Certificate> getWaitCertificate();

    void rejectCertificate(Integer certificateId, String approver);

    List<Certificate> getApprovedCertificate();
}
