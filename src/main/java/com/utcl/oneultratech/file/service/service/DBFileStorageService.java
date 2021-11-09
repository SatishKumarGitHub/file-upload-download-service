package com.utcl.oneultratech.file.service.service;

import com.utcl.oneultratech.file.service.exception.FileStorageException;
import com.utcl.oneultratech.file.service.model.FileDB;
import com.utcl.oneultratech.file.service.repository.FileDBRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DBFileStorageService {

    private final FileDBRepository repository;

    public FileDB storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            FileDB dbFile = new FileDB(null, fileName, file.getContentType(), file.getBytes());

            return repository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public FileDB getFile(String fileId)  {
        return repository.findById(fileId)
                .orElseThrow(() -> new FileStorageException("File not found with id " + fileId));
    }

}
