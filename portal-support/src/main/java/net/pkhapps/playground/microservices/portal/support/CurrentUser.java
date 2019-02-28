package net.pkhapps.playground.microservices.portal.support;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class CurrentUser {

    private final TokenStore tokenStore;

    public CurrentUser(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public OAuth2Authentication getAuthentication() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            return (OAuth2Authentication) authentication;
        } else {
            throw new IllegalStateException("No OAuth2Authentication bound to current thread");
        }
    }

    public String getUserName() {
        return getAuthentication().getName();
    }

    public Optional<URI> getAvatar() {
        return Optional.ofNullable(getAdditionalInformation().get("avatar")).map(Object::toString).map(URI::create);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(getAdditionalInformation().get("email")).map(Object::toString);
    }

    private Map<String, Object> getAdditionalInformation() {
        var details = getAuthentication().getDetails();
        if (details instanceof OAuth2AuthenticationDetails) {
            var accessToken = tokenStore.readAccessToken(((OAuth2AuthenticationDetails) details).getTokenValue());
            return accessToken.getAdditionalInformation();
        } else {
            return Collections.emptyMap();
        }
    }
}
