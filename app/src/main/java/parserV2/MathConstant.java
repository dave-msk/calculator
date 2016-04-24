package parserV2;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by david on 4/25/16.
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
