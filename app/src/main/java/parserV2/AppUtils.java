package parserV2;

import java.math.BigDecimal;

/**
 * Created by david on 5/5/16.
 */
public class AppUtils {

    public static final int MAXDIGIT = 14;
    public static final int MAXDECIMAL = 9;

    public static double round(double d) {
        int digits = leadingDigits(d);
        int roundToDecimal = Math.min(MAXDIGIT-digits,MAXDECIMAL);
        double rounded =  BigDecimal.valueOf(d).setScale(roundToDecimal,BigDecimal.ROUND_HALF_UP).doubleValue();
        if (Math.abs(rounded) > Math.abs(d))
            return rounded;
        else if (Math.abs(rounded - d)*Math.pow(10, roundToDecimal) < .1)
            return rounded;
        return d;
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