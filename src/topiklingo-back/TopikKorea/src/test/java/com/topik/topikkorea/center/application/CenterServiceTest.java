package com.topik.topikkorea.center.application;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.repository.CenterOfferRepository;
import com.topik.topikkorea.center.domain.repository.CenterRepository;
import com.topik.topikkorea.helper.center.CenterFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CenterServiceTest {
    @Mock
    private CenterRepository centerRepository;

    @Mock
    private CenterOfferRepository centerOfferRepository;

    @InjectMocks
    private CenterServiceImpl centerService;

    private Center center;

    @BeforeEach
    public void init() {
        center = CenterFixture.testIdCenter();
    }
}
