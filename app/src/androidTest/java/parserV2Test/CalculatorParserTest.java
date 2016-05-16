package parserV2Test;

import parserV2.CalculatorParser;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Snigdha on 5/16/2016.
 */
public class CalculatorParserTest {

    public void testParse() throws Exception {

    }

    //test fail
    public void testHasCorrectFormat() throws Exception {
       boolean actual_value = CalculatorParser.hasCorrectFormat("2+3+4");
        boolean expected_value = true;
        assertEquals("Test failed",expected_value,actual_value);
    }

    public void testIsNumeric() throws Exception {

    }
}