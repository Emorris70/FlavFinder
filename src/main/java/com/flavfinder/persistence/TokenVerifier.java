package com.flavfinder.persistence;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

import java.net.URL;
import java.util.Date;
import java.util.Properties;

/**
 * Verifies and extracts claims from the Cognito
 * issued JWT token. Ensures the token is valid,
 * signed, and not expired.
 *
 * @author EmileM
 */
public class TokenVerifier {
    private final String jwksUrl;

    /**
     * Instantiates a new TokenVerifier.
     */
    public TokenVerifier() {

    }

    /**
     * Instantiates a new TokenVerifier.
     * Builds the JWKS url from the User Pool region and ID.
     */
    public TokenVerifier(Properties properties) {
        String region = properties.getProperty("aws.cognito.region");
        String userPoolId = properties.getProperty("aws.cognito.userPoolId");

        // This is the URL where Cognito publishes its public keys
        this.jwksUrl = "https://cognito-idp." + region + ".amazonaws.com/"
                + userPoolId + "/.well-known/jwks.json";
    }
}
