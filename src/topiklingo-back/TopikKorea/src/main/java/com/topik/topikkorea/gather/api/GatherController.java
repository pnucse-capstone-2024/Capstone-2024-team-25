package com.topik.topikkorea.gather.api;

import com.topik.topikkorea.gather.application.GatherService;
import com.topik.topikkorea.gather.application.dto.request.CreateGatherRequest;
import com.topik.topikkorea.gather.application.dto.request.OfferGatherRequest;
import com.topik.topikkorea.gather.domain.Gather;
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

@Tag(name = "Gather", description = "그룹 관련 api")
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GatherController {
    private final GatherService gatherService;
    private final HttpRequest httpRequest;

    // 그룹을 생성한다.
    @Operation(summary = "그룹 생성")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER})
    @PostMapping("/")
    public ResponseEntity<Void> createGroup(@RequestBody CreateGatherRequest request) {
        gatherService.insertGather(request, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    // 그룹 가입 오퍼를 넣는다.
    @Operation(summary = "그룹 가입 오퍼 등록")
    @PostMapping("/offer")
    public ResponseEntity<Void> offerGroup(@RequestBody OfferGatherRequest request) {
        gatherService.offerGather(request, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    // 센터의 그룹의 목록을 가져온다.
    @Operation(summary = "자신의 센터 그룹 목록 조회")
    @GetMapping("/")
    public ResponseEntity<List<Gather>> getGroups() {
        List<Gather> gathers = gatherService.getGathersByCenter(httpRequest.getCurrentMember());
        return ResponseEntity.ok(gathers);
    }

    // 그룹을 업데이트 한다.
    @Operation(summary = "그룹 업데이트 - 이름")
    @PutMapping("/{groupId}")
    public ResponseEntity<Void> updateGroup(@PathVariable Long groupId, @RequestBody CreateGatherRequest request) {
        gatherService.updateGather(groupId, request, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    // 그룹을 삭제한다.
    @Operation(summary = "그룹 삭제")
    @DeleteMapping("/{centerId}")
    public ResponseEntity<Void> deleteCenter(@PathVariable Long centerId) {
        gatherService.deleteGather(centerId);
        return ResponseEntity.ok().build();
    }
}
