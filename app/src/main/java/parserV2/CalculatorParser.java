package parserV2;

import java.util.ArrayList;
import java.util.List;

public class CalculatorParser {
	public static Expression parse(String expStr) {
		
		expStr = expStr.trim();
		while (isSurrounded(expStr))
			expStr = expStr.substring(1,expStr.length()-1);
		
		List<String> components = flatSplit(expStr);
		
		if (components.size() == 1) {
			expStr = components.get(0);
			if (isNumeric(expStr)) {
				return new Expression(Double.parseDouble(expStr));
			}

            int furtherestIndex = 0;
            // StringBuilder sbd = new StringBuilder();
            for (int i = 0; i < expStr.length(); i++) {
                UnaryOperator op = UnaryOperator.getUnaryOperator(expStr.substring(0, i + 1));
                if (op != null)
                    furtherestIndex = i+1;
            }
			if (furtherestIndex > 0) {
                UnaryOperator op = UnaryOperator.getUnaryOperator(expStr.substring(0, furtherestIndex));
                return new Expression(parse(expStr.substring(furtherestIndex)), op.op);
            } else {
                 return new Expression(MathConstant.getMathConstant(expStr).value);
            }
		}
		
		// Scan for the operators with low precedence.
		for (int i = components.size()-2; i >= 0; i--) {
			String currStr = components.get(i);
			BinaryOperator opb = BinaryOperator.getBinaryOperator(currStr);
			Precedence pre = BinaryOperator.getPrecedence(currStr);
			if (opb != null && pre == Precedence.Low) {
				StringBuilder expL = new StringBuilder();
				StringBuilder expR = new StringBuilder();
				for (int j = 0; j < i; j++)
					expL.append(components.get(j));
				for (int j = i+1; j < components.size(); j++)
					expR.append(components.get(j));
				return new Expression(parse(expL.toString()), parse(expR.toString()), opb.op);
			}
		}
		
		// Check if negation is present.
		if (components.get(0).charAt(0) == 'n')
			return new Expression(parse(expStr.substring(1)), UnaryOperator.NEAGTE.op);
		
		// Scan for the operators with middle precedence.
		for (int i = components.size()-2; i >= 0; i--) {
			String currStr = components.get(i);
			BinaryOperator opb = BinaryOperator.getBinaryOperator(currStr);
			Precedence pre = BinaryOperator.getPrecedence(currStr);
			if (opb != null && pre == Precedence.Middle) {
				StringBuilder expL = new StringBuilder();
				StringBuilder expR = new StringBuilder();
				for (int j = 0; j < i; j++)
					expL.append(components.get(j));
				for (int j = i+1; j < components.size(); j++) 
					expR.append(components.get(j));
				return new Expression(parse(expL.toString()),parse(expR.toString()), opb.op);
			}
		}
		
		// Scan for power operations.
		for (int i = 1; i < components.size(); i++) {
			String currStr = components.get(i);
			BinaryOperator opb = BinaryOperator.getBinaryOperator(currStr);
			if (opb == BinaryOperator.POW) {
				StringBuilder expL = new StringBuilder();
				StringBuilder expR = new StringBuilder();
				for (int j = 0; j < i; j++)
					expL.append(components.get(j));
				for (int j = i+1; j < components.size(); j++) 
					expR.append(components.get(j));
				return new Expression(parse(expL.toString()),parse(expR.toString()), opb.op);
			}
		}
		
		// Scan for binary operators with high precedence.
		for (int i = components.size()-2; i >= 0; i--) {
			String currStr = components.get(i);
			BinaryOperator opb = BinaryOperator.getBinaryOperator(currStr);
			Precedence pre = BinaryOperator.getPrecedence(currStr);
			if (opb != null && pre == Precedence.High) {
				StringBuilder expL = new StringBuilder();
				StringBuilder expR = new StringBuilder();
				for (int j = 0; j < i; j++)
					expL.append(components.get(j));
				for (int j = i+1; j < components.size(); j++) 
					expR.append(components.get(j));
				return new Expression(parse(expL.toString()),parse(expR.toString()), opb.op);
			}
		}
		return null;
	}
	
