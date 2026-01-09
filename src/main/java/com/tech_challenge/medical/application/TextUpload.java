package com.tech_challenge.medical.application;

import org.springframework.web.multipart.MultipartFile;

public record TextUpload(
        MultipartFile file,
        Long patientId,
        String source
) {
}
