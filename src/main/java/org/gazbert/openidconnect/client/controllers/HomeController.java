package org.gazbert.openidconnect.client.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Homepage is protected.
 * Accessing this page will trigger the OpenID Connect login with Google Identity Platform.
 *
 * @author gazbert
 */
@Controller
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/home")
    @ResponseBody
    public final String homepage() {

        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Authenticated Username: " + username);

        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Authentication Principal: " + principal);

        return "<h2>Secured Homepage</h2>Welcome: " + username;
    }
}
