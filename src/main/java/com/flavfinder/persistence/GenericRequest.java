package com.flavfinder.persistence;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

abstract class GenericRequest {
    private static final Logger log = LogManager.getLogger(GenericRequest.class);

    /**
     * Performs a synchronous GET request to a REST resource and maps the JSON response
     * to a specified Java type(aka POJO class)
     *
     * This method dynamically builds the target URL using the base URL and path,
     * appends query parameters, sets required headers, and handles connection cleanup
     * automatically.
     *
     * @param baseUrl The base URL of the API service.
     * @param action users search/input criteria.
     * @param queryParams Map of key-value pairs to be appended as query strings.
     * @param headers Map of HTTP headers required by the API.
     * @param responseType The Class of the POJO the response should be mapped into.
     * @return An instance of T containing the mapped API data.
     * @param <T> The type of the response entity.
     */
    public <T> T executeGetRequest(String baseUrl, String action, Map<String, Object> queryParams,
                                   Map<String, String> headers, Class<T> responseType)
    {
        Client client = ClientBuilder.newClient();
        try  {
            // The url to target
            // https://example.api/
            WebTarget target = client.target(baseUrl);

            // 1. Add Path (if any)
            // represents the users entered value.
            // e.g. if user enters 1234 mc street translate to
            // 1234%20mc%20street...
            if (action != null) {
                target = target.path(action);
            }

            // 2. Add Query Parameters dynamically
            if (queryParams != null) {

                for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    target = target.queryParam(entry.getKey(), entry.getValue());
                }
            }

            // 3. Add Headers and Execute
            // The response im expecting back i.e. JSON
            Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON);

            if (headers != null) {

                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.header(entry.getKey(), entry.getValue());
                }
            }

            log.debug("Target URL: " + target.getUri());

            // Maps the JSON TEXT e.g. the targeting API class
            // simulates: mapper.readValue(response, targetClass.class)
            return builder.get(responseType);
        } catch (ProcessingException e) {
            log.error("JSON Structure didn't match the POJO" + e.getMessage(), e);
            throw e;
        } catch (WebApplicationException e) {
            log.error("Server returned an error status" + e.getMessage(), e);
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
}
