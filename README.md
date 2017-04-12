# OpenID Connect Spring Client

[![Build Status](https://travis-ci.org/gazbert/openid-connect-spring-client.svg?branch=master)](https://travis-ci.org/gazbert/openid-connect-spring-client)

A simple demo of an [OpenId Connect](http://openid.net/connect/faq/) client using 
[Spring Security](http://docs.spring.io/spring-security/site/docs/4.2.2.RELEASE/reference/htmlsingle) and 
[Spring OAuth2](https://projects.spring.io/spring-security-oauth/docs/oauth2.html) to authenticate with the 
[Google Identity Platform](https://developers.google.com/identity/protocols/OpenIDConnect).

## User Guide

### Google Credentials

1. Instructions assume you already have a [Google account](https://accounts.google.com/).
1. Create a new project at [Google Developer Console](https://console.developers.google.com/).
1. Click on 'Credentials' on left menu.
1. Click on 'OAuth consent screen' tab,
1. Add 'Product name shown to users' e.g. MyThingyApp. Save.
1. Click 'Create credentials' and select 'OAuth client ID'.
1. Credentials: Create Client ID: Select 'Web Application'.
1. Credentials: Create Client ID: Set 'Authorised redirect URIs': http://localhost:8080/login
1. Click 'Create'. This will create your OAuth2 clientId and clientSecret.

### Demo App 

1. Rename `application.properties.template` to `application.properties`.
1. Add your clientId and clientSecret to the `application.properties` file.
1. Update `google.openidconnect.optional.scopes` in `application.properties` as required.
1. Build the app: `mvn clean install`
1. Start the app: `mvn spring-boot:run`
1. Open a browser and go to: `http://localhost:8080`

Once you've authenticated with Google and authorized the app, you should get redirected to the Homepage:

```
Secured Homepage

UserId: ALongNumber
Email: <username>@gmail.com
```

It's worth taking a look at the browser network tab to see the OpenID Connect interactions. 
The app console log dumps out some useful stuff too, e.g. token details and user info.

## Testing
A bare-bones [integration test](./src/test/java/org/gazbert/openidconnect/client/OpenIdConnectClientApplicationIT.java) 
is included and can be run as part of the build. To do this:
 
1. Start the app: `mvn spring-boot:run`
1. Run the test: `mvn verify -Dskip.integration.tests=false`

## Credits
This app was inspired by the excellent tutorial written by [eugenp](https://github.com/eugenp/tutorials/tree/master/spring-security-openid).
The integration test originated from code written by [fromi](https://github.com/fromi/spring-google-openidconnect).

## References

1. A good [introduction](https://connect2id.com/learn/openid-connect) to OpenID Connect.
1. OpenID Connect [Core Specification](http://openid.net/specs/openid-connect-core-1_0.html).
1. [RFC7519](https://tools.ietf.org/html/rfc7519) - the Java Web Token (JWT) spec.
1. A useful JWT [Debugger](https://jwt.io/).