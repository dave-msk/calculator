package calculatorParser.calculatorParser;

import java.util.HashMap;
import java.util.Set;

import calculatorParser.*;
import calculatorParser.Precedence;

public enum Operator {
	
	ADD ('+', calculatorParser.calculatorParser.Precedence.Low, new Computable() {
		@Override
		public double compute(double l, double r) {
			return l + r;
		}
	}),

	SUB ('-', calculatorParser.Precedence.Low, new Computable() {
		@Override
		public double compute(double l, double r) {
			return l - r;
		}
	}),

	MULT ('*', calculatorParser.Precedence.Middle, new Computable() {
		@Override
		public double compute(double l, double r) {
			return l * r;
		}
	}),

	DIV ('/', calculatorParser.Precedence.Middle, new Computable() {
		@Override
		public double compute(double l, double r) {
			return l / r;
		}
	}),

	POW ('^', calculatorParser.Precedence.POW, new Computable() {
		@Override
		public double compute(double l, double r) {
			return Math.pow(l,r);
		}
	});

	public final Computable op;

	Operator(char symbol, calculatorParser.Precedence pre, Computable op) {
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

	public static calculatorParser.Operator getOperator(char opChar) {
		if (Maps.symbolMap.keySet().contains(opChar))
			return Maps.symbolMap.get(opChar);
		else
			return null;
	}

	public static calculatorParser.Precedence getPrecedence(char opChar) {
		if (Maps.precedenceMap.keySet().contains(opChar))
			return Maps.precedenceMap.get(opChar);
		else
			return null;
	}

	public static Set<Character> getOpSymbols() {
		return Maps.symbolMap.keySet();
	}

	private static class Maps {
		static final HashMap<Character, calculatorParser.Operator> symbolMap = new HashMap<>();
		static final HashMap<Character, Precedence> precedenceMap = new HashMap<>();
	}
	
}
