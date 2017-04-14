package org.gazbert.openidconnect.client.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static java.util.Optional.empty;

/**
 * Custom authentication filter for using OpenID Connect.
 *
 * @author gazbert
 */
public class OpenIdConnectAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private OAuth2RestOperations restTemplate;

    public OpenIdConnectAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(new NoOpAuthenticationManager());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        OAuth2AccessToken accessToken;

        try {
            accessToken = restTemplate.getAccessToken();
            log.info("AccessToken: value: " + accessToken.getValue());
            log.info("AccessToken: additionalInfo: " + accessToken.getAdditionalInformation());
            log.info("AccessToken: tokenType: " + accessToken.getTokenType());
            log.info("AccessToken: expiration: " + accessToken.getExpiration());
            log.info("AccessToken: expiresIn: " + accessToken.getExpiresIn());

        } catch (OAuth2Exception e) {
            throw new BadCredentialsException("Could not obtain Access Token", e);
        }

        try {
            final String idToken = accessToken.getAdditionalInformation().get("id_token").toString();
            log.info("Encoded id_token from accessToken.additionalInformation: " + idToken);

            final Jwt tokenDecoded = JwtHelper.decode(idToken);
            log.info("Decoded JWT id_token: " + tokenDecoded);
            log.info("Decoded JWT id_token -> claims: " + tokenDecoded.getClaims());

            final Map authInfo = new ObjectMapper().readValue(tokenDecoded.getClaims(), Map.class);

            @SuppressWarnings("unchecked")
            final OpenIdConnectUserDetails userDetails = new OpenIdConnectUserDetails(authInfo, accessToken);
            log.info("OpenIdConnectUserDetails -> userId: " + userDetails.getUsername());

            return new PreAuthenticatedAuthenticationToken(userDetails, empty(), userDetails.getAuthorities());

        } catch (InvalidTokenException e) {
            throw new BadCredentialsException("Could not obtain user details from Access Token", e);
        }
    }

    public void setRestTemplate(OAuth2RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static class NoOpAuthenticationManager implements AuthenticationManager {
        @Override
        public Authentication authenticate(Authentication authentication) {
            throw new UnsupportedOperationException("No authentication should be done with this AuthenticationManager");
        }
    }
}
