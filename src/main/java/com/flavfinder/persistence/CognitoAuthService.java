package com.flavfinder.persistence;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;

import javax.xml.stream.events.Attribute;
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

    /**
     * Instantiates a new CognitoAuthService.
     */
    public CognitoAuthService() {}

    /**
     * Instantiates a new CognitoAuthService. lastly,
     * initializes the appropriate variables.
     *
     * @param properties the read properties file context.
     * @param client the single shared cognito client.
     */
    public CognitoAuthService(Properties properties, CognitoIdentityProviderClient client) {
        this.properties = properties;
        this.cognitoClient = client;
        this.clientId = properties.getProperty("aws.cognito.clientId");
        this.userPoolId = properties.getProperty("aws.cognito.userPoolId");
    }

    /**
     * Registers a new user in the Cognito User Pool.
     *
     * @param firstName the user's first name.
     * @param email the user's email.
     * @param password  the user's password
     */
    public void register(String firstName, String email, String password) {
        // Attributes to store in cognito
        AttributeType firstNameAttr = AttributeType.builder()
                .name("name")
                .value(firstName)
                .build();

        AttributeType emailAttr = AttributeType.builder()
                .name("email")
                .value(email)
                .build();

        // Build the signup request
        SignUpRequest request = SignUpRequest.builder()
                .clientId(clientId)
                .username(email)
                .password(password)
                .userAttributes(firstNameAttr, emailAttr)
                .build();

        // sends the request to cognito
        cognitoClient.signUp(request);
    }
}
