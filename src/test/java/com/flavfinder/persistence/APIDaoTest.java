package com.flavfinder.persistence;

import com.flavfinder.APIdentity.Address;
import com.flavfinder.APIdentity.ResultsItem;
import com.flavfinder.APIdentity.TomTomResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class APIDaoTest {

    /**
     * Test - Gets the countries name
     */
    @Test
    void getCountryName() {
        // Send a request
        APIDao dao = new APIDao();
        TomTomResponse response = dao.callTomTom("4 north 2nd street san jose");

        // Verify response not null
        assertNotNull(response);

        List<ResultsItem> items = response.getResults();

        for (ResultsItem item : items) {
            Address address = item.getAddress();
            assertEquals("California", address.getCountrySubdivisionName());
        }
    }
}