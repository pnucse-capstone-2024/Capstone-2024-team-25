package com.topik.topikkorea.write.answer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.helper.write.answer.WriteAnswerFixture;
import com.topik.topikkorea.helper.write.problem.WriteProblemFixture;
import com.topik.topikkorea.helper.write.question.WriteQuestionFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import com.topik.topikkorea.write.domain.WriteProblemType;
import com.topik.topikkorea.write.domain.answer.WriteAnswer;
import com.topik.topikkorea.write.domain.answer.WriteAnswerBundle;
import com.topik.topikkorea.write.domain.answer.repository.WriteAnswerBundleRepository;
import com.topik.topikkorea.write.domain.answer.repository.WriteAnswerRepository;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import com.topik.topikkorea.write.domain.problem.repository.WriteProblemRepository;
import com.topik.topikkorea.write.domain.question.WriteQuestion;
import com.topik.topikkorea.write.domain.question.repository.WriteQuestionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WriteAnswerRepositoryTest {
    @Autowired
    private WriteProblemRepository writeProblemRepository;

    @Autowired
    private WriteQuestionRepository writeQuestionRepository;

    @Autowired
    private WriteAnswerRepository writeAnswerRepository;

    @Autowired
    private WriteAnswerBundleRepository writeAnswerBundleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private WriteProblem writeProblem;

    private WriteQuestion writeQuestion;

    private WriteAnswer writeAnswer;

    private WriteAnswerBundle bundle;

    private Member member;

    @Transactional
    @BeforeEach
    public void init() {
        writeProblem = WriteProblemFixture.testWriteProblem(WriteProblemType.WRITING_2_PROBLEM_TYPE_1);
        writeProblemRepository.save(writeProblem);

        writeQuestion = WriteQuestionFixture.testWriteQuestion(writeProblem);
        writeQuestionRepository.save(writeQuestion);

        member = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        memberRepository.save(member);

        bundle = WriteAnswerBundle.builder()
                .member(member)
                .build();
        writeAnswerBundleRepository.save(bundle);

        writeAnswer = WriteAnswerFixture.testWriteAnswer(writeQuestion, bundle, member);
    }

    @Test
    @Transactional
    @DisplayName("[success] WriteAnswer를 저장에 성공")
    public void save_write_answer() {
        writeAnswerRepository.save(writeAnswer);

        assertThat(writeAnswer.getId()).isNotNull();
    }
}
