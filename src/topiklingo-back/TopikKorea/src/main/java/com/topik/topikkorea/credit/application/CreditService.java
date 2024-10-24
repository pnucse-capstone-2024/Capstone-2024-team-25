package com.topik.topikkorea.credit.application;

import com.topik.topikkorea.credit.application.dto.request.GetCreditsRequest;
import com.topik.topikkorea.credit.application.dto.request.GiveCreditRequest;
import com.topik.topikkorea.credit.application.dto.request.UseCreditRequest;
import com.topik.topikkorea.credit.application.dto.response.UnusedCreditResponse;
import com.topik.topikkorea.credit.application.dto.response.UsedCreditResponse;
import java.util.List;

public interface CreditService {

    void giveCredits(GiveCreditRequest request);

    List<UnusedCreditResponse> getUnusedCredit(GetCreditsRequest request);

    List<UsedCreditResponse> getUsedCredit(GetCreditsRequest request);

    void useCredit(UseCreditRequest request);
}
