/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.oauth2;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.pamarin.commons.security.DefaultUserDetails;
import com.pamarin.commons.security.HashBasedToken;
import com.pamarin.oauth2.exception.UnauthorizedClientException;
import com.pamarin.oauth2.model.AccessTokenResponse;
import com.pamarin.oauth2.model.AuthorizationRequest;
import com.pamarin.oauth2.model.CodeAccessTokenRequest;
import com.pamarin.oauth2.model.RefreshAccessTokenRequest;
import com.pamarin.oauth2.model.TokenBase;
import com.pamarin.oauth2.service.AccessTokenGenerator;
import com.pamarin.oauth2.service.ClientVerification;
import com.pamarin.commons.security.LoginSession;
import com.pamarin.oauth2.service.TokenVerification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import static com.pamarin.commons.util.DateConverterUtils.convert2LocalDateTime;
import com.pamarin.oauth2.domain.OAuth2AccessToken;
import com.pamarin.oauth2.repository.OAuth2AccessTokenRepo;
import com.pamarin.oauth2.repository.OAuth2RefreshTokenRepo;
import com.pamarin.oauth2.service.RefreshTokenGenerator;
import com.pamarin.oauth2.service.RefreshTokenVerification;
import java.util.Date;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/12
 */
@Service
@Transactional
class AccessTokenGeneratorImpl implements AccessTokenGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(AccessTokenGeneratorImpl.class);

    @Autowired
    private LoginSession loginSession;

    @Autowired
    @Qualifier("authorizationCodeVerification")
    private TokenVerification authorizationCodeVerification;

    @Autowired
    private ClientVerification clientVerification;

    @Autowired
    private OAuth2AccessTokenRepo accessTokenRepo;

    @Autowired
    private OAuth2RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private RefreshTokenGenerator refreshTokenGenerator;

    @Autowired
    private RefreshTokenVerification refreshTokenVerification;

    @Autowired
    private HashBasedToken hashBasedToken;

    private AccessTokenResponse buildAccessTokenResponse(TokenBase base) {
        OAuth2AccessToken accessToken = accessTokenRepo.save(OAuth2AccessToken.builder()
                .userId(base.getUserId())
                .clientId(base.getClientId())
                .build()
        );
        String token = hashBasedToken.hash(
                DefaultUserDetails.builder()
                        .username(accessToken.getId())
                        .password(accessToken.getSecretKey())
                        .build(),
                convert2LocalDateTime(new Date(accessToken.getExpiresAt()))
        );
        base.setId(accessToken.getId());
        return AccessTokenResponse.builder()
                .accessToken(token)
                .expiresIn(accessToken.getExpireMinutes() * 60L)
                .refreshToken(refreshTokenGenerator.generate(base))
                .tokenType("bearer")
                .build();
    }

    @Override
    public AccessTokenResponse generate(AuthorizationRequest req) {
        UserDetails userDetails = loginSession.getUserDetails();
        return buildAccessTokenResponse(TokenBase.builder()
                .clientId(req.getClientId())
                .userId(userDetails.getUsername())
                .build());
    }

    @Override
    public AccessTokenResponse generate(CodeAccessTokenRequest req) {
        clientVerification.verifyClientIdAndClientSecret(req.getClientId(), req.getClientSecret());
        try {
            TokenBase authCode = authorizationCodeVerification.verify(req.getCode());
            return buildAccessTokenResponse(authCode);
        } catch (TokenExpiredException ex) {
            LOG.warn(null, ex);
            throw new UnauthorizedClientException(ex);
        }
    }

    @Override
    public AccessTokenResponse generate(RefreshAccessTokenRequest req) {
        clientVerification.verifyClientIdAndClientSecret(req.getClientId(), req.getClientSecret());
        TokenBase base = refreshTokenVerification.verify(req.getRefreshToken());
        revokeToken(base.getId());
        base.setId(null);
        base.setClientId(req.getClientId());
        return buildAccessTokenResponse(base);
    }

    private void revokeToken(String tokenId) {
        refreshTokenRepo.deleteById(tokenId);
        accessTokenRepo.deleteById(tokenId);
    }
}
