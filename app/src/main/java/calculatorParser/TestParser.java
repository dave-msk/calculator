package calculatorParser;

import java.util.List;

public class TestParser {
	public static void main(String[] args) throws Exception{
		String originalStr = "-(-(-(-2)+3.25^4^2))/5*2+4/2*3.427-6/(20+4)";
		String expStr = "-( -( -  ( - 2   )+3.25^4^2))       /      5    *2+     4/2*3.427 -6/(20     +   4      )           ";
		if (CalculatorParser.hasCorrectFormat(expStr)){
			Expression exp = CalculatorParser.parse(expStr);
			System.out.println("ans = " + exp.evaluate());
		}
		/*	
		System.out.println(CalculatorParser.hasCorrectFormat(expStr));
		System.out.println(expStr);
		List<String> components = CalculatorParser.flatSplit(expStr);
		components.stream().forEach(System.out::print);
		System.out.println();
		System.out.println(originalStr);
		components.stream().forEach(System.out::println);
		System.out.println(Double.parseDouble(".2"));
		*/
	}
}
