//Testing
//Assignment 2 COMP6442
package edu.anu.comp6442.assignment2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import parserV2.CalculatorParser;
import parserV2.Element;
import parserV2.InvalidComputationException;
import parserV2.Rule;

public class MainActivity extends AppCompatActivity {

    EditText exp_field;
    StringBuilder exp;
    int cursor = 0;
    int num_blanket = 0;
    boolean evaluated = false;
    Pattern p = Pattern.compile(".*[a-zA-Z].*");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exp = new StringBuilder();
        exp_field = (EditText) findViewById(R.id.expression_field);
        // exp_field.setInputType(InputType.TYPE_NULL);
        exp_field.setRawInputType(InputType.TYPE_CLASS_TEXT);
        exp_field.setTextIsSelectable(true);
        exp_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = exp_field.getSelectionStart();
                evaluated = false;
            }
        });
    }

    public void typeEntry(View view) {
        if (evaluated && p.matcher(exp.toString()).find())
            clearExp();
        switch (view.getId()) {
            case R.id.cancel_button:
                clearExp();
                updateExpField();
                break;
            case R.id.back_button:
                // TODO: Erase the element before the cursor.
                if (cursor > 0) {
                    Element element = getPreviousElement2();
                    if (element == Element.UnaryOperators) {
                        erasePreviousElement();
                    }
                    erasePreviousElement();
                }
                break;
            case R.id.num0_button:
            case R.id.num1_button:
            case R.id.num2_button:
            case R.id.num3_button:
            case R.id.num4_button:
            case R.id.num5_button:
            case R.id.num6_button:
            case R.id.num7_button:
            case R.id.num8_button:
            case R.id.num9_button:
                // TODO: Check if eligible to put a number at current position,
                // TODO: ignore if ineligible, insert number otherwise.
                if (evaluated) {
                    clearExp();
                }
                addNumber(((Button)view).getText().toString());
                break;
            case R.id.add_button:
            case R.id.sub_button:
            case R.id.mult_button:
            case R.id.div_button:
            case R.id.pow_button:
                // TODO: Check if eligible to put an operator at current position,
                // TODO: ignore if ineligible, insert number otherwise.
                addBinaryOperator(((Button) view).getText().toString());
                break;
            case R.id.blanket_button:
                // TODO: Check which blanket is eligible, ignore if ineligible in both cases.
                if (cursor == 0) {
                    num_blanket++;
                    insertEntry('(');
                } else {
                    if (isEligible(Element.OpenBlanket)) {
                        num_blanket++;
                        insertEntry('(');
                    } else if (isEligible(Element.CloseBlanket) && num_blanket > 0) {
                        num_blanket--;
                        insertEntry(')');
                    } else if (isEligible(Element.BinaryOperators)) {
                        num_blanket++;
                        insertEntry("*(");
                    }
                }
                evaluated = false;
                break;
            case R.id.eval_button:

                if (CalculatorParser.hasCorrectFormat(exp.toString())) {
                    try {
                        double result = CalculatorParser.parse(exp.toString()).evaluate();
                        clearExp();
                        exp.append(result);
                        cursor = exp.length();
                        updateExpField();
                        evaluated = true;
                    } catch (InvalidComputationException e) {
                        Toast.makeText(this,"Invalid computation. Please check the expression.",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.neg_button:
                // TODO: Check eligibility, ignore if ineligible.
                negate();
                evaluated = false;
                break;
            case R.id.dot_button:
                if (evaluated) {
                    clearExp();
                    evaluated = false;
                }
                // TODO: Check eligibility, ignore if ineligible.
                if (isEligibleForDot()) {
                    insertEntry('.');
                }
                break;
        }
        exp_field.setSelection(cursor);
    }

    private void updateExpField() {
        exp_field.setText(exp.toString());
    }

    private void insertEntry(String entry) {
        exp.insert(cursor,entry);
        cursor += entry.length();
        updateExpField();
    }

    private void insertEntry(char entry) {
        exp.insert(cursor,entry);
        cursor++;
        updateExpField();
    }

    private void insertEntryAt(int position, char entry) {
        exp.insert(position,entry);
        cursor++;
        updateExpField();
    }

    private void insertEntryAt(int position, String entry) {
        exp.insert(position, entry);
        cursor += entry.length();
        updateExpField();
    }

    private void backSpace() {
        if (cursor == 0)
            return;
        exp.delete(cursor - 1, cursor);
        cursor--;
        updateExpField();
    }

    private void backSpaceAt(int position) {
        if (position <= 0)
            return;
        if (exp.charAt(position-1) == '(')
            num_blanket--;
        exp.delete(position-1,position);
        cursor = (cursor >= position) ? cursor-1 : cursor;
        updateExpField();
    }

    private void clearExp() {
        exp.setLength(0);
        cursor = 0;
    }

    private boolean isEligibleForDot() {

        for (int i = cursor-1; i >= 0; i--) {
            char currChar = exp.charAt(i);
            if (Element.getElement(currChar) == Element.Numbers)
                continue;
            if (Element.getElement(currChar) == Element.Dot)
                return false;
            else
                return true;
        }
        return true;
    }

    private boolean isEligible(Element element) {
        assert(cursor > 0);
        Rule rule = Rule.getRule(getPreviousElement());
        return rule.obeysRule(element);
    }

    private Element getPreviousElement2() {
        assert(cursor > 0);
        int earliestIndex = cursor-1;
        for (int i = cursor-2; i >= 0; i--) {
            Element element = Element.getElement(exp.substring(i, cursor-1));
            if (element != null)
                earliestIndex = i;
        }
        return Element.getElement(exp.substring(earliestIndex,cursor-1));
    }

    private Element getPreviousElement() {
        int earliestIndex = cursor;
        for (int i = cursor-1; i >= 0; i--) {
            Element element = Element.getElement(exp.substring(i, cursor));
            if (element != null)
                earliestIndex = i;
        }
        return Element.getElement(exp.substring(earliestIndex,cursor));
    }

    private Element getNextElement() {
        int furtherestIndex = cursor;
        for (int i = cursor+1; i < exp.length(); i++) {
            Element element = Element.getElement(exp.substring(cursor,i));
            if (element != null)
                furtherestIndex = i;
        }
        return Element.getElement(exp.substring(cursor,furtherestIndex));
    }

    private void eraseNextElement() {
        int furtherestIndex = cursor;
        for (int i = cursor+1; i < exp.length(); i++) {
            Element element = Element.getElement(exp.substring(cursor,i));
            if (element != null)
                furtherestIndex = i;
        }
        exp.delete(cursor,furtherestIndex);
        updateExpField();
    }

    private void erasePreviousElement() {
        int earliestIndex = cursor;
        for (int i = cursor-1; i >= 0; i--) {
            Element element = Element.getElement(exp.substring(i,cursor));
            if (element != null)
                earliestIndex = i;
        }
        exp.delete(earliestIndex,cursor);
        cursor -= cursor - earliestIndex;
        updateExpField();
    }

    private void addBinaryOperator(String op) {
        if (cursor == 0)
            return;
        Rule prevRule = Rule.getRule(getPreviousElement());
        if (!prevRule.obeysRule(Element.BinaryOperators))
            return;
        if (exp.length() > cursor) {
            Rule currRule = Rule.BinaryOperators;

            // ---------------------------
            Element nextEle = getNextElement();
            if (nextEle == Element.BinaryOperators)
                eraseNextElement();
            else if (!currRule.obeysRule(nextEle))
                return;
        }
        insertEntry(op);
        evaluated = false;
    }

    private void addNumber(String num) {
        if (cursor > 0) {
            char prevChar = exp.charAt(cursor-1);
            Element prevEle = Element.getElement(prevChar);
            if (prevEle == Element.CloseBlanket) {
                insertEntry("*" + num);
                return;
            } else if (prevEle != Element.BinaryOperators && prevEle != null) {
                insertEntry(num);
                return;
            }
        }
        if (exp.length() > cursor) {
            // char nextChar = exp.charAt(cursor);
            int index = 0;
            StringBuilder sbd = new StringBuilder();
            for (int i = cursor; i < exp.length(); i++) {
                sbd.append(exp.charAt(i));
                if (Element.getElement(sbd.toString()) != null)
                    index = i;
            }
            Element nextEle = Element.getElement(exp.substring(cursor,index+1));
            if (nextEle == Element.OpenBlanket || nextEle == Element.UnaryOperators) {
                insertEntry(num + "*");
                return;
            }
        }
        insertEntry(num);
        evaluated = false;
    }

    private void negate() {
        boolean negateDetected = false;
        int searchCursor = 0;
        char prevChar = (cursor > 0) ? exp.charAt(cursor-1) : 0;
        for (int i = cursor-1; i >= 0; i--) {
            char currChar = exp.charAt(i);
            Element currEle = Element.getElement(currChar);

            if (negateDetected) {
                if (currEle != Element.OpenBlanket) {
                    negateDetected = false;
                    searchCursor = i+1;
                    break;
                }
                searchCursor = i+1;
                break;
            }
            if (currEle == Element.CloseBlanket)
                return;
            if (currEle == Element.Numbers || currEle == Element.Dot)
                continue;
            if (currChar == '-') {
                negateDetected = true;
                continue;
            }
            if (currEle == Element.BinaryOperators || currEle == Element.OpenBlanket) {
                searchCursor = i+1;
                break;
            }
        }

        if (negateDetected) {
            // TODO: Erase the negate sign and the nearest coming close blanket.
            for (int i = cursor; i < exp.length(); i++) {
                char currChar = exp.charAt(i);
                if (Element.getElement(currChar) == Element.CloseBlanket) {
                    backSpaceAt(i+1);
                    break;
                }
            }
            backSpaceAt(searchCursor);
            backSpaceAt(searchCursor);
        } else {
            // TODO: Add the negate sign in front of the current number.
            if (cursor < exp.length()) {
                char nextChar = exp.charAt(cursor);
                if (prevChar == '(' && nextChar == '-')
                    return;
            }
            insertEntryAt(searchCursor,"(-");
            num_blanket++;
        }
        /*
        if (exp.length() == 0) {
            num_blanket++;
            insertEntry("(-");
            return;
        }
        if (exp.length()-cursor == 1) {
            char nextChar = exp.charAt(cursor);
            if (cursor == 0) {
                if (rule.obeysRule(nextChar)) {

                }
            }
        }*/

    }
}
