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
 *
 * This class consists of the unary operators used in the calculator.
 */
public enum UnaryOperator {
	NEAGTE("n", Precedence.Low, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return -d;
		}
	}),
	SQRT("sqrt", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.sqrt(d);
		}
	}),
	SIND("sin", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.sin(Math.toRadians(d));
		}
	}),
	COSD("cos", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.cos(Math.toRadians(d));
		}
	}),
	TAND("tan", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.tan(Math.toRadians(d));
		}
	}),
	SIN("sin", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.sin(d);
		}
	}),
	COS("cos", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.cos(d);
		}
	}),
	TAN("tan", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.tan(d);
		}
	}),
	SINH("sinh", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.sinh(d);
		}
	}),
	COSH("cosh", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.cosh(d);
		}
	}),
	TANH("tanh", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.tanh(d);
		}
	}),
	LOG("log", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.log(d);
		}
	}),
	EXP("exp", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.exp(d);
		}
	}),
	SIG("sig", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return 1 /(1+Math.exp(-d));
		}
	}),
	ABS("abs", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.abs(d);
		}
	});
	
	public final UnaryComputable op;
    public final String symbol;
	
	UnaryOperator(String symbol, Precedence pre, UnaryComputable op) {
		this.op = op;
        this.symbol = symbol;
		Maps.symbolMap.put(symbol, this);
		Maps.precedenceMap.put(symbol, pre);
	}
	
	public static UnaryComputable getUnaryComputable(String symbol) {
		if (Maps.symbolMap.keySet().contains(symbol))
			return getUnaryOperator(symbol).op;
		else
			return null;
	}
	
	public static UnaryOperator getUnaryOperator(String symbol) {
		if (Maps.symbolMap.keySet().contains(symbol))
			return Maps.symbolMap.get(symbol);
		else
			return null;
	}
	
	public static Precedence getPrecedence(String symbol) {
		if (Maps.precedenceMap.keySet().contains(symbol))
			return Maps.precedenceMap.get(symbol);
		else
			return null;
	}
	
	public static Set<String> getOpSymbols() {
		return Maps.symbolMap.keySet();
	}
	
	private static class Maps {
		static final HashMap<String, UnaryOperator> symbolMap = new HashMap<>();
		static final HashMap<String, Precedence> precedenceMap = new HashMap<>();
	}
	
}

