/* Copyright (C) 2016 Siu-Kei Muk, Snigdha Brahma
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */

package parserV2;

import java.math.BigDecimal;

/**
 * COMP6442 - Software Construction
 * Assignment 2 - Calculator
 * Authors: Siu Kei, Muk & Snigdha Brahma
 * This is a helper class that consists of some helper methods like rounding.
 */
public final class AppUtils {

    public static final int MAXDIGIT = 14;
    public static final int MAXDECIMAL = 9;

    private AppUtils(){};
   //method for rounding the numerical value
    public static double round(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d))
            return d;
        int digits = leadingDigits(d);
        int roundToDecimal = Math.min(MAXDIGIT-digits,MAXDECIMAL);
        double rounded =  BigDecimal.valueOf(d).setScale(roundToDecimal,BigDecimal.ROUND_HALF_UP).doubleValue();
        return rounded;
    }


    private static int leadingDigits(double d) {
        int digits = 0;
        while (Math.abs(d) >= 1) {
            digits++;
            d /= 10.0;
        }
        return digits;
    }
}
