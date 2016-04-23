package parserV2;

import java.util.HashMap;
import java.util.Set;

public enum BinaryOperator {
	ADD ("+", Precedence.Low, new BinaryComputable() {
		@Override
		public double compute(double l, double r) {
			return l + r;
		}
	}),
	
	SUB ("-", Precedence.Low, new BinaryComputable() {
		@Override
		public double compute(double l, double r) {
			return l - r;
		}
	}),
	
	MULT ("*", Precedence.Middle, new BinaryComputable() {
		@Override
		public double compute(double l, double r) {
			return l * r;
		}
	}),

	DIV ("/", Precedence.Middle, new BinaryComputable() {
		@Override
		public double compute(double l, double r) {
			return l / r;
		}
	}),
	
	POW ("^", Precedence.Pow, new BinaryComputable() {
		@Override
		public double compute(double l, double r) {
			return Math.pow(l,r);
		}
	});
	
	public final BinaryComputable op;
	
	BinaryOperator(String symbol, Precedence pre, BinaryComputable op) {
		this.op = op;
		Maps.symbolMap.put(symbol, this);
		Maps.precedenceMap.put(symbol, pre);
	}
	
	public static BinaryComputable getBinaryComputable(String symbol) {
		if (Maps.symbolMap.keySet().contains(symbol))
			return getBinaryOperator(symbol).op;
		else
			return null;
	}
	
	public static BinaryOperator getBinaryOperator(String symbol) {
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
		static final HashMap<String, BinaryOperator> symbolMap = new HashMap<>();
		static final HashMap<String, Precedence> precedenceMap = new HashMap<>();
	}
}
