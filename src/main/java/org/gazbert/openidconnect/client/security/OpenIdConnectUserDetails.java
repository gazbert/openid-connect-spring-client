package org.gazbert.openidconnect.client.security;

import com.google.common.base.MoreObjects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Encapsulates OpenID Connect user details.
 *
 * @author gazbert
 */
public class OpenIdConnectUserDetails implements UserDetails {

    private static final long serialVersionUID = -4153740588312462691L;

    private String userId;
    private String email;
    private OAuth2AccessToken accessToken;

    OpenIdConnectUserDetails(Map<String, String> userInfo, OAuth2AccessToken accessToken) {
        this.userId = userInfo.get("sub");
        this.email = userInfo.get("email");
        this.accessToken = accessToken;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null; // not relevant for OpenID Connect
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public OAuth2AccessToken getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", userId)
                .add("email", email)
                .add("accessToken", accessToken)
                .toString();
    }
}
