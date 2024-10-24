package com.topik.topikkorea.file.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record FileRequest(
        String objectId,
        String objectType,
        MultipartFile file) {
}
