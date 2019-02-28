package net.pkhapps.playground.microservices.portal.support.config;

import net.pkhapps.playground.microservices.portal.support.CurrentUser;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoDefaultConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.OAuth2ClientConfiguration;
import org.springframework.security.oauth2.provider.token.TokenStore;

@EnableWebSecurity
@Configuration
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableConfigurationProperties(OAuth2SsoProperties.class)
@Import({OAuth2ClientConfiguration.class, ResourceServerTokenServicesConfiguration.class})
class PortalSecurityConfig extends OAuth2SsoDefaultConfiguration {

    /*
     * We have to extend OAuth2SsoDefaultConfiguration and do some stuff manually. Simply using @EnableOauth2Sso will
     * not work since this config is imported and not component scanned. This in turn has to do with an issue in
     * Spring Boot: https://github.com/spring-projects/spring-boot/issues/5093
     */

    PortalSecurityConfig(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Bean
    public CurrentUser currentUser(TokenStore tokenStore) {
        return new CurrentUser(tokenStore);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.requestMatchers().antMatchers("/**")
                .and()
                .authorizeRequests().antMatchers("/login**", "/error").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .headers().frameOptions().disable(); // TODO Would be better to use 'allow-from [portal-server-uri]'
    }
}
