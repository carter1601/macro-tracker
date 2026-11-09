package com.macrotracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MacroTotalsTest {

    @Test
    void constructorTest() {
        MacroTotals food = new MacroTotals(100, 50, 50, 50);
        assertEquals(100, food.getCalories(), 0.001);
    }

    @Test
    void plusTest() {
        MacroTotals a = new MacroTotals(100, 50, 50, 50);
        MacroTotals b = new MacroTotals(100, 50, 50, 50);
        MacroTotals sum = a.plus(b);
        assertEquals(200, sum.getCalories(), 0.001);
        assertEquals(100, sum.getProtein(), 0.001);
    }

    @Test
    void minusTest() {
        MacroTotals a = new MacroTotals(100, 50, 50, 50);
        MacroTotals b = new MacroTotals(50, 25, 25, 25);
        MacroTotals difference = a.minus(b);
        assertEquals(50, difference.getCalories(), 0.001);
        assertEquals(25, difference.getProtein(), 0.001);
    }

    @Test
    void timesTest() {
        MacroTotals a = new MacroTotals(100, 50, 50, 50);
        MacroTotals sum = a.times(2);
        assertEquals(200, sum.getCalories(), 0.001);
        assertEquals(100, sum.getProtein(), 0.001);
    }
}