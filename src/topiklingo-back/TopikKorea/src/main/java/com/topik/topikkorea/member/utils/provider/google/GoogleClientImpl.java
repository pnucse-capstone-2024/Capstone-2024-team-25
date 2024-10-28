package com.topik.topikkorea.member.utils.provider.google;

import com.topik.topikkorea.member.application.dto.request.GoogleAccessTokenRequest;
import com.topik.topikkorea.member.application.dto.response.GoogleAccessTokenResponse;
import com.topik.topikkorea.member.application.dto.response.GoogleAccountProfileResponse;
import com.topik.topikkorea.member.exception.MemberException;
import com.topik.topikkorea.member.exception.MemberExceptionType;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GoogleClientImpl implements GoogleClient {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String authorizationCode;

    @Value("${url.access-token}")
    private String accessTokenUrl;

    @Value("${url.profile}")
    private String profileUrl;

    private final RestTemplate restTemplate;

    public GoogleAccountProfileResponse getGoogleAccountProfile(final String code) {
        final String accessToken = requestGoogleAccessToken(code);
        return requestGoogleAccountProfile(accessToken);
    }

    private String requestGoogleAccessToken(final String code) {
        final String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        final HttpEntity<GoogleAccessTokenRequest> httpEntity = new HttpEntity<>(
                new GoogleAccessTokenRequest(decodedCode, clientId, clientSecret, redirectUri, authorizationCode),
                headers
        );
        final GoogleAccessTokenResponse response = restTemplate.exchange(
                accessTokenUrl, HttpMethod.POST, httpEntity, GoogleAccessTokenResponse.class
        ).getBody();
        return Optional.ofNullable(response)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE))
                .accessToken();
    }

    private GoogleAccountProfileResponse requestGoogleAccountProfile(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        final HttpEntity<GoogleAccessTokenRequest> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(profileUrl, HttpMethod.GET, httpEntity, GoogleAccountProfileResponse.class)
                .getBody();
    }
}

