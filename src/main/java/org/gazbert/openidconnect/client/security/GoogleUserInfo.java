package org.gazbert.openidconnect.client.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * Encapsulates Google user info.
 *
 * Example userinfo response payload:
 *
 * <pre>
 * {
 *    "sub": "118412435170935916453",
 *    "name": "Joze Myname",
 *    "given_name": "Joze",
 *    "family_name": "Myname",
 *    "profile": "https://plus.google.com/+JozeMyname",
 *    "picture": "https://lh3.googleusercontent.com/blabla/photo.jpg",
 *    "email": "joze.myname@gmail.com",
 *    "email_verified": true
 * }
 * </pre>
 *
 * @author gazbert
 *
 */
public class GoogleUserInfo {

    private final String id;
    private final String name;
    private final String givenName;
    private final String familyName;
    private final String profile;
    private final String picture;
    private final String email;
    private final boolean emailVerified;

    @JsonCreator
    public GoogleUserInfo(@JsonProperty("sub") String id,
                          @JsonProperty("name") String name,
                          @JsonProperty("given_name") String givenName,
                          @JsonProperty("family_name") String familyName,
                          @JsonProperty("profile") String profile,
                          @JsonProperty("picture") String picture,
                          @JsonProperty("email") String email,
                          @JsonProperty("email_verified") boolean emailVerified) {
        this.id = id;
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.profile = profile;
        this.picture = picture;
        this.email = email;
        this.emailVerified = emailVerified;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getProfile() {
        return profile;
    }

    public String getPicture() {
        return picture;
    }

    public String getEmail() {
        return email;
    }

    public boolean getEmailVerified() {
        return emailVerified;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("givenName", givenName)
                .add("familyName", familyName)
                .add("profile", profile)
                .add("picture", picture)
                .add("email", email)
                .add("emailVerified", emailVerified)
                .toString();
    }
}