package com.utcl.oneultratech.file.service.controller;

import com.utcl.oneultratech.file.service.dto.ResponseDto;
import com.utcl.oneultratech.file.service.model.FileDB;
import com.utcl.oneultratech.file.service.service.DBFileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {


    private final DBFileStorageService service;

    @PostMapping("/uploadFile")
    public ResponseDto uploadFile(@RequestParam("file") MultipartFile file) {
        FileDB fileName = service.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName.getName())
                .toUriString();

        return new ResponseDto(fileName.getName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
         FileDB file = service.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                        file.getName() + "."+file.getType() + "\"")
                .body(new ByteArrayResource(file.getData()));
    }


}
