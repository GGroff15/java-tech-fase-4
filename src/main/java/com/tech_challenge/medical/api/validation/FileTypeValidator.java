package com.tech_challenge.medical.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {

    private String[] allowedTypes;

    @Override
    public void initialize(FileType constraintAnnotation) {
        this.allowedTypes = constraintAnnotation.types();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        String contentType = file.getContentType();
        for (String type : allowedTypes) {
            if (type.equalsIgnoreCase(contentType)) {
                return true;
            }
        }
        return false;
    }
}
