package com.flavfinder.persistence;

import com.flavfinder.APIdentity.Address;
import com.flavfinder.APIdentity.Position;
import com.flavfinder.APIdentity.ResultsItem;
import com.flavfinder.APIdentity.TomTomResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class represents HTTP request to TomTom
 * URL end point.
 */
class TomApiTest {
    Resources dao;
    TomTomResponse response;

    @BeforeEach
    void setUp() {
        dao = new Resources();
        // Ensures I make one request
        response = dao.callTomTom("4 north 2nd street san jose");
    }

    /**
     * Test - Gets the country subdivision name.
     */
    @Test
    void getCountryName() {
        // Verify response not null
        assertNotNull(response);

        List<ResultsItem> items = response.getResults();

        for (ResultsItem item : items) {
            Address address = item.getAddress();
            assertEquals("California", address.getCountrySubdivisionName());
        }
    }

    /**
     * Test - Gets the longitude and latitude from the response.
     */
    @Test
    void getGeometry() {
        // Ensure not null
        assertNotNull(response);

        // Get the result
        List<ResultsItem> items = response.getResults();

        // Get latitude and longitude from results
        for (ResultsItem item : items) {
            Position p = item.getPosition();
            double lat = (double) p.getLat();
            double lon = (double) p.getLon();
            assertEquals(37.3376732, lat);
            assertEquals(-121.8898157, lon);
        }
    }
}