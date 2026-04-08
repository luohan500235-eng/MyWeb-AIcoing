package com.lh.blog.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "blog.jwt")
public class JwtProperties {

    private String secret = "ReplaceThisSecretWithAtLeast32Characters123456";
    private long expiration = 604800000L;
}