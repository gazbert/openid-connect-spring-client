/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Romain Fromi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.gazbert.openidconnect.client;

import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;

/**
 * Bare bones integration test for testing OpenID Connect client behaviour.
 *
 * Originated from:
 * https://github.com/fromi/spring-google-openidconnect/blob/master/src/test/java/com/github/fromi/openidconnect/SecurityIT.java
 *
 * @author gazbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
public class OpenIdConnectClientApplicationIT {

    @Value("${server.port:8080}")
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void whenUserAccessesUnsecuredRootPathThenExpectNoRedirection() {
        given().redirects().follow(false).when().get("/").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void whenUserAccessesUnknownPathThenExpectNoRedirection() {
        given().redirects().follow(false).when().get("/not-exist").then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void whenUserAccessesSecuredHomepageThenExpectRedirectToLoginPage() {
        given().redirects().follow(false).when().get("/home").then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .header("Location", endsWith("/login"));
    }

    @Test
    public void whenLoginPageIsAccessedThenExpectRedirectToGoogle() {
        given().redirects().follow(false).when().get("/login").then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .header("Location", startsWith("https://accounts.google.com/o/oauth2/auth"));
    }
}