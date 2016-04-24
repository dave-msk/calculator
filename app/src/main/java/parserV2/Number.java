package parserV2;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by david on 4/25/16.
 */
public enum Number {
    ZERO("0",0),
    ONE("1",1),
    TWO("2",2),
    THREE("3",3),
    FOUR("4",4),
    FIVE("5",5),
    SIX("6",6),
    SEVEN("7",7),
    EIGHT("8",8),
    NINE("9",9);

    public final String symbol;
    public final double value;

    Number(String symbol, double value) {
        this.symbol = symbol;
        this.value = value;
        Maps.symbolMap.put(symbol,this);
    }

    public static Number getNumber(String symbol) {
        if (Maps.symbolMap.containsKey(symbol))
            return Maps.symbolMap.get(symbol);
        else
            return null;
    }

    public static Set<String> getNumberSymbols() {
        return Maps.symbolMap.keySet();
    }

    private static class Maps {
        public static final HashMap<String, Number> symbolMap = new HashMap<>();
    }


}
