/* Copyright (C) 2016 Siu-Kei Muk, Snigdha Brahma
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license.  See the LICENSE file for details.
 */

package parserV2;

import java.util.ArrayList;
import java.util.List;

/**
 * COMP6442 - Software Construction
 * Assignment 2 - Calculator
 * Authors: Siu Kei, Muk & Snigdha Brahma
 *
 * This is the main parser used in the calculator.
 * This class consists of only static methods.
 */

public final class CalculatorParser {

	private CalculatorParser(){};

	public static Expression parse(String expStr, boolean degreeMode) {

		// Trim the expStr
		expStr = expStr.trim();
        // Remove outer-most blankets surrounding the whole expression
		while (isSurrounded(expStr))
			expStr = expStr.substring(1,expStr.length()-1);

        // Split the expression in the same layer into
        // exp, binary operation, exp, binary operation,... ,exp
		List<String> components = flatSplit(expStr);

        // If only 1 element present after splitting,
        // it must be a number, a math constant or a unary function(exp)
		if (components.size() == 1) {
			expStr = components.get(0);
            System.out.println(expStr);
            // If it is a number, then return an expression of pure number.
            if (isNumeric(expStr)) {
				return new Expression(Double.parseDouble(expStr));
			}

            // We check if the expression is unary function(exp)
            int furtherestIndex = 0;
            for (int i = 0; i < expStr.length(); i++) {
                UnaryOperator op = UnaryOperator.getUnaryOperator(expStr.substring(0, i + 1));
                if (op != null)
                    furtherestIndex = i+1;
            }
            // If the unary function is detected, than it is unary function(exp)
            // Otherwise it must be a math constant.
			if (furtherestIndex > 0) {
                UnaryOperator op = UnaryOperator.getUnaryOperator(expStr.substring(0, furtherestIndex));
                if (degreeMode && op == UnaryOperator.SIN)
                    op = UnaryOperator.SIND;
                else if (degreeMode && op == UnaryOperator.COS)
                    op = UnaryOperator.COSD;
                else if (degreeMode && op == UnaryOperator.TAN)
                    op = UnaryOperator.TAND;
                return new Expression(parse(expStr.substring(furtherestIndex),degreeMode), op.op);
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
				return new Expression(parse(expL.toString(),degreeMode), parse(expR.toString(),degreeMode), opb.op);
			}
		}

        // If the progress reaches here, the form must be
        // (+-)exp, [*/^], exp, [*/^], ... , exp
		// Check if negation is present before the first term.
		if (components.get(0).charAt(0) == 'n')
			return new Expression(parse(expStr.substring(1),degreeMode), UnaryOperator.NEAGTE.op);

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
				return new Expression(parse(expL.toString(),degreeMode),parse(expR.toString(),degreeMode), opb.op);
			}
		}
		
		// Scan for power operations.
        // Right associativity
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
				return new Expression(parse(expL.toString(),degreeMode),parse(expR.toString(),degreeMode), opb.op);
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
				return new Expression(parse(expL.toString(),degreeMode),parse(expR.toString(),degreeMode), opb.op);
			}
		}
		return null;
	}

    // This method scans the whole expression to check
    // if it is in the correct format.
	public static boolean hasCorrectFormat(String expStr) {
		int layer = 0; // "layer" tracks the current depth in the expression
        int furtherestIndex = -1; // This is used in finding the longest element string.
		boolean hasDot = false; // This is to prevent two dots appearing in the same number.

		StringBuilder currElementStr = new StringBuilder();
        Element prevElement = Element.Start;//initially set to @start but gets updated by the rule

		for (int i = 0; i < expStr.length(); i++) {
            if (furtherestIndex >= i)
                continue;

            // Get the current element by checking the longest eligible element string.
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

            // Capture the element
            Element currEle = Element.getElement(currElementStr.toString());

            // Check if the grammar is obeyed
			Rule rule = Rule.getRule(prevElement);
			if (!rule.obeysRule(currEle))
				return false;

            // Housing keeping variables update.
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

        // Return false if the expression is not ended properly.
		if (layer != 0)
			return false;
		if (prevElement != Element.Numbers && prevElement != Element.CloseBlanket
                && prevElement != Element.MathConst)
			return false;
        if (furtherestIndex != expStr.length()-1)
            return false;
		return true;
	}

    // Check if the expression string is numeric.
	public static boolean isNumeric(String str) {
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

    // This method splits the expression in the same layer into a list of the form
    // exp, binary operation, exp, binary operation,... ,exp
	private static List<String> flatSplit(String expStr) {
		int layer = 0;

		int furthestIndex = 0;
		char prevChar = 0;
		StringBuilder sbd = new StringBuilder();
		List<String> result = new ArrayList<>();
        // Scan through the whole expression string
		for (int i = 0; i < expStr.length(); i++) {
			if (furthestIndex > i) {
				continue;
			}

			if (i > 0)
				prevChar = expStr.charAt(i-1);

			char currChar = expStr.charAt(i);

            // Skip the spaces, and update housekeeping variables
			if (currChar == ' ')
				continue;
			else if (currChar == '(')
				layer++;
			else if (currChar == ')')
				layer--;

            // Convert negation operation to 'n'
			if ((prevChar == 0 || prevChar == '(') && currChar == '-') {
				sbd.append('n');
				continue;
			}

            // Get the next element.
			for (int j = i; j < expStr.length(); j++) {
				Element element = Element.getElement(expStr.substring(i,j+1));
				if (element != null)
					furthestIndex = j+1;
			}

            // If the element is a binary operator, than update the resulting list
            // else add it into the current expression candidate.
			String currElementStr = expStr.substring(i,furthestIndex);
			Element currElement = Element.getElement(currElementStr);
			if (layer == 0 && currElement == Element.BinaryOperators) {
				result.add(sbd.toString());
				result.add(currElementStr);
				sbd.setLength(0);
			} else {
				sbd.append(currElementStr);
			}
		}
        // Add the last expression to the list.
		result.add(sbd.toString());
		return result;
	}

    // This method checks if the whole expression string is surrounded by blankets.
	private static boolean isSurrounded(String expStr) {
		int layer = 0;
		if (expStr.isEmpty())
			return false;
		if (expStr.charAt(0) != '(')
			return false;

        // If the layer hits zero while it is still in the middle of the expression string,
        // then it is not surrounded by blankets, otherwise it is.
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
