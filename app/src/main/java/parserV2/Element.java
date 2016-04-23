package parserV2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public enum Element {
	OpenBlanket("("),
	CloseBlanket(")"),
	UnaryOperators(UnaryOperator.getOpSymbols()),
	BinaryOperators(BinaryOperator.getOpSymbols()),
	Numbers(new String[] {"0","1","2","3","4","5","6","7","8","9"}),
	Negate("n"),
	Dot("."),
    Start("@Start"),
	Null("");
	
	private Set<String> symbols;
	Element(String s) {
		this.symbols = new HashSet<>();
		this.symbols.add(s);
		Maps.symbolMap.put(s, this);
	}
	Element(Set<String> symbols) {
		this.symbols = symbols;
		for (String s : symbols)
			Maps.symbolMap.put(s, this);
	}
	Element(String[] symbols) {
		this.symbols = new HashSet<>();
		for (int i = 0; i < symbols.length; i++) {
			this.symbols.add(symbols[i]);
			Maps.symbolMap.put(symbols[i], this);
		}
	}
	
	public boolean hasSymbol(String s) {
		return symbols.contains(s);
	}
	
	public static Element getElement(String s) {
		if (Maps.symbolMap.keySet().contains(s))
			return Maps.symbolMap.get(s);
		else
			return null;
	}

	public static Element getElement(char c) {
        return getElement(Character.toString(c));
    }
	
	private static class Maps {
		static final HashMap<String,Element> symbolMap = new HashMap<>();
	}
}

