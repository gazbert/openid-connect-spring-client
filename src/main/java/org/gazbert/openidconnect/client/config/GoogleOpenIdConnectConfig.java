package org.gazbert.openidconnect.client.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * OpenID Connect config for authenticating using the Google Identity Platform.
 *
 * Loaded from the application.properties.
 *
 * @author gazbert
 */
@Configuration
@EnableOAuth2Client
public class GoogleOpenIdConnectConfig {

    @Value("${google.openidconnect.client_id}")
    private String clientId;

    @Value("${google.openidconnect.client_secret}")
    private String clientSecret;

    @Value("${google.openidconnect.auth_uri}")
    private String authorizationUri;

    @Value("${google.openidconnect.token_uri}")
    private String tokenUri;

    @Value("${google.openidconnect.redirect_uri}")
    private String redirectUri;

    @Value("${google.openidconnect.optional.scopes}")
    private String optionalScopes;

    @Bean
    public OAuth2RestTemplate getGoogleOpenIdConnectRestTemplate(@Qualifier("oauth2ClientContext")
                                                                             OAuth2ClientContext clientContext) {
        return new OAuth2RestTemplate(createGoogleOpenIdConnectConfig(), clientContext);
    }

    @Bean
    public OAuth2ProtectedResourceDetails createGoogleOpenIdConnectConfig() {
        final AuthorizationCodeResourceDetails resourceDetails = new AuthorizationCodeResourceDetails();
        resourceDetails.setClientAuthenticationScheme(AuthenticationScheme.form); // include client credentials in POST Content
        resourceDetails.setClientId(clientId);
        resourceDetails.setClientSecret(clientSecret);
        resourceDetails.setUserAuthorizationUri(authorizationUri);
        resourceDetails.setAccessTokenUri(tokenUri);

        final List<String> scopes = new ArrayList<>();
        scopes.add("openid"); // always need this
        scopes.addAll(Arrays.asList(optionalScopes.split(",")));
        resourceDetails.setScope(scopes);

        resourceDetails.setPreEstablishedRedirectUri(redirectUri);
        resourceDetails.setUseCurrentUri(false);
        return resourceDetails;
    }
}
