package com.topik.topikkorea.center.application;

import com.topik.topikkorea.center.application.dto.request.CreateCenterRequest;
import com.topik.topikkorea.center.application.dto.request.OfferCenterRequest;
import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.CenterOffer;
import com.topik.topikkorea.center.domain.repository.CenterOfferRepository;
import com.topik.topikkorea.center.domain.repository.CenterRepository;
import com.topik.topikkorea.center.exception.CenterException;
import com.topik.topikkorea.center.exception.CenterExceptionType;
import com.topik.topikkorea.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService {

    private final CenterRepository centerRepository;
    private final CenterOfferRepository centerOfferRepository;

    // 센터를 추가한다.
    @Override
    public void insertCenter(CreateCenterRequest request) {
        Center center = Center.builder()
                .name(request.name())
                .nation(request.nation())
                .address(request.address())
                .build();
        centerRepository.save(center);
    }

    // 센터를 업데이트한다.
    @Override
    public void updateCenter(Long centerId, CreateCenterRequest request) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new CenterException(CenterExceptionType.NOT_FOUND_CENTER));
        center.update(request.name(), request.nation(), request.address());
        centerRepository.save(center);
    }

    // 센터를 삭제한다.
    @Override
    public void deleteCenter(Long centerId) {
        centerRepository.deleteById(centerId);
    }

    // 센터를 가져온다.
    @Override
    public Center getCenter(Long centerId) {
        return centerRepository.findById(centerId)
                .orElseThrow(() -> new CenterException(CenterExceptionType.NOT_FOUND_CENTER));
    }

    // 센터 목록을 가져온다.
    @Override
    public List<Center> getCenters() {
        return centerRepository.findAll();
    }

    // 센터에 가입 오퍼를 넣는다.
    @Override
    public void offerCenter(OfferCenterRequest request, Member member) {
        if (centerOfferRepository.findCenterOfferByMember(member.getId()).isPresent()) {
            throw new CenterException(CenterExceptionType.ALREADY_OFFER_CENTER);
        }
        CenterOffer centerOffer = CenterOffer.builder()
                .center(getCenter(request.centerId()))
                .member(member)
                .build();

        centerOfferRepository.save(centerOffer);
    }

    // 센터에 가입 오퍼를 가져온다.
    @Override
    public CenterOffer getCenterOffer(Long offerId) {
        return centerOfferRepository.findById(offerId)
                .orElseThrow(() -> new CenterException(CenterExceptionType.NOT_FOUND_CENTER_OFFER));
    }

    // 센터에 가입 오퍼 목록을 가져온다.
    @Override
    public List<CenterOffer> getCenterOffersByCenterId(Long centerId) {
        return centerOfferRepository.findCenterOffersByCenter(centerId).get();
    }

    // 센터의 가입 오퍼 목록을 삭제한다.
    @Override
    public void deleteOfferCenter(Long offerId) {
        centerOfferRepository.deleteById(offerId);
    }
}
