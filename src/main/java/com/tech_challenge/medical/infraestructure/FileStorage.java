package com.tech_challenge.medical.infraestructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Component
public class FileStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorage.class);

    public String store(MultipartFile file) throws IOException {
        LOGGER.info("Storing report for file: {}", file.getOriginalFilename());

        try (FileOutputStream outputStream = new FileOutputStream(Objects.requireNonNull(file.getOriginalFilename()))) {
            outputStream.write(Objects.requireNonNull(file.getBytes()));
        }

        LOGGER.info("File stored successfully");

        return file.getOriginalFilename();
    }

}
