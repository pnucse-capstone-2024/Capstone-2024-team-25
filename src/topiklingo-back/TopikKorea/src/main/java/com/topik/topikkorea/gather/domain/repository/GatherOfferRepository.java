package com.topik.topikkorea.gather.domain.repository;

import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.gather.domain.GatherOffer;
import com.topik.topikkorea.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GatherOfferRepository extends JpaRepository<GatherOffer, Long> {
    @Query("SELECT go FROM GatherOffer go WHERE go.gather = :gather AND go.member.isDeleted = false")
    Optional<List<GatherOffer>> findGatherOffersByGather(Gather gather);

    @Query("SELECT go FROM GatherOffer go WHERE go.member = :member AND go.member.isDeleted = false")
    Optional<GatherOffer> findGatherOffersByMember(Member member);
}
