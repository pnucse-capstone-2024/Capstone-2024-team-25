package com.topik.topikkorea.gather.domain.repository;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.gather.domain.Gather;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatherRepository extends JpaRepository<Gather, Long> {
    List<Gather> findAllByCenter(Center center);
}
