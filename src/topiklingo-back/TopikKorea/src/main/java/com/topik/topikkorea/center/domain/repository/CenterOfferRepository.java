package com.topik.topikkorea.center.domain.repository;

import com.topik.topikkorea.center.domain.CenterOffer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CenterOfferRepository extends JpaRepository<CenterOffer, Long> {
    @Query("SELECT co FROM CenterOffer co WHERE co.center.id = :centerId AND co.center.isDeleted = false AND co.member.isDeleted = false")
    Optional<List<CenterOffer>> findCenterOffersByCenter(Long centerId);

    @Query("SELECT co FROM CenterOffer co WHERE co.member.id = :memberId AND co.center.isDeleted = false AND co.member.isDeleted = false")
    Optional<CenterOffer> findCenterOfferByMember(Long memberId);

    @Query("SELECT co FROM CenterOffer co WHERE co.id = :id AND co.center.isDeleted = false AND co.member.isDeleted = false")
    Optional<CenterOffer> findById(Long id);
}
