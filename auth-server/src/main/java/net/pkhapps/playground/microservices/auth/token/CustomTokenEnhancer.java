package net.pkhapps.playground.microservices.auth.token;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.HashMap;

/**
 * This token enhancer adds additional information to the access token that will be sent to clients.
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        var additionalInfo = new HashMap<String, Object>();
        // TODO In a real-world app, you would look up the information form the user database
        additionalInfo.put("email", authentication.getName() + "@foo.bar");
        additionalInfo.put("avatar", "https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50");
        var details = authentication.getUserAuthentication().getDetails();
        if (details instanceof WebAuthenticationDetails) {
            additionalInfo.put("session_id", ((WebAuthenticationDetails) details).getSessionId());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
