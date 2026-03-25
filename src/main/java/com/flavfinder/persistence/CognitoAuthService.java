package com.flavfinder.persistence;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

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
    private CognitoIdentityProviderClient cognitoClient;
    private String clientId;
    private String userPoolId;
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
     *
     * @throws UsernameExistsException  If the email is already registered
     * @throws InvalidPasswordException   If the password doesn't meet policy requirements
     * @throws InvalidParameterException   If any required field is invalid or missing
     * @throws TooManyRequestsException  If too many requests are made in a short period
     */
    public void register(String firstName, String email, String password)
            throws UsernameExistsException, InvalidPasswordException,
            InvalidParameterException, TooManyRequestsException
    {
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

    /**
     * Confirms a new user's registration using the
     * verification code sent to their email.
     *
     * @param email the user's email to confirm
     * @param code the verification code sent to the user's email
     *
     * @throws CodeMismatchException If the code that was sent to user doesn't match.
     * @throws ExpiredCodeException If the code expired
     */
    public void confirmSignUp(String email, String code) throws CodeMismatchException, ExpiredCodeException {
        // Build the confirm signup request
        ConfirmSignUpRequest request = ConfirmSignUpRequest.builder()
                .clientId(clientId)
                .username(email)
                .confirmationCode(code)
                .build();

        cognitoClient.confirmSignUp(request);
    }
}
