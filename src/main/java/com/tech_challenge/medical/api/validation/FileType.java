package com.tech_challenge.medical.api.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileType {

    String message() default "{file.invalid.type}";

    /*
     * Allowed file types (MIME types)
     */
    String[] types();

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}
