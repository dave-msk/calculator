package parserV2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public enum Rule {
	OpenBlanket(Element.OpenBlanket, new Element[] {Element.OpenBlanket, Element.Numbers, Element.Dot, Element.Negate, Element.UnaryOperators}),
	CloseBlanket(Element.CloseBlanket, new Element[] {Element.CloseBlanket, Element.BinaryOperators}), 
	UnaryOperators(Element.UnaryOperators, new Element[] {Element.OpenBlanket}),
	BinaryOperators(Element.BinaryOperators, new Element[] {Element.OpenBlanket,Element.Numbers,Element.Dot,Element.UnaryOperators}),
	Numbers(Element.Numbers, new Element[] {Element.CloseBlanket,Element.BinaryOperators,Element.Dot, Element.Numbers}),
	Negate(Element.Negate, new Element[] {Element.OpenBlanket,Element.Dot, Element.Numbers, Element.UnaryOperators}),
	Dot(Element.Dot, Element.Numbers),
    Start(Element.Start, new Element[]{Element.OpenBlanket, Element.Numbers, Element.Negate,Element.Dot,Element.UnaryOperators}),
	Null(Element.Null);
	
	private Set<Element> rule = new HashSet<>();
	private Element element;
	Rule(Element thisElement,Element[] elements) {
		rule.addAll(Arrays.asList(elements));
		this.element = thisElement;
		Rules.ruleMap.put(thisElement, this);
	}
	Rule(Element thisElement, Element element){
		rule.add(element);
		this.element = thisElement;
		Rules.ruleMap.put(thisElement, this);
	}
    Rule(Element thisElement) {
        this.element = thisElement;
    }
	public static Rule getRule(String s) {
		return Rules.ruleMap.get(Element.getElement(s));
	}
	public static Rule getRule(Element e) {
        return Rules.ruleMap.get(e);
    }
	public boolean obeysRule(Element nextElement) {
		return rule.contains(nextElement);
	}
	public boolean obeysRule(String nextSymbol) {
		return rule.contains(Element.getElement(nextSymbol));
	}
	
	private static class Rules {
		static final HashMap<Element, Rule> ruleMap = new HashMap<>();
	}
}
