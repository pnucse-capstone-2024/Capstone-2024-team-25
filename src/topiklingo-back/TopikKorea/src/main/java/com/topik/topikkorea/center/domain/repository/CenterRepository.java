package com.topik.topikkorea.center.domain.repository;

import com.topik.topikkorea.center.domain.Center;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CenterRepository extends JpaRepository<Center, Long> {
    @Modifying
    @Query("UPDATE Center c SET c.isDeleted = true WHERE c.id = :id")
    void deleteById(Long id);

    @Query("SELECT c FROM Center c WHERE c.id = :id AND c.isDeleted = false")
    Optional<Center> findById(Long id);

    @Query("SELECT c FROM Center c WHERE c.isDeleted = false")
    Optional<List<Center>> findCenters();
}
