package parserV2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * COMP6442 - Software Construction
 * Assignment 2 - Calculator
 * Authors: Siu Kei, Muk & Snigdha Brahma
 *
 * This enum class defines the elements in the expression.
 * The elements would be used to check the eligibility of insertion and deletion.
 */

public enum Element {
	OpenBlanket("("),
	CloseBlanket(")"),
	UnaryOperators(UnaryOperator.getOpSymbols()),
	BinaryOperators(BinaryOperator.getOpSymbols()),
	Numbers(Number.getNumberSymbols()),
	MathConst(MathConstant.getConstSymbols()),
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

