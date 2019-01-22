package net.pkhapps.playground.microservices.directory.client;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO Document me!
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ServiceDirectoryClientConfig.class)
public @interface EnableServiceDirectoryClient {
}
