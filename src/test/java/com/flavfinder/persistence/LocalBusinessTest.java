package com.flavfinder.persistence;

import com.flavfinder.APIdentity.BusinessItem;
import com.flavfinder.APIdentity.LocalBusinessResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Local Business API.
 * @author EmileM
 */
@Disabled
class LocalBusinessTest {
    private static final Logger log = LogManager.getLogger(LocalBusinessTest.class);
    private Resources resource;
    private LocalBusinessResponse response;
    double lat = 43.0731;
    double lon = -89.4012;
    private String query = "pizza";

    @BeforeEach
    void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/config.properties"));

        resource = new Resources(properties);
        // Ensures I make one request
        response = resource.callLocalBusiness(lat, lon, query);
    }

    /**
     * Test - Gets the first result from the Local Business API.
     * Also verifies the fields I need are present.
     */
    @Test
    void callLocalBusiness() {
        assertNotNull(response);

        assertEquals("OK", response.getStatus());
        assertNotNull(response.getData());
        assertFalse(response.getData().isEmpty());

        // Verify first result has the fields we need
        BusinessItem first = response.getData().get(0);
        log.info("First result: {}", first.getName());
        assertNotNull(first.getPlaceId());
        assertNotNull(first.getName());
        assertNotNull(first.getPhotosSample());
        assertFalse(first.getPhotosSample().isEmpty());
    }
}