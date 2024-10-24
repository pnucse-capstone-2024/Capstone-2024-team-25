package com.topik.topikkorea.center.api;

import com.topik.topikkorea.center.application.CenterService;
import com.topik.topikkorea.center.application.dto.request.CreateCenterRequest;
import com.topik.topikkorea.center.application.dto.request.OfferCenterRequest;
import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.CenterOffer;
import com.topik.topikkorea.member.annotation.AuthCheck;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.utils.http.HttpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Center", description = "센터 관련 api")
@RestController
@RequestMapping("/center")
@RequiredArgsConstructor
public class CenterController {
    private final CenterService centerService;
    private final HttpRequest httpRequest;

    @Operation(summary = "center 생성")
    @AuthCheck(AuthType.ADMIN)
    @PostMapping("/")
    public ResponseEntity<Void> createCenter(@RequestBody CreateCenterRequest request) {
        centerService.insertCenter(request);
        return ResponseEntity.ok().build();
    }

    // 센터에 가입 오퍼를 넣는다.
    @Operation(summary = "센터 가입 오퍼 등록")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @PostMapping("/offer")
    public ResponseEntity<Void> offerCenter(@RequestBody OfferCenterRequest request) {
        centerService.offerCenter(request, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    // 센터의 가입 오퍼 목록을 가져온다.
    @Operation(summary = "자신의 센터 가입 오퍼 목록 조회")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER})
    @GetMapping("/offer")
    public ResponseEntity<List<CenterOffer>> getCenterOffers() {
        List<CenterOffer> centerOffers = centerService.getCenterOffersByCenterId(
                httpRequest.getCurrentMember().getCenter().getId());
        return ResponseEntity.ok(centerOffers);
    }

    // 센터의 목록을 가져온다.
    @Operation(summary = "센터 목록 조회")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @GetMapping("/")
    public ResponseEntity<List<Center>> getCenters() {
        List<Center> centers = centerService.getCenters();
        return ResponseEntity.ok(centers);
    }

    // 센터를 업데이트 한다.
    @Operation(summary = "센터 정보 업데이트 - 이름, 주소, 국가")
    @AuthCheck(AuthType.ADMIN)
    @PutMapping("/{centerId}")
    public ResponseEntity<Void> updateCenter(@PathVariable Long centerId, @RequestBody CreateCenterRequest request) {
        centerService.updateCenter(centerId, request);
        return ResponseEntity.ok().build();
    }

    // 센터를 삭제 한다.
    @Operation(summary = "센터 삭제")
    @AuthCheck(AuthType.ADMIN)
    @DeleteMapping("/{centerId}")
    public ResponseEntity<Void> deleteCenter(@PathVariable Long centerId) {
        centerService.deleteCenter(centerId);
        return ResponseEntity.ok().build();
    }

    // 가입 오퍼를 삭제한다.
    @Operation(summary = "센터 가입 오퍼 삭제")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER})
    @DeleteMapping("/offer/{offerId}")
    public ResponseEntity<Void> deleteOfferCenter(@PathVariable Long offerId) {
        centerService.deleteOfferCenter(offerId);
        return ResponseEntity.ok().build();
    }
}
