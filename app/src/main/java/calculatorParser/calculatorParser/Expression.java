package calculatorParser.calculatorParser;

import calculatorParser.Computable;

public class Expression {
	private calculatorParser.Expression l;
	private calculatorParser.Expression r;
	private Computable op;
	private double value;
	private boolean evaluated;
	
	public Expression(double value) {
		this.l = null;
		this.r = null;
		this.op = null;
		this.value = value;
		this.evaluated = true;
	}
	
	public Expression(calculatorParser.Expression l, calculatorParser.Expression r, Computable op) {
		this.l = l;
		this.r = r;
		this.op = op;
		evaluated = false;
	}
	
	public double evaluate(){
		if (evaluated)
			return value;
		else if (op == null)
			value = -l.evaluate();
		else
			value = op.compute(l.evaluate(), r.evaluate());
		evaluated = true;
		return value;
	}
}
