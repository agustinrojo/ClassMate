package com.classmate.comment_service.client;

import com.classmate.comment_service.dto.filedtos.FileDTO;
import com.classmate.comment_service.dto.filedtos.FileResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(name = "file-service")
public interface FileServiceClient {
    @PostMapping(value = "/api/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<FileResponseDTO> uploadFile(@RequestPart("file") MultipartFile file);

    @GetMapping("/api/files/{fileId}")
    ResponseEntity<FileDTO> downloadFile(@PathVariable("fileId") Long fileId);
}
