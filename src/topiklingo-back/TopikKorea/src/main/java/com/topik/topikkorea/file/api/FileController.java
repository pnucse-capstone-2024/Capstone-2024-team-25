package com.topik.topikkorea.file.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.topik.topikkorea.file.application.S3ImageFileService;
import com.topik.topikkorea.file.application.S3ListenFileService;

@Controller
@RequiredArgsConstructor
public class FileController {
    private final S3ImageFileService imageFileService;
    private final S3ListenFileService listenFileService;
    @PostMapping("/file/image")
    public ResponseEntity<String> uploadImage(
            @RequestParam("objectId") String objectId,
            @RequestParam("objectType") String objectType,
            @RequestParam("image") MultipartFile file) {
        try {
            String fileUrl = imageFileService.upload(objectId, objectType, file);
            return ResponseEntity.ok("Image File uploaded successfully: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/file/listen")
    public ResponseEntity<String> uploadListen(
            @RequestParam("objectId") String objectId,
            @RequestParam("objectType") String objectType,
            @RequestParam("listen") MultipartFile file) {
        try {
            String fileUrl = listenFileService.upload(objectId, objectType, file);
            return ResponseEntity.ok("File uploaded successfully: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @DeleteMapping("/file")
    public ResponseEntity<String> deleteFile(@RequestParam("fileName") String fileName, @RequestParam("type") String type) {
        try {
            if ("image".equalsIgnoreCase(type)) {
                imageFileService.deleteFile(fileName);
            } else if ("listen".equalsIgnoreCase(type)) {
                listenFileService.deleteFile(fileName);
            } else {
                return ResponseEntity.badRequest().body("Invalid file type");
            }
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
