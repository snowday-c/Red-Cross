package com.example.redcross.controller;

import com.example.redcross.common.Result;
import com.example.redcross.entity.Certificate;
import com.example.redcross.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping("/gain")
    public Result getCertificate(@RequestBody Certificate certificate) {
        Integer userId = certificate.getUserId();
        String certificateTitle = certificate.getCertificateTitle();
        String certificateContent = certificate.getCertificateContent();

        certificateService.gainCertificate(userId,certificateTitle, certificateContent);
        return Result.success();
    }

    @PostMapping("/list")
    public Result listCertificate(@RequestBody Certificate certificate) {
        Integer userId = certificate.getUserId();
        return Result.success(certificateService.listCertificate(userId));
    }
    @PostMapping("/delete")
    public Result deleteCertificate(@RequestBody Certificate certificate) {
        Integer certificateId = certificate.getCertificateId();
        certificateService.deleteCertificate(certificateId);
        return Result.success();
    }
}
