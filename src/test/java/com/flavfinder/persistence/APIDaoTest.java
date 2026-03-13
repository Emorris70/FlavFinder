package com.flavfinder.persistence;

import com.flavfinder.APIdentity.TomTomResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class APIDaoTest {

    @Test
    void demo() {
        APIDao dao = new APIDao();
        TomTomResponse response = dao.callTomTom("4 north 2nd street san jose");
        assertNotNull(response);
    }
}