package com.topik.topikkorea.credit.api;

import com.topik.topikkorea.credit.api.dto.GiveCreditApiRequest;
import com.topik.topikkorea.credit.application.CreditService;
import com.topik.topikkorea.credit.application.dto.request.GetCreditsRequest;
import com.topik.topikkorea.credit.application.dto.request.GiveCreditRequest;
import com.topik.topikkorea.credit.application.dto.response.UnusedCreditResponse;
import com.topik.topikkorea.credit.application.dto.response.UsedCreditResponse;
import com.topik.topikkorea.member.annotation.AuthCheck;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.utils.http.HttpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/credit")
@Tag(name = "Credit", description = "크레딧 관련 api")
@RestController
@RequiredArgsConstructor
public class CreditController {
    @Autowired
    private CreditService creditService;

    @Autowired
    private HttpRequest httpRequest;

    @Operation(summary = "크레딧을 부여한다")
    @AuthCheck(AuthType.ADMIN)
    @PostMapping("/")
    public ResponseEntity<Void> giveCredit(@RequestBody GiveCreditApiRequest request) {
        GiveCreditRequest giveCreditRequest = GiveCreditRequest.builder()
                .creditor(httpRequest.getCurrentMember())
                .receiverId(request.receiverId())
                .credit(request.credit())
                .build();

        creditService.giveCredits(giveCreditRequest);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용하지 않는 크레딧 조회")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @GetMapping("/unused")
    public ResponseEntity<List<UnusedCreditResponse>> getUnusedCredits() {
        List<UnusedCreditResponse> creditResponses = creditService.getUnusedCredit(
                GetCreditsRequest.builder()
                        .member(httpRequest.getCurrentMember())
                        .build()
        );

        return ResponseEntity.ok(creditResponses);
    }

    @Operation(summary = "사용한 크레딧 조회")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @GetMapping("/used")
    public ResponseEntity<List<UsedCreditResponse>> getUsedCredits() {
        List<UsedCreditResponse> creditResponses = creditService.getUsedCredit(
                GetCreditsRequest.builder()
                        .member(httpRequest.getCurrentMember())
                        .build()
        );

        return ResponseEntity.ok(creditResponses);
    }
}
