package calculatorParser.calculatorParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import calculatorParser.*;
import calculatorParser.Element;

public enum Rule {
	OpenBlanket(calculatorParser.Element.OpenBlanket, new calculatorParser.Element[] {calculatorParser.Element.OpenBlanket, calculatorParser.Element.Numbers, calculatorParser.Element.Dot, calculatorParser.Element.Negate}),
	CloseBlanket(calculatorParser.Element.CloseBlanket, new calculatorParser.Element[] {calculatorParser.Element.CloseBlanket, calculatorParser.Element.Operators}),
	Operators(calculatorParser.Element.Operators, new calculatorParser.Element[] {calculatorParser.Element.OpenBlanket, calculatorParser.Element.Numbers, calculatorParser.Element.Dot}),
	Numbers(calculatorParser.Element.Numbers, new calculatorParser.Element[] {calculatorParser.Element.CloseBlanket, calculatorParser.Element.Operators, calculatorParser.Element.Dot, calculatorParser.Element.Numbers}),
	Negate(calculatorParser.Element.Negate, new calculatorParser.Element[] {calculatorParser.Element.OpenBlanket, calculatorParser.Element.Dot, calculatorParser.Element.Numbers}),
	Dot(calculatorParser.Element.Dot, calculatorParser.Element.Numbers),
	Null(calculatorParser.Element.Null, new calculatorParser.Element[]{calculatorParser.Element.OpenBlanket, calculatorParser.Element.Numbers, calculatorParser.Element.Negate, calculatorParser.Element.Dot});

	private Set<calculatorParser.Element> rule = new HashSet<>();
	private calculatorParser.Element element;
	Rule(calculatorParser.Element thisElement, calculatorParser.Element[] elements) {
		rule.addAll(Arrays.asList(elements));
		this.element = thisElement;
		Rules.ruleMap.put(thisElement, this);
	}
	Rule(calculatorParser.Element thisElement, calculatorParser.Element element){
		rule.add(element);
		this.element = thisElement;
		Rules.ruleMap.put(thisElement, this);
	}
	public static calculatorParser.Rule getRule(char c) {
		return Rules.ruleMap.get(calculatorParser.Element.getElement(c));
	}
	public boolean obeysRule(calculatorParser.Element nextElement) {
		return rule.contains(nextElement);
	}
	public boolean obeysRule(char nextChar) {
		return rule.contains(calculatorParser.Element.getElement(nextChar));
	}

	private static class Rules {
		static final HashMap<Element, calculatorParser.Rule> ruleMap = new HashMap<>();
	}
}
