package com.topik.topikkorea.gather.application;

import com.topik.topikkorea.gather.application.dto.request.CreateGatherRequest;
import com.topik.topikkorea.gather.application.dto.request.OfferGatherRequest;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.gather.domain.GatherOffer;
import com.topik.topikkorea.gather.domain.repository.GatherOfferRepository;
import com.topik.topikkorea.gather.domain.repository.GatherRepository;
import com.topik.topikkorea.gather.exception.GatherException;
import com.topik.topikkorea.gather.exception.GatherExceptionType;
import com.topik.topikkorea.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GatherServiceImpl implements GatherService {

    private final GatherRepository gatherRepository;
    private final GatherOfferRepository gatherOfferRepository;

    // 그룹을 추가한다.
    @Override
    public void insertGather(CreateGatherRequest request, Member member) {
        Gather gather = Gather.builder()
                .name(request.name())
                .center(member.getCenter())
                .teacher(member)
                .build();
        gatherRepository.save(gather);
    }

    // 그룹을 업데이트한다.
    @Override
    public void updateGather(Long centerId, CreateGatherRequest request, Member member) {
        Gather gather = gatherRepository.findById(centerId)
                .orElseThrow(() -> new GatherException(GatherExceptionType.NOT_FOUND_GROUP));
        gather.update(request.name(), gather.getCenter(), member);
        gatherRepository.save(gather);
    }

    // 그룹을 삭제한다.
    @Override
    public void deleteGather(Long centerId) {
        gatherRepository.deleteById(centerId);
    }

    // 그룹을 가져온다.
    @Override
    public Gather getGather(Long gatherId) {
        return gatherRepository.findById(gatherId)
                .orElseThrow(() -> new GatherException(GatherExceptionType.NOT_FOUND_GROUP));
    }

    @Override
    public List<Gather> getGathersByCenter(Member member) {
        return gatherRepository.findAllByCenter(member.getCenter());
    }

    // 그룹 가입 오퍼를 넣는다.
    @Override
    public void offerGather(OfferGatherRequest request, Member member) {
        if (gatherOfferRepository.findGatherOffersByMember(member).isPresent()) {
            throw new GatherException(GatherExceptionType.ALREADY_EXIST_GROUP_OFFER);
        }
        GatherOffer gatherOffer = GatherOffer.builder()
                .gather(getGather(request.gatherId()))
                .member(member)
                .build();

        gatherOfferRepository.save(gatherOffer);
    }

    // 그룹 가입 오퍼를 가져온다.
    @Override
    public GatherOffer getGatherOffer(Long offerId) {
        return gatherOfferRepository.findById(offerId)
                .orElseThrow(() -> new GatherException(GatherExceptionType.NOT_FOUND_GROUP_OFFER));
    }

    // 그룹 가입 오퍼들을 가져온다.
    @Override
    public List<GatherOffer> getGatherOffersByGatherId(Long gatherId) {
        Gather gather = getGather(gatherId);
        return gatherOfferRepository.findGatherOffersByGather(gather)
                .orElseThrow(() -> new GatherException(GatherExceptionType.NOT_FOUND_GROUP_OFFER));
    }

    // 그룹의 가입 오퍼 목록을 삭제한다.
    @Override
    public void deleteOfferGather(Long offerId) {
        gatherOfferRepository.deleteById(offerId);
    }
}
