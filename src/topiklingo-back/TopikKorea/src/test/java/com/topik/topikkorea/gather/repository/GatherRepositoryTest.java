package com.topik.topikkorea.gather.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.repository.CenterRepository;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.gather.domain.repository.GatherRepository;
import com.topik.topikkorea.helper.center.CenterFixture;
import com.topik.topikkorea.helper.gather.GatherFixture;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class GatherRepositoryTest {
    @Autowired
    private GatherRepository gatherRepository;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Gather gather;

    private Center center;

    private Member teacher;

    @BeforeEach
    @Transactional
    public void init() {
        center = CenterFixture.testCenter();
        centerRepository.save(center);

        teacher = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        memberRepository.save(teacher);

        gather = GatherFixture.testGather(center, teacher);
    }

    @Test
    @Transactional
    @DisplayName("[success] 그룹 생성 성공")
    public void insertGather_그룹_생성_성공() {
        // when
        gatherRepository.save(gather);

        // then
        assertThat(gather.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 그룹 조회 성공")
    public void getGather_그룹_조회_성공() {
        // given
        gatherRepository.save(gather);

        // when
        final Gather savedGather = gatherRepository.findById(gather.getId()).get();

        // then
        assertThat(savedGather.getId()).isEqualTo(gather.getId());
    }

    @Test
    @Transactional
    @DisplayName("[success] 그룹 삭제 성공")
    public void deleteGather_그룹_삭제_성공() {
        // given
        gatherRepository.save(gather);

        // when
        gatherRepository.deleteById(gather.getId());

        // then
        assertThat(gatherRepository.findById(gather.getId())).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("[success] 그룹 삭제후 member 조회 성공")
    public void deleteGather_그룹_삭제후_member_조회_성공() {
        // given
        gatherRepository.save(gather);

        // when
        gatherRepository.deleteById(gather.getId());

        // then
        assertThat(memberRepository.findById(teacher.getId()).get()).isEqualTo(teacher);
    }

    @Test
    @Transactional
    @DisplayName("[success] 센터 아이디로 그룹 조회 성공")
    public void getGathersByCenter_센터_아이디로_그룹_조회_성공() {
        // given
        gatherRepository.save(gather);
        Gather gather2 = GatherFixture.testGather(center, teacher);
        gatherRepository.save(gather2);

        // when
        final List<Gather> savedGathers = gatherRepository.findAllByCenter(center);

        // then
        assertThat(savedGathers.size()).isEqualTo(2);
    }
}
