/* Copyright (C) 2016 Siu-Kei Muk, Snigdha Brahma
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */

package calculatorParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public enum Rule {
	OpenBlanket(Element.OpenBlanket, new Element[] {Element.OpenBlanket, Element.Numbers, Element.Dot, Element.Negate}),
	CloseBlanket(Element.CloseBlanket, new Element[] {Element.CloseBlanket, Element.Operators}), 
	Operators(Element.Operators, new Element[] {Element.OpenBlanket,Element.Numbers,Element.Dot}),
	Numbers(Element.Numbers, new Element[] {Element.CloseBlanket,Element.Operators,Element.Dot, Element.Numbers}),
	Negate(Element.Negate, new Element[] {Element.OpenBlanket,Element.Dot, Element.Numbers}),
	Dot(Element.Dot, Element.Numbers),
	Null(Element.Null, new Element[]{Element.OpenBlanket, Element.Numbers, Element.Negate,Element.Dot});
	
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
	public static Rule getRule(char c) {
		return Rules.ruleMap.get(Element.getElement(c));
	}
	public boolean obeysRule(Element nextElement) {
		return rule.contains(nextElement);
	}
	public boolean obeysRule(char nextChar) {
		return rule.contains(Element.getElement(nextChar));
	}
	
	private static class Rules {
		static final HashMap<Element, Rule> ruleMap = new HashMap<>();
	}
}
