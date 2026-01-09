package com.tech_challenge.medical.api.dto;

import com.tech_challenge.medical.api.validation.FileType;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public record TextUploadRequest(

        @FileType(types = {MediaType.APPLICATION_PDF_VALUE, MediaType.TEXT_PLAIN_VALUE})
        MultipartFile file,
        Long patientId,
        String source
) {
}
