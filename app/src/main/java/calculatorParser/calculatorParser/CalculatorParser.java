package calculatorParser.calculatorParser;

import java.util.ArrayList;
import java.util.List;

import calculatorParser.Element;
import calculatorParser.Expression;
import calculatorParser.Operator;
import calculatorParser.Precedence;
import calculatorParser.Rule;

public class CalculatorParser {
	public static Expression parse(String expStr) {
		// We perform multiple scans each layer.
		// 1st scan: Elimination of out-most ineffective blankets,
		//           and spaces.
		expStr = expStr.trim();
		while (surrounded(expStr)) {
			// System.out.println(expStr);
			expStr = expStr.substring(1,expStr.length()-1).trim();
		}
		
		// Split the expression into sub-expressions.
		List<String> components = flatSplit(expStr);
		
		// Leaf and negation cases.
		if (components.size() == 1) {
			if (isNumeric(expStr))
				return new Expression(Double.parseDouble(expStr));
			else
				return new Expression(parse(expStr.substring(1)),null,null);
		}
		
		// Scan for operators of low precedence.
		int index = -1;
		for (int i = components.size()-1; i >= 0; i--) {
			String component = components.get(i);
			if (component.length() != 1) continue;
			char opChar = component.charAt(0);
			if (Operator.getPrecedence(opChar) == Precedence.Low) {
				index = i;
				break;
			}
		}
		
		if (index > 0) {
			StringBuilder sbd = new StringBuilder();
			for (int i = 0; i < index; i++) {
				sbd.append(components.get(i));
			}
			String leftExpStr = sbd.toString();
			sbd.setLength(0);
			for (int i = index+1; i < components.size(); i++) {
				sbd.append(components.get(i));
			}
			String rightExpStr = sbd.toString();
			char opChar = components.get(index).charAt(0);
			return new Expression(parse(leftExpStr),parse(rightExpStr),Operator.getComputable(opChar));
		}
		
		// Scan for operators of middle precedence.
		for (int i = components.size()-1; i >= 0; i--) {
			String component = components.get(i);
			if (component.length() != 1) continue;
			char opChar = component.charAt(0);
			if (Operator.getPrecedence(opChar) == Precedence.Middle) {
				index = i;
				break;
			}
		}
		
		if (index > 0) {
			StringBuilder sbd = new StringBuilder();
			for (int i = 0; i < index; i++) {
				sbd.append(components.get(i));
			}
			String leftExpStr = sbd.toString();
			sbd.setLength(0);
			for (int i = index+1; i < components.size(); i++) {
				sbd.append(components.get(i));
			}
			String rightExpStr = sbd.toString();
			char opChar = components.get(index).charAt(0);
			return new Expression(parse(leftExpStr),parse(rightExpStr),Operator.getComputable(opChar));
		}
		
		// Scan for operators of high precedence.
		for (int i = components.size()-1; i >= 0; i--) {
			String component = components.get(i);
			if (component.length() != 1) continue;
			char opChar = component.charAt(0);
			if (Operator.getPrecedence(opChar) == Precedence.Middle) {
				index = i;
				break;
			}
		}
				
		if (index > 0) {
			StringBuilder sbd = new StringBuilder();
			for (int i = 0; i < index; i++) {
				sbd.append(components.get(i));
			}
			String leftExpStr = sbd.toString();
			sbd.setLength(0);
			for (int i = index+1; i < components.size(); i++) {
				sbd.append(components.get(i));
			}
			String rightExpStr = sbd.toString();
			char opChar = components.get(index).charAt(0);
			return new Expression(parse(leftExpStr),parse(rightExpStr),Operator.getComputable(opChar));
		}
		
		// Handle the special case of exponent, which is right-associative.
		for (int i = 0; i < components.size(); i++) {
			String component = components.get(i);
			if (component.length() != 1) continue;
			char opChar = component.charAt(0);
			if (Operator.getPrecedence(opChar) == Precedence.POW) {
				index = i;
				break;
			}
		}
				
		if (index > 0) {
			StringBuilder sbd = new StringBuilder();
			for (int i = 0; i < index; i++) {
				sbd.append(components.get(i));
			}
			String leftExpStr = sbd.toString();
			sbd.setLength(0);
			for (int i = index+1; i < components.size(); i++) {
				sbd.append(components.get(i));
			}
			String rightExpStr = sbd.toString();
			char opChar = components.get(index).charAt(0);
			return new Expression(parse(leftExpStr),parse(rightExpStr),Operator.getComputable(opChar));
		}
		return null;
	}
	
	public static boolean hasCorrectFormat(String expStr) {
		int layer = 0;
		boolean dot = false;
		boolean hasSpace = false;
		char prevChar = 0;
		char currChar = 0;
		
		for (int i = 0; i < expStr.length(); i++) {
			
			if (expStr.charAt(i) == ' ') {
				hasSpace = true;
				continue;
			}
			
			prevChar = currChar;
			currChar = expStr.charAt(i);
			
			if ((prevChar == 0 || prevChar == '(') && (currChar == '-')) 
				currChar = 'n';
			
			Rule rule = Rule.getRule(prevChar);
			Element currEle = Element.getElement(currChar);
			if (!rule.obeysRule(currEle))
				return false;
			if (areNonSeparable(prevChar, currChar) && hasSpace)
				return false;
			hasSpace = false;
			
			switch (currEle) {
			case OpenBlanket:
				layer++;
				break;
			case CloseBlanket:
				if (layer == 0)
					return false;
				layer--;
				dot = false;
				break;
			case Dot:
				if (dot)
					return false;
				dot = true;
				break;
			case Operators:
				dot = false;
			}
		}
		if (layer != 0)
			return false;
		return true;
	}
	
	public static List<String> flatSplit(String expStr) {
		int layer = 0;
		StringBuilder sbd = new StringBuilder();
		List<String> result = new ArrayList<>();
		char prevChar = 0;
		for (int i = 0; i < expStr.length(); i++) {
			char currChar = expStr.charAt(i);
			
			if (currChar == ' ')
				continue;
			else if (currChar == '(')
				layer++;
			else if (currChar == ')')
				layer--;
			
			if ((prevChar == 0 || prevChar == '(') && currChar == '-')
				sbd.append('n');
			else {
				if (layer == 0 && Element.getElement(currChar) == Element.Operators) {
					result.add(sbd.toString());
					result.add(Character.toString(currChar));
					sbd.setLength(0);
				} else
					sbd.append(currChar);
			}
			prevChar = currChar;
		}
		result.add(sbd.toString());
		return result;
	}
	
	private static boolean isNumeric(String str) {
		return !str.isEmpty() && str.matches("\\d*(\\.\\d+)?");
	}
	
	private static boolean areNonSeparable(char prev, char curr) {
		Element prevEle = Element.getElement(prev);
		Element currEle = Element.getElement(curr);
		switch (prevEle) {
		case Dot:
			return true;
		case Numbers:
			if (currEle == Element.Dot || currEle == Element.Numbers)
				return true;
		}
		return false;
	}
	
	private static boolean surrounded(String expStr) {
		int layer = 0;
		if (expStr.charAt(0) != '(')
			return false;
		for (int i = 0; i < expStr.length(); i++) {
			switch (expStr.charAt(i)){
			case '(':
				layer++;
				continue;
			case ')':
				layer--;
				continue;
			default:
				if (layer == 0 && Operator.getOperator(expStr.charAt(i)) != null){
					return false;
				}
			}
		}
		return true;
	}
}
