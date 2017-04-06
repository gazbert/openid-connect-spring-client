package org.gazbert.openidconnect.client.config;

import org.gazbert.openidconnect.client.security.OpenIdConnectAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import static org.springframework.http.HttpMethod.GET;

/**
 * Web Security config for the app.
 *
 * @author gazbert
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    public WebSecurityConfig(OAuth2RestTemplate oAuth2RestTemplate) {
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public OpenIdConnectAuthenticationFilter createOpenIdConnectFilter() {
        final OpenIdConnectAuthenticationFilter openIdConnectFilter = new OpenIdConnectAuthenticationFilter("/login");
        openIdConnectFilter.setRestTemplate(oAuth2RestTemplate);
        return openIdConnectFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .addFilterAfter(new OAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(createOpenIdConnectFilter(), OAuth2ClientContextFilter.class)
                .httpBasic().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .and()
                .authorizeRequests()
                .antMatchers(GET, "/").permitAll()
                .antMatchers(GET, "/home").authenticated();
                // .antMatchers("/","/index*").permitAll()
                // .anyRequest().authenticated();
    }
}