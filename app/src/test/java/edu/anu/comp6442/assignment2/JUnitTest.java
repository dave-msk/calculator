package edu.anu.comp6442.assignment2;

import org.junit.Test;

import parserV2.CalculatorParser;

import static junit.framework.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class JUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testHasCorrectFormat() throws Exception {
        boolean actual_value = CalculatorParser.hasCorrectFormat("2+3");
        boolean expected_value = true;
        assertEquals("Test failed", expected_value, actual_value);
    }

    public void testIsNumeric() throws Exception {
        boolean actual_value = CalculatorParser.isNumeric("12.65");
        boolean expected_value = true;
        assertEquals("Test failed", expected_value, actual_value);
    }



}