	public static boolean hasCorrectFormat(String expStr) {
		int layer = 0;
        int furtherestIndex = -1;
		// boolean hasWhiteSpace = false;
		boolean hasDot = false;
		// boolean accumulating = false;

		StringBuilder currElementStr = new StringBuilder();
        Element prevElement = Element.Start;

		for (int i = 0; i < expStr.length(); i++) {
            if (furtherestIndex >= i)
                continue;

            StringBuilder sbd = new StringBuilder();
            for (int j = i; j < expStr.length(); j++) {
                char currChar = expStr.charAt(j);

                if ((prevElement == Element.Start || prevElement == Element.OpenBlanket)  && currChar == '-')
                    currChar = 'n';

                sbd.append(currChar);
                Element currEle = Element.getElement(sbd.toString());
                if (currEle != null) {
                    currElementStr.setLength(0);
                    currElementStr.append(sbd);
                    furtherestIndex = j;
                }
            }

            Element currEle = Element.getElement(currElementStr.toString());

			Rule rule = Rule.getRule(prevElement);
			if (!rule.obeysRule(currEle))
				return false;
			
			switch (currEle) {
			case OpenBlanket:
				layer++;
				break;
			case CloseBlanket:
				if (layer == 0)
					return false;
				layer--;
				hasDot = false;
				break;
			case Dot:
				if (hasDot)
					return false;
				hasDot = true;
				break;
			case BinaryOperators:
				hasDot = false;
			}
			prevElement = currEle;
		}

		if (layer != 0)
			return false;
		if (prevElement != Element.Numbers && prevElement != Element.CloseBlanket
                && prevElement != Element.MathConst)
			return false;
        if (furtherestIndex != expStr.length()-1)
            return false;
		return true;
	}
	
	private static boolean isNumeric(String str) {
		return str.matches("\\d*(\\.\\d+)?") && !str.isEmpty();
	}
	
	private static boolean areNonSeparable(String prev, String curr) {
		Element prevEle = Element.getElement(prev);
		Element currEle = Element.getElement(curr);
		if (prevEle != null && currEle != null) {
			switch (prevEle) {
			case Dot:
				return true;
			case Numbers:
				if (currEle == Element.Dot || currEle == Element.Numbers)
					return true;
			}
		}
		return false;
	}
	
	private static List<String> flatSplit(String expStr) {
		int layer = 0;
		StringBuilder sbd = new StringBuilder();
		List<String> result = new ArrayList<>();
		StringBuilder prevElementStr = new StringBuilder();
		StringBuilder currElementStr = new StringBuilder();
		for (int i = 0; i < expStr.length(); i++) {
			char currChar = expStr.charAt(i);
			String prevStr = prevElementStr.toString();
			
			if (currChar == ' ')
				continue;
			else if (currChar == '(')
				layer++;
			else if (currChar == ')')
				layer--;
			
			if ((prevStr.length() == 0 || prevStr.equals("(")) && currChar == '-')
				sbd.append('n');
			else {
				currElementStr.append(currChar);
				String currStr = currElementStr.toString();
				Element currEle = Element.getElement(currStr);
				if (currEle == null)
					continue;
				
				if (layer == 0 && currEle == Element.BinaryOperators) {
					result.add(sbd.toString());
					result.add(currStr);
					sbd.setLength(0);
				} else
					sbd.append(currStr);
			}
			prevElementStr.setLength(0);
			prevElementStr.append(currElementStr);
			currElementStr.setLength(0);
		}
		result.add(sbd.toString());
		return result;
	}
	
	private static boolean isSurrounded(String expStr) {
		int layer = 0;
		if (expStr.isEmpty())
			return false;
		if (expStr.charAt(0) != '(')
			return false;
		StringBuilder currSbd = new StringBuilder();
		for (int i = 0; i < expStr.length(); i++) {
			currSbd.append(expStr.charAt(i));
			String currStr = currSbd.toString();
			Element currEle = Element.getElement(currStr);
			if (currEle == null)
				continue;
			switch (currEle) {
			case OpenBlanket:
				layer++;
				break;
			case CloseBlanket:
				layer--;
				break;
			default:
				if (layer == 0 && currEle == Element.BinaryOperators)
					return false;
			}
			currSbd.setLength(0);
		}
		return true;
	}
}
