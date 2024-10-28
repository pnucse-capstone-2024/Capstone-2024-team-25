package com.topik.topikkorea.gather.application;

import com.topik.topikkorea.gather.application.dto.request.CreateGatherRequest;
import com.topik.topikkorea.gather.application.dto.request.OfferGatherRequest;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.gather.domain.GatherOffer;
import com.topik.topikkorea.member.domain.Member;
import java.util.List;

public interface GatherService {
    void insertGather(final CreateGatherRequest request, Member member);

    void updateGather(final Long centerId, final CreateGatherRequest request, Member member);

    void deleteGather(final Long centerId);

    void offerGather(OfferGatherRequest request, Member member);

    GatherOffer getGatherOffer(Long offerId);

    List<GatherOffer> getGatherOffersByGatherId(Long gatherId);

    void deleteOfferGather(Long offerId);

    Gather getGather(final Long gatherId);

    List<Gather> getGathersByCenter(Member member);
}
