package com.example.redcross.mapper;

import com.example.redcross.entity.Certificate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CertificateMapper {
    boolean gainCertificate(Integer userId, String certificateTitle, String certificateContent);

    List<Certificate> listCertificate(Integer userId);

    void deleteCertificate(Integer certificateId);
}
