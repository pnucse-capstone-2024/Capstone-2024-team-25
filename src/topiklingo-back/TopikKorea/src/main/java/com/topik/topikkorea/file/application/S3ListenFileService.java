package com.topik.topikkorea.file.application;

import static com.topik.topikkorea.file.exception.FileExceptionType.FAIL_UPLOAD_LISTEN_FILE;
import static com.topik.topikkorea.file.exception.FileExceptionType.INVALID_FILE_FORMAT;

import com.amazonaws.services.s3.AmazonS3;
import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.repository.ExamRepository;
import com.topik.topikkorea.exam.exception.ExamException;
import com.topik.topikkorea.exam.exception.ExamExceptionType;
import com.topik.topikkorea.file.domain.repository.FileRepository;
import com.topik.topikkorea.file.exception.FileException;
import com.topik.topikkorea.file.exception.FileExceptionType;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3ListenFileService extends S3FileService {
    protected final ExamRepository examRepository;

    private static final List<String> LISTEN_MIME_TYPES = List.of(
            "video/mp4", "video/x-msvideo", "video/webm", //mp4, avi, webm
            "audio/mpeg", "audio/wav", "audio/webm"// mp3, wav, webm
    );

    @Value("${s3.folder.listenFolder}")
    private String listenFolder;

    public S3ListenFileService(final FileRepository fileRepository, AmazonS3 amazonS3, ExamRepository examRepository) {
        super(fileRepository, amazonS3);
        this.examRepository = examRepository;
    }

    @Override
    protected FileExceptionType failUploadErrorMessage() {
        return FAIL_UPLOAD_LISTEN_FILE;
    }

    @Override
    void validateExtension(String mimeType) {
        final boolean isValidate = LISTEN_MIME_TYPES.stream()
                .anyMatch(validType -> validType.equalsIgnoreCase(mimeType));
        if (!isValidate) {
            throw new FileException(INVALID_FILE_FORMAT);
        }
    }

    @Override
    public void saveObjectRepository(String objectId, String objectType, String url) {
        Exam exam = examRepository.findById(objectId)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));
        exam.setListenUrl(url);
        examRepository.save(exam);
    }

    @Override
    String getFileFolder() {
        return listenFolder;
    }
}
