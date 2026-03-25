package com.flavfinder.persistence;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import java.util.Properties;

/**
 * @author EmileM
 */
public class CognitoClientUtil implements PropertiesLoader {
    Properties properties;
    // Single shared instance
    private static CognitoIdentityProviderClient cognitoClient;

    public CognitoClientUtil() {

    }
}
