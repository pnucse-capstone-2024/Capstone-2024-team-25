package com.topik.topikkorea.center.application;

import com.topik.topikkorea.center.application.dto.request.CreateCenterRequest;
import com.topik.topikkorea.center.application.dto.request.OfferCenterRequest;
import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.CenterOffer;
import com.topik.topikkorea.member.domain.Member;
import java.util.List;

public interface CenterService {
    void insertCenter(final CreateCenterRequest request);

    void updateCenter(final Long centerId, final CreateCenterRequest request);

    void deleteCenter(final Long centerId);

    void offerCenter(OfferCenterRequest request, Member member);

    CenterOffer getCenterOffer(Long offerId);

    List<CenterOffer> getCenterOffersByCenterId(Long centerId);

    void deleteOfferCenter(Long offerId);

    Center getCenter(final Long centerId);

    List<Center> getCenters();
}
