/* Copyright (C) 2016 Siu-Kei Muk, Snigdha Brahma
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */

package edu.anu.comp6442.assignment2;

import org.junit.Test;

import parserV2.AppUtils;
import parserV2.CalculatorParser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * COMP6442 - Software Construction
 * Assignment 2 - Calculator
 * Authors: Siu Kei, Muk & Snigdha Brahma
 *
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
        boolean actual_value1 = CalculatorParser.hasCorrectFormat("(+)");
        boolean expected_value1 = false;
        assertEquals("Test failed", expected_value1, actual_value1);
    }

    //similarly testing of the isNumeric method, testing whether the regular expression working properly or not
    @Test
    public void testIsNumeric() throws Exception {
        boolean actual_value = CalculatorParser.isNumeric("12.65");
        boolean expected_value = true;
        assertEquals("Test failed", expected_value, actual_value);
        boolean actual_value1 = CalculatorParser.isNumeric("1");
        boolean expected_value1 = true;
        assertEquals("Test failed", expected_value1, actual_value1);
    }

    //testing of the round() method of AppUtils
    @Test
    public void testRound() throws Exception {
        assertTrue(Double.toString(AppUtils.round(0.0 / 0.0)).equals("NaN"));
        assertTrue(Double.toString(AppUtils.round(1.0/0.0)).equals("Infinity"));
        assertTrue(Double.toString(AppUtils.round(0.0)).equals("0.0"));
        assertTrue(Double.toString(AppUtils.round(0.99999999999)).equals("1.0"));
    }
}
