package com.topik.topikkorea.member.utils.provider.google;

import com.topik.topikkorea.member.application.dto.response.GoogleAccountProfileResponse;


public interface GoogleClient {
    GoogleAccountProfileResponse getGoogleAccountProfile(final String code);
}
