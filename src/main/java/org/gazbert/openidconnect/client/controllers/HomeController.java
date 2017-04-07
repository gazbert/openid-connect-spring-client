package org.gazbert.openidconnect.client.controllers;

import org.gazbert.openidconnect.client.security.GoogleUserInfo;
import org.gazbert.openidconnect.client.security.OpenIdConnectUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * Homepage is protected.
 * Accessing this page will trigger the OpenID Connect login with Google Identity Platform.
 * It will also fetch additional user data back from userinfo endpoint.
 *
 * @author gazbert
 */
@Controller
@Configuration
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${google.openidconnect.userinfo_uri}")
    private String userInfoUri;

    @RequestMapping("/home")
    @ResponseBody
    public final String homepage() {

        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Authenticated Username: " + username);

        final OpenIdConnectUserDetails userDetails =
                (OpenIdConnectUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Authentication Principal: " + userDetails);

        final GoogleUserInfo googleUserInfo = getUserInfo(userDetails);
        log.info("UserInfo endpoint response: " + googleUserInfo);

        return "<h2>Secured Homepage</h2>User Id: " + userDetails.getUsername()
                + (userDetails.getEmail() != null ? "<p>Email: " + userDetails.getEmail() : "");
    }

    private GoogleUserInfo getUserInfo(OpenIdConnectUserDetails userDetails) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + userDetails.getAccessToken());
        final HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
        final ResponseEntity<GoogleUserInfo> userInfoResponseEntity =
                restTemplate.exchange(userInfoUri, HttpMethod.GET, httpEntity, GoogleUserInfo.class);
        return userInfoResponseEntity.getBody();
    }
}
