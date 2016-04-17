package calculatorParser.calculatorParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import calculatorParser.Operator;

public enum Element {
	OpenBlanket('('),
	CloseBlanket(')'),
	Operators(Operator.getOpSymbols()),
	Numbers(new char[] {'0','1','2','3','4','5','6','7','8','9'}),
	Negate('n'),
	Dot('.'),
	Null((char)0);
	
	private Set<Character> chars;
	Element(char c) {
		this.chars = new HashSet<>();
		this.chars.add(c);
		Maps.symbolMap.put(c, this);
	}
	Element(Set<Character> chars) {
		this.chars = chars;
		for (Character c : chars)
			Maps.symbolMap.put(c, this);
	}
	Element(char[] chars) {
		this.chars = new HashSet<>();
		for (int i = 0; i < chars.length; i++) {
			this.chars.add(chars[i]);
			Maps.symbolMap.put(chars[i], this);
		}
	}
	
	public boolean hasSymbol(char c) {
		return chars.contains(c);
	}
	
	public static calculatorParser.Element getElement(char c) {
		if (Maps.symbolMap.keySet().contains(c))
			return Maps.symbolMap.get(c);
		else
			return null;
	}
	
	private static class Maps {
		static final HashMap<Character, calculatorParser.Element> symbolMap = new HashMap<>();
	}
}
