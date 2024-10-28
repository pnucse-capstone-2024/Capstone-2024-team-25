package com.topik.topikkorea.write.answer.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.helper.write.answer.WriteAnswerFixture;
import com.topik.topikkorea.helper.write.problem.WriteProblemFixture;
import com.topik.topikkorea.helper.write.question.WriteQuestionFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.write.application.answer.WriteAnswerServiceImpl;
import com.topik.topikkorea.write.application.answer.dto.request.CreateWriteAnswerRequest;
import com.topik.topikkorea.write.application.answer.dto.request.GraduateWriteAnswerRequest;
import com.topik.topikkorea.write.application.question.WriteQuestionService;
import com.topik.topikkorea.write.domain.WriteProblemType;
import com.topik.topikkorea.write.domain.answer.WriteAnswer;
import com.topik.topikkorea.write.domain.answer.WriteAnswerBundle;
import com.topik.topikkorea.write.domain.answer.repository.WriteAnswerBundleRepository;
import com.topik.topikkorea.write.domain.answer.repository.WriteAnswerRepository;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import com.topik.topikkorea.write.domain.question.WriteQuestion;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WriteAnswerServiceTest {
    @Mock
    private WriteAnswerRepository writeAnswerRepository;

    @Mock
    private WriteQuestionService writeQuestionService;

    @Mock
    private WriteAnswerBundleRepository writeAnswerBundleRepository;

    @InjectMocks
    private WriteAnswerServiceImpl writeAnswerService;

    private WriteAnswer writeAnswer;

    private Member member;

    private WriteProblem writeProblem;

    private WriteQuestion writeQuestion;

    private WriteAnswerBundle bundle;

    private WriteAnswer testWriteAnswer;

    @BeforeEach
    public void init() {
        writeProblem = WriteProblemFixture.testWriteProblem(WriteProblemType.WRITING_2_PROBLEM_TYPE_1);
        writeQuestion = WriteQuestionFixture.testWriteQuestion(writeProblem);
        member = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        bundle = WriteAnswerBundle.builder()
                .member(member)
                .build();
        testWriteAnswer = WriteAnswerFixture.testWriteAnswer(writeQuestion, bundle, member);
    }

    @Test
    @DisplayName("[success] WriteAnswer를 저장에 성공")
    public void save_write_answer() {
        // given
        CreateWriteAnswerRequest request = CreateWriteAnswerRequest.builder()
                .answer(testWriteAnswer.getAnswer())
                .questionId(11)
                .build();
        WriteAnswerBundle bundle = WriteAnswerBundle.builder()
                .member(member)
                .build();
        List<CreateWriteAnswerRequest> listRequest = List.of(request);

        // mocking
        when(writeAnswerBundleRepository.save(any(WriteAnswerBundle.class))).thenReturn(bundle);
        when(writeQuestionService.getWriteQuestion(anyLong())).thenReturn(writeQuestion);
        when(writeAnswerRepository.save(any(WriteAnswer.class))).thenReturn(testWriteAnswer);

        // when
        writeAnswerService.createWriteAnswer(listRequest, member);

        // then
        verify(writeAnswerRepository).save(any(WriteAnswer.class));
    }

    @Test
    @DisplayName("[success] WriteAnswer 채점 성공")
    public void graduate_write_answer() {
        // given
        GraduateWriteAnswerRequest request = GraduateWriteAnswerRequest.builder()
                .answerId(11)
                .score(100)
                .reason("good")
                .build();

        // mocking
        when(writeAnswerRepository.findById(anyLong())).thenReturn(Optional.of(testWriteAnswer));

        // when
        writeAnswerService.GraduateWriteAnswer(request);

        // then
        verify(writeAnswerRepository).save(any(WriteAnswer.class));
        assertThat(testWriteAnswer.getScore()).isEqualTo(100);
        assertThat(testWriteAnswer.getReason()).isEqualTo("good");
    }
}
