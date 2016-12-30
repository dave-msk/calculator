/* Copyright (C) 2016 Siu-Kei Muk, Snigdha Brahma
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */

package calculatorParser;

import java.util.HashMap;
import java.util.Set;

public enum Operator {
	
	ADD ('+', Precedence.Low, new Computable() {
		@Override
		public double compute(double l, double r) {
			return l + r;
		}
	}),
	
	SUB ('-', Precedence.Low, new Computable() {
		@Override
		public double compute(double l, double r) {
			return l - r;
		}
	}),
	
	MULT ('*', Precedence.Middle, new Computable() {
		@Override
		public double compute(double l, double r) {
			return l * r;
		}
	}),

	DIV ('/', Precedence.Middle, new Computable() {
		@Override
		public double compute(double l, double r) {
			return l / r;
		}
	}),
	
	POW ('^', Precedence.POW, new Computable() {
		@Override
		public double compute(double l, double r) {
			return Math.pow(l,r);
		}
	});
	
	public final Computable op;
	
	Operator(char symbol, Precedence pre, Computable op) {
		this.op = op;
		Maps.symbolMap.put(symbol, this);
		Maps.precedenceMap.put(symbol, pre);
	}
	
	public static Computable getComputable(char opChar) {
		if (Maps.symbolMap.keySet().contains(opChar))
			return getOperator(opChar).op;
		else
			return null;
	}
	
	public static Operator getOperator(char opChar) {
		if (Maps.symbolMap.keySet().contains(opChar))
			return Maps.symbolMap.get(opChar);
		else
			return null;
	}
	
	public static Precedence getPrecedence(char opChar) {
		if (Maps.precedenceMap.keySet().contains(opChar))
			return Maps.precedenceMap.get(opChar);
		else
			return null;
	}
	
	public static Set<Character> getOpSymbols() {
		return Maps.symbolMap.keySet();
	}
	
	private static class Maps {
		static final HashMap<Character, Operator> symbolMap = new HashMap<>();
		static final HashMap<Character, Precedence> precedenceMap = new HashMap<>();
	}
	
}
