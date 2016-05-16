package parserV2;

import java.math.BigDecimal;

public class Expression {
	private Expression l = null;
	private Expression r = null;
	private BinaryComputable opb = null;
	private UnaryComputable opu = null;
	private double value;
	private boolean evaluated = false;
	
	public Expression(Expression l, Expression r, BinaryComputable opb) {
		this.l = l;
		this.r = r;
		this.opb = opb;
	}
	
	public Expression(Expression l, UnaryComputable opu) {
		this.l = l;
		this.opu = opu;
	}
	
	public Expression(double value) {
		this.value = AppUtils.round(value);
		evaluated = true;
	}
	
	public double evaluate() throws InvalidComputationException{
		if (evaluated) {
			return value;
		} else if (opb != null) {
            try {
                value = AppUtils.round(opb.compute(l.evaluate(), r.evaluate()));
            } catch (InvalidComputationException e) {
                throw e;
            } catch (Exception e) {
                throw new InvalidComputationException();
            }
			evaluated = true;
			return value;
		} else {
            try {
                value = AppUtils.round(opu.compute(l.evaluate()));
            } catch (InvalidComputationException e) {
                throw e;
            } catch (Exception e) {
                throw new InvalidComputationException();
            }
			evaluated = true;
			return value;
		}
	}
	
}
