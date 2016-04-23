package calculatorParser;

public class Expression {
	private Expression l;
	private Expression r;
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
	
	public Expression(Expression l, Expression r, Computable op) {
		this.l = l;
		this.r = r;
		this.op = op;
		evaluated = false;
	}
	
	public double evaluate() throws InvalidComputationException{
		if (evaluated)
			return value;
		else if (op == null)
			try {
				value = -l.evaluate();
			} catch (InvalidComputationException e) {
				throw e;
			}
		else
			try {
				value = op.compute(l.evaluate(), r.evaluate());
			} catch (InvalidComputationException e) {
				throw e;
			} catch (Exception e) {
				throw new InvalidComputationException();
			}
		evaluated = true;
		return value;
	}
}
