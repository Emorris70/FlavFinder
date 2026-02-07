package com.flavfinder.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddTest {
    Add add;

    @BeforeEach
    void setUp() {
        add = new Add();
    }

    @Test
    void testAddSum() {
        assertEquals(4, add.addTwoSum(2, 2));
    }
}