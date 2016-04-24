package parserV2;

import java.util.HashMap;
import java.util.Set;

import parserV2.UnaryComputable;

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
	SIN("sin", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.sin(Math.toRadians(d));
		}
	}),
	COS("cos", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.cos(Math.toRadians(d));
		}
	}),
	TAN("tan", Precedence.High, new UnaryComputable() {
		@Override
		public double compute(double d) {
			return Math.tan(Math.toRadians(d));
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

