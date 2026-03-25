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

    /**
     * Instantiates a new CognitoClientUtil
     */
    public CognitoClientUtil() {

    }

    /**
     * Instantiates a new CognitoClientUtil. And initializes
     * the properties variable.
     *
     * @param properties
     */
    public CognitoClientUtil(Properties properties) {
        this();
        this.properties = properties;
    }
}
