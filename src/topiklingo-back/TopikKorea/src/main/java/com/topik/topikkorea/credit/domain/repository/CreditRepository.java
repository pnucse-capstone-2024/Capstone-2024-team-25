package com.topik.topikkorea.credit.domain.repository;

import com.topik.topikkorea.credit.domain.Credit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    @Query("select c from Credit c where c.receiver.id = :receiverId and c.used = false")
    List<Credit> findUnusedCredits(Long receiverId);

    @Query("select c from Credit c where c.receiver.id = :receiverId and c.used = false order by c.createdAt asc limit 1")
    Optional<Credit> findOldestCredit(Long receiverId);

    @Query("select c from Credit c where c.receiver.id = :receiverId and c.used = true")
    List<Credit> findUsedCredits(Long receiverId);
}
