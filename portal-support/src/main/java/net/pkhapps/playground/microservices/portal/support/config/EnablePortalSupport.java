package net.pkhapps.playground.microservices.portal.support.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add this to the Spring configuration of all microfrontend applications and add the necessary configuration
 * properties.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(PortalSecurityConfig.class)
public @interface EnablePortalSupport {
}
