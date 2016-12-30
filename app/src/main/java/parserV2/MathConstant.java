/* Copyright (C) 2016 Siu-Kei Muk, Snigdha Brahma
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */

package parserV2;

import java.util.HashMap;
import java.util.Set;

/**
 * COMP6442 - Software Construction
 * Assignment 2 - Calculator
 * Authors: Siu Kei, Muk & Snigdha Brahma
 * This enum class defines the math constants to be used in the calculator.
 */
public enum MathConstant {
    PI("PI",Math.PI),
    E("e",Math.E);

    public final String symbol;
    public final double value;

    MathConstant(String symbol, double value) {
        this.symbol = symbol;
        this.value = value;
        Maps.symbolMap.put(symbol,this);
    }

    public static MathConstant getMathConstant(String symbol) {
        if (Maps.symbolMap.containsKey(symbol))
            return Maps.symbolMap.get(symbol);
        else
            return null;
    }

    public static Set<String> getConstSymbols() {
        return Maps.symbolMap.keySet();
    }

    private static class Maps {
        public static final HashMap<String, MathConstant> symbolMap = new HashMap<>();
    }
}
