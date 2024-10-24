package com.topik.topikkorea.file.application;

import static com.topik.topikkorea.file.exception.FileExceptionType.FAIL_UPLOAD_IMAGE_FILE;
import static com.topik.topikkorea.file.exception.FileExceptionType.INVALID_FILE_FORMAT;

import com.amazonaws.services.s3.AmazonS3;
import com.topik.topikkorea.file.domain.ObjectType;
import com.topik.topikkorea.file.domain.repository.FileRepository;
import com.topik.topikkorea.file.exception.FileException;
import com.topik.topikkorea.file.exception.FileExceptionType;
import com.topik.topikkorea.problem.domain.answer.Answer;
import com.topik.topikkorea.problem.domain.answer.repository.AnswerRepository;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.problem.repository.ProblemRepository;
import com.topik.topikkorea.problem.domain.question.Question;
import com.topik.topikkorea.problem.domain.question.repository.QuestionRepository;
import com.topik.topikkorea.problem.exception.answer.AnswerException;
import com.topik.topikkorea.problem.exception.answer.AnswerExceptionType;
import com.topik.topikkorea.problem.exception.question.QuestionException;
import com.topik.topikkorea.problem.exception.question.QuestionExceptionType;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3ImageFileService extends S3FileService {
    protected final AnswerRepository answerRepository;
    protected final QuestionRepository questionRepository;
    protected final ProblemRepository problemRepository;

    private static final List<String> IMAGE_MIME_TYPES = List.of("image/jpeg", "image/png", "image/gif", "image/webp");

    @Value("${s3.folder.imageFolder}")
    private String imageFolder;

    @Autowired
    public S3ImageFileService(final FileRepository fileRepository, final AmazonS3 amazonS3,
                              AnswerRepository answerRepository, QuestionRepository questionRepository,
                              ProblemRepository problemRepository) {
        super(fileRepository, amazonS3);
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.problemRepository = problemRepository;
    }

    @Override
    protected FileExceptionType failUploadErrorMessage() {
        return FAIL_UPLOAD_IMAGE_FILE;
    }

    @Override
    public void validateExtension(final String mimeType) {
        final boolean isValidate = IMAGE_MIME_TYPES.stream()
                .anyMatch(validType -> validType.equalsIgnoreCase(mimeType));
        if (!isValidate) {
            throw new FileException(INVALID_FILE_FORMAT);
        }
    }

    @Override
    public String getFileFolder() {
        return imageFolder;
    }

    @Override
    public void saveObjectRepository(String objectId, String objectType, String url) {
        switch (ObjectType.valueOf(objectType.toUpperCase())) {
            case PROBLEM:
                Problem problem = problemRepository.findById(objectId);
                problem.setExample(url);
                problemRepository.save(problem);
                break;
            case QUESTION:
                Question question = questionRepository.findById(objectId)
                        .orElseThrow(() -> new QuestionException(QuestionExceptionType.QUESTION_NOT_FOUND));
                question.setExample(url);
                questionRepository.save(question);
                break;
            case ANSWER:
                Answer answer = answerRepository.findById(objectId)
                        .orElseThrow(() -> new AnswerException(AnswerExceptionType.ANSWER_NOT_FOUND));
                answer.setAnswer(url);
                answerRepository.save(answer);
                break;
            default:
                break;
        }
    }
}
