package edu.anu.comp6442.assignment2;

import org.junit.Test;

import parserV2.AppUtils;
import parserV2.CalculatorParser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

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
        boolean actual_value = CalculatorParser.hasCorrectFormat("-(-(-(-2)+sin(-3.25^4)^2))/5*log(2)+4/tan(-(-2))*3.427-6/(20+4)");
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
        assertTrue(Double.toString(AppUtils.round(0.99999999999)).equals("1.0"));
    }
}