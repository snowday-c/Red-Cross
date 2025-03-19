package com.example.redcross.mapper;

import com.example.redcross.entity.Certificate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CertificateMapper {
    boolean gainCertificate(Integer certificateId, String certificateTitle, String certificateContent,String approver);

    List<Certificate> listCertificate(Integer userId);

    void deleteCertificate(Integer certificateId);

    Integer canApplyCertificate(Integer userId);

    Integer applyCertificate(Integer userId);

    List<Certificate> getWaitCertificate();

    void rejectCertificate(Integer certificateId, String approver);

    List<Certificate> getApprovedCertificate();
}
