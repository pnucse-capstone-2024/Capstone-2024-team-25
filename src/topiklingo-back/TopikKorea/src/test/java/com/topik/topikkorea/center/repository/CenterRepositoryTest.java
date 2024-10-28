package com.topik.topikkorea.center.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.repository.CenterRepository;
import com.topik.topikkorea.helper.center.CenterFixture;
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
public class CenterRepositoryTest {
    @Autowired
    private CenterRepository centerRepository;

    private Center center;

    @BeforeEach
    public void init() {
        center = CenterFixture.testCenter();
    }

    @Test
    @Transactional
    @DisplayName("[success] 센터 저장 테스트")
    public void insertCenter_센터_저장() {
        // when
        Center savedCenter = centerRepository.save(center);

        // then
        assertThat(savedCenter.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 센터 조회 테스트")
    public void findCenterById_센터_조회() {
        // given
        Center savedCenter = centerRepository.save(center);

        // when
        Center findCenter = centerRepository.findById(savedCenter.getId()).get();

        // then
        assertThat(findCenter).isEqualTo(savedCenter);
    }

    @Test
    @Transactional
    @DisplayName("[success] 센터 삭제 테스트")
    public void deleteCenter_센터_삭제() {
        // given
        Center savedCenter = centerRepository.save(center);

        // when
        centerRepository.deleteById(savedCenter.getId());

        // then
        assertThat(centerRepository.findById(savedCenter.getId())).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("[success] 센터 리스트 조회 테스트")
    public void findCenters_센터_리스트_조회() {
        // given
        centerRepository.save(center);
        Center center2 = CenterFixture.testCenter();
        centerRepository.save(center2);

        // when
        List<Center> centers = centerRepository.findCenters().get();

        // then
        assertThat(centers.size()).isEqualTo(2);
    }
}
