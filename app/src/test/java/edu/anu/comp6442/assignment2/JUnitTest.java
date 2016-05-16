package edu.anu.comp6442.assignment2;

import org.junit.Test;

import parserV2.AppUtils;
import parserV2.CalculatorParser;

import static junit.framework.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class JUnitTest {
    //random test not related to the assignment
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    // testing of hasCorrectFormat method of CalculatorParser class
    @Test
    public void testHasCorrectFormat() throws Exception {
        boolean actual_value = CalculatorParser.hasCorrectFormat("2+3");
        boolean expected_value = true;
        assertEquals("Test failed", expected_value, actual_value);
    }

    //similarly testing of the isNumeric method, testing whether the regular expression working properly or not
    @Test
    public void testIsNumeric() throws Exception {
        boolean actual_value = CalculatorParser.isNumeric("12.65");
        boolean expected_value = true;
        assertEquals("Test failed", expected_value, actual_value);
    }

    //testing of the round() method of AppUtils
    @Test
    public void testRound() throws Exception {
        double expected_value= 12.0;
        double actual_value = AppUtils.round(12);
        assertEquals("Test failed", expected_value, actual_value);
    }



}