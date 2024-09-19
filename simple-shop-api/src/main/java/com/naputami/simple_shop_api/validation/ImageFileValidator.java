package com.naputami.simple_shop_api.validation;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;

import jakarta.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private long maxSize;
    private boolean allowNull;

    @Override
    public void initialize(ValidImage constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null) {
            return allowNull;
        }

        if (file != null && !isSupportedContentType(file.getContentType())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File must be an image").addConstraintViolation();
            return false;
        }

        if (file != null && file.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File must not be larger than " + maxSize + " bytes").addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg"));
    }
}
