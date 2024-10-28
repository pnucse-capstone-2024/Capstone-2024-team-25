package com.topik.topikkorea.member.api;

import com.topik.topikkorea.member.annotation.AuthCheck;
import com.topik.topikkorea.member.application.MemberService;
import com.topik.topikkorea.member.application.dto.request.GoogleLoginRequest;
import com.topik.topikkorea.member.application.dto.request.MemberDetailRequest;
import com.topik.topikkorea.member.application.dto.request.UpdateAuthRequest;
import com.topik.topikkorea.member.application.dto.response.LoginResponse;
import com.topik.topikkorea.member.application.dto.response.MemberInfoResponse;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.utils.http.HttpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "회원 관련 api")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final HttpRequest httpRequest;

    @Operation(summary = "구글 로그인")
    @PostMapping("/google")
    public ResponseEntity<LoginResponse> loginByGoogle(@Valid @RequestBody final GoogleLoginRequest request) {
        return ResponseEntity.ok(memberService.loginByGoogle(request));
    }

    @Operation(summary = "회원 상세 정보 추가 - 국가, 성별, 생일")
    @AuthCheck(AuthType.UNVERIFIED_USER)
    @PostMapping("/{memberId}/detail")
    public ResponseEntity<Void> insertMemberDetail(@PathVariable Long memberId,
                                                   @Valid @RequestBody final MemberDetailRequest request) {
        memberService.insertMemberDetail(memberId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 권한 변경")
    @PatchMapping("/auth")
    public ResponseEntity<Void> updateMemberAuth(@RequestBody final UpdateAuthRequest request) {
        memberService.updateMemberAuth(request.email(), request.authType());
        return ResponseEntity.ok().build();
    }

    // center의 가입 오퍼를 승인한다.
    @Operation(summary = "센터 가입 승인")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER})
    @PostMapping("/center/offer/{offerId}")
    public ResponseEntity<Void> insertMemberCenter(@PathVariable final Long offerId) {
        memberService.insertMemberCenter(offerId, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    // group의 가입 오퍼를 승인한다.
    @Operation(summary = "그룹 가입 승인")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER})
    @PostMapping("/group/offer/{offerId}")
    public ResponseEntity<Void> insertMemberGroup(@PathVariable final Long offerId) {
        memberService.insertMemberGather(offerId, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 정보 조회")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@PathVariable final Long memberId) {
        return ResponseEntity.ok(memberService.getMemberInfo(memberId, httpRequest.getCurrentMember()));
    }

    @Operation(summary = "토큰 유효 여부 조회")
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate() {
        httpRequest.validateFromToken();
        return ResponseEntity.ok().build();
    }
}
