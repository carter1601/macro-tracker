package com.macrotracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserProfileTest {

    @Test
    void constructorTest() {
        UserProfile user = new UserProfile(1, "joe", 25, "male", 150, 150);
        assertEquals("joe", user.getName());
    }
}