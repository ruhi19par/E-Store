package com.example.lcwd.Electronic.services.impl;

import com.example.lcwd.Electronic.exceptions.BadApiRequest;
import com.example.lcwd.Electronic.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger =
            LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) {

        try {
            logger.info("Upload method called");

            String originalFileName = file.getOriginalFilename();
            logger.info("Original Filename: {}", originalFileName);

            if (originalFileName == null || !originalFileName.contains(".")) {
                throw new BadApiRequest("Invalid file name");
            }

            // generate unique file name
            String fileName = UUID.randomUUID().toString();
            String extension = originalFileName.substring(
                    originalFileName.lastIndexOf(".")
            );

            String fileNameWithExt = fileName + extension;

            // make path absolute (VERY IMPORTANT)
            String fullBasePath = System.getProperty("user.dir")
                    + File.separator + path;

            String fullPathWithFileName =
                    fullBasePath + File.separator + fileNameWithExt;

            logger.info("Final upload path: {}", fullPathWithFileName);

            // validate extension
            if (extension.equalsIgnoreCase(".png") ||
                    extension.equalsIgnoreCase(".jpg") ||
                    extension.equalsIgnoreCase(".jpeg")) {

                File folder = new File(fullBasePath);

                if (!folder.exists()) {
                    boolean created = folder.mkdirs();
                    logger.info("Folder created: {}", created);
                }

                Files.copy(
                        file.getInputStream(),
                        Paths.get(fullPathWithFileName)
                );

                logger.info("File uploaded successfully: {}", fileNameWithExt);

            } else {
                throw new BadApiRequest(
                        "File with extension " + extension + " not allowed"
                );
            }

            return fileNameWithExt;

        } catch (IOException e) {
            logger.error("File upload failed", e);
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Override
    public InputStream getResource(String path, String name) {

        try {
            String fullPath = System.getProperty("user.dir")
                    + File.separator + path
                    + File.separator + name;

            logger.info("Fetching file from: {}", fullPath);

            return new FileInputStream(fullPath);

        } catch (FileNotFoundException e) {
            logger.error("File not found: {}", name);
            throw new RuntimeException("File not found: " + name, e);
        }
    }
}