/* Copyright (C) 2016 Siu-Kei Muk, Snigdha Brahma
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */

package calculatorParser;

public class TestParser {
	public static void main(String[] args) throws Exception{
		String originalStr = "-(-(-(-2)+3.25^4^2))/5*2+4/2*3.427-6/(20+4)";
		String expStr = "-( -( -  ( - 2   )+3.25^4^2))       /      5    *2+     4/2*3.427 -6/(20     +   4      )           ";
		if (CalculatorParser.hasCorrectFormat(expStr)){
			Expression exp = CalculatorParser.parse(expStr);
			System.out.println("ans = " + exp.evaluate());
		}
	}
}
