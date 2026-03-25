package com.flavfinder.persistence;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import java.util.Properties;

/**
 * Handles all AWS Cognito operations against the specific
 * User Pool. Responsible for registering, confirming,
 * and authenticating users.
 *
 * @author EmileM
 */
public class CognitoAuthService {
    private final CognitoIdentityProviderClient cognitoClient;
    private final String clientId;
    private final String userPoolId;
    private Properties properties;

    public CognitoAuthService() {}

    public CognitoAuthService(Properties properties, CognitoIdentityProviderClient client) {

        this.properties = properties;
//        IF I create a new instance of the CognitoClientUtil class there might be loss.
        // Since I already create one instance in app start.
        this.clientId = properties.getProperty("aws.cognito.clientId");
        this.userPoolId = properties.getProperty("aws.cognito.userPoolId");
    }
}
