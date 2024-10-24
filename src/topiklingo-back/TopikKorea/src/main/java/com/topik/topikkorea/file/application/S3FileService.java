package com.topik.topikkorea.file.application;

import static com.topik.topikkorea.file.exception.FileExceptionType.FILE_IS_NULL;
import static com.topik.topikkorea.file.exception.FileExceptionType.INVALID_FILE_ACCESS;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.topik.topikkorea.file.application.dto.request.FileRequest;
import com.topik.topikkorea.file.domain.File;
import com.topik.topikkorea.file.domain.repository.FileRepository;
import com.topik.topikkorea.file.exception.FileException;
import com.topik.topikkorea.file.exception.FileExceptionType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public abstract class S3FileService {
    protected final FileRepository fileRepository;
    protected final String EXTENSION_DELIMITER = ".";
    protected final AmazonS3 amazonS3;

    @Value("${s3.bucket}")
    protected String bucket;

    protected S3FileService(FileRepository fileRepository, AmazonS3 amazonS3) {
        this.fileRepository = fileRepository;
        this.amazonS3 = amazonS3;
    }

    public String upload(final String objectId, final String objectType, final MultipartFile file) {
        long startTime = System.currentTimeMillis();
        validateNotNull(file);
        final String fileExtension = getFileExtension(file);
        final String newFileName = createFileName(objectId, fileExtension);
        final ObjectMetadata objectMetadata = getObjectMetadata(file);

        try (final InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, newFileName, inputStream, objectMetadata));
        } catch (final IOException exception) {
            throw new FileException(failUploadErrorMessage());
        }
        long endTime = System.currentTimeMillis();
        log.info("{} - 실행 시간: {}", Thread.currentThread().getName(), endTime - startTime);
        String fileUrl = amazonS3.getUrl(bucket, newFileName).toString();
//        saveRepository(objectId, fileUrl);
        saveObjectRepository(objectId, objectType, fileUrl);

        return fileUrl;
    }

    abstract FileExceptionType failUploadErrorMessage();

    protected String getFileExtension(final MultipartFile file) {
        final String originalFileName = file.getOriginalFilename();
        final int extensionIndex = Objects.requireNonNull(originalFileName)
                .lastIndexOf(EXTENSION_DELIMITER);

        try {
            String mimeType = new Tika().detect(file.getInputStream());
            validateExtension(mimeType);
        } catch (IOException e) {
            throw new FileException(INVALID_FILE_ACCESS);
        }
        return originalFileName.substring(extensionIndex);
    }

    abstract void validateExtension(String mimeType);

    protected ObjectMetadata getObjectMetadata(final MultipartFile multipartFile) {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }

    protected String createFileName(final String objectId, final String fileExtension) {
        return getFileFolder()+"/"+objectId.concat(fileExtension);
    }

    abstract String getFileFolder();

    abstract void saveObjectRepository(String objectId, String objectType, String url);

    protected void validateNotNull(final MultipartFile file) {
        if (file == null) {
            throw new FileException(FILE_IS_NULL);
        }
    }

    public void deleteFile(final String fileName) {
        final String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        amazonS3.deleteObject(bucket, decodedFileName);
    }

    public void saveRepository(final String objectId, final String fileUrl) {
        fileRepository.save(File.builder()
                .objectId(objectId)
                .fileUrl(fileUrl)
                .build());
    }
}
