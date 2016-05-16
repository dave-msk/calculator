package parserV2;

/**
 *
 */

public class TestParser {
	public static void main(String[] args) throws InvalidComputationException{
		String expStr1 = "-(-(-(-2)+sin(-3.25^4)^2))/5*log(2)+4/tan(-(-2))*3.427-6/(20+4)";
		System.out.println(expStr1.length());
		System.out.println(CalculatorParser.hasCorrectFormat(expStr1));
		Expression exp = CalculatorParser.parse(expStr1,true);
		System.out.println(exp.evaluate());
		
		// List<String> strs = CalculatorParser.flatSplit(exp1);
		// strs.stream().forEach(System.out::println);
	}
}
