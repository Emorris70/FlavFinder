package com.flavfinder.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SWAPIDaoTest {

    @Test
    void getPlanet() {
        SWAPIDao swapiDao = new SWAPIDao();
        assertEquals("Tatooine", swapiDao.getPlanet().getName());
    }
}