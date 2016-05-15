//Testing
//Assignment 2 COMP6442
package edu.anu.comp6442.assignment2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import parserV2.AppUtils;
import parserV2.BinaryOperator;
import parserV2.CalculatorParser;
import parserV2.Element;
import parserV2.InvalidComputationException;
import parserV2.MathConstant;
import parserV2.Number;
import parserV2.Rule;
import parserV2.UnaryOperator;

public class MainActivity extends AppCompatActivity {

    private static final String CURSOR_KEY = "edu.anu.comp6442.assignment2.CURSORKEY";
    private static final String EXP_KEY = "edu.anu.comp6442.assignment2.EXPKEY";
    private static final String STATE_KEY = "edu.anu.comp6442.assignment2.STATEKEY";
    private static final String HISTORY_KEY = "edu.anu.comp6442.assignment2.HISTORYKEY";

    //Initialization
    LinearLayout historyView;
    ListView historyList;
    EditText exp_field;
    TextView value_field; //for holding the value
    StringBuilder exp;
    int cursor = 0;
    boolean evaluated = false;
    Pattern p = Pattern.compile(".*[a-zA-Z].*");

    //OnCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to hold the value after the evaluation of the expression
        value_field = (TextView) findViewById(R.id.textView_value);

        //for history
        historyView = (LinearLayout) findViewById(R.id.history_view);
        historyList = (ListView) findViewById(R.id.history_list);

        //the expression field
        exp_field = (EditText) findViewById(R.id.expression_field);
        exp_field.setRawInputType(InputType.TYPE_CLASS_TEXT);
        exp_field.setTextIsSelectable(true); //for selecting text in the expression field

        //for getting the cursor position
        exp_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = exp_field.getSelectionStart();
                evaluated = false;
            }
        });

        //checking whether a previously destroyed instance is recreated or not
        if (savedInstanceState != null) {
            //Restoring values of members from saved state
            exp = new StringBuilder(savedInstanceState.getString(EXP_KEY));
            cursor = savedInstanceState.getInt(CURSOR_KEY);
            evaluated = savedInstanceState.getBoolean(STATE_KEY);
            updateExpField();
            exp_field.setSelection(cursor);
        } else {
            //initializing exp for new instance
            exp = new StringBuilder();
        }

    }

    //getting the state back
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURSOR_KEY, cursor);
        outState.putString(EXP_KEY, exp.toString());
        outState.putBoolean(STATE_KEY, evaluated);
    }

    public void toppleHistoryView(View view) {

        if (historyView.getVisibility() == View.VISIBLE)
            historyView.setVisibility(View.GONE);
        else
            historyView.setVisibility(View.VISIBLE);
    }

    //typing the values in the text area of the calculator
    public void typeEntry(View view) {
        if (evaluated && p.matcher(exp.toString()).find())
            clearExp();
        switch (view.getId()) {
            case R.id.cancel_button:
                clearExp();
                updateExpField();
                break;
            case R.id.back_button:
                if (cursor > 0) {
                    Element element = getPreviousElement2();
                    if (element == Element.UnaryOperators) {
                        erasePreviousElement();
                    }
                    erasePreviousElement();
                }
                break;
            case R.id.num0_button:
                addNumber(Number.ZERO); break;
            case R.id.num1_button:
                addNumber(Number.ONE); break;
            case R.id.num2_button:
                addNumber(Number.TWO); break;
            case R.id.num3_button:
                addNumber(Number.THREE); break;
            case R.id.num4_button:
                addNumber(Number.FOUR); break;
            case R.id.num5_button:
                addNumber(Number.FIVE); break;
            case R.id.num6_button:
                addNumber(Number.SIX); break;
            case R.id.num7_button:
                addNumber(Number.SEVEN); break;
            case R.id.num8_button:
                addNumber(Number.EIGHT); break;
            case R.id.num9_button:
                addNumber(Number.NINE); break;
            // -------------------------------------------
            case R.id.pi_button:
                addMathConst(MathConstant.PI); break;
            case R.id.e_button:
                addMathConst(MathConstant.E); break;
            // -------------------------------------------
            case R.id.recip_button:
                addRecip(); break;
            case R.id.sqrt_button:
                addUnaryOperator(UnaryOperator.SQRT); break;
            case R.id.sin_button:
                addUnaryOperator(UnaryOperator.SIN); break;
            case R.id.cos_button:
                addUnaryOperator(UnaryOperator.COS); break;
            case R.id.tan_button:
                addUnaryOperator(UnaryOperator.TAN); break;
            case R.id.sinh_button:
                addUnaryOperator(UnaryOperator.SINH); break;
            case R.id.cosh_button:
                addUnaryOperator(UnaryOperator.COSH); break;
            case R.id.tanh_button:
                addUnaryOperator(UnaryOperator.TANH); break;
            case R.id.log_button:
                addUnaryOperator(UnaryOperator.LOG); break;
            case R.id.exp_button:
                addUnaryOperator(UnaryOperator.EXP); break;
            case R.id.sig_button:
                addUnaryOperator(UnaryOperator.SIG); break;
            case R.id.abs_button:
                addUnaryOperator(UnaryOperator.ABS); break;
            // -------------------------------------------
            case R.id.add_button:
                addBinaryOperator(BinaryOperator.ADD); break;
            case R.id.sub_button:
                addBinaryOperator(BinaryOperator.SUB); break;
            case R.id.mult_button:
                addBinaryOperator(BinaryOperator.MULT); break;
            case R.id.div_button:
                addBinaryOperator(BinaryOperator.DIV); break;
            case R.id.mod_button:
                addBinaryOperator(BinaryOperator.MOD); break;
            case R.id.pow_button:
                addBinaryOperator(BinaryOperator.POW); break;
            // ---------------------------------------------------------
            case R.id.blanket_button:
                addBlanket(); break;
            case R.id.eval_button:
                evaluate(); break;//-----------------//-------------------
            case R.id.neg_button:
                negate(); break;
            case R.id.dot_button:
                if (evaluated) {
                    clearExp();
                    evaluated = false;
                }
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

    @Deprecated
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

    @Deprecated
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
        return Element.getElement(exp.substring(earliestIndex, cursor - 1));
    }

    private Element getPreviousElement() {
        if (cursor == 0)
            return Element.Start;
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
        if (furtherestIndex == cursor)
            if (cursor < exp.length())
                return null;
            else
                return Element.Null;
        else
            return Element.getElement(exp.substring(cursor,furtherestIndex));
    }

    private void evaluate() {
        if (CalculatorParser.hasCorrectFormat(exp.toString())) {
            try {
                double result = CalculatorParser.parse(exp.toString()).evaluate();

                if (!Double.isInfinite(result) && !Double.isNaN(result))
                    result = AppUtils.round(result);
//---------------------------------------------------------------------------//------------------------------
               // clearExp();
                exp.append(result);// memory legage
                cursor = exp.length();
                updateExpField();

                evaluated = true;
            } catch (InvalidComputationException e) {
                Toast.makeText(this,"Invalid computation. Please check the expression.",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid format",Toast.LENGTH_SHORT).show();
        }
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

    private void addBlanket() {
        Element nextEle = getNextElement();
        int layer = 0;
        for (int i = 0; i < exp.length(); i++) {
            if (exp.charAt(i) == '(')
                layer++;
            if (exp.charAt(i) == ')')
                layer--;
        }
        if (isEligible(Element.OpenBlanket))
            insertEntry('(');
        else if (isEligible(Element.CloseBlanket) && layer > 0)
            if (Rule.BinaryOperators.obeysRule(nextEle))
                insertEntry(")" + BinaryOperator.MULT.symbol);
            else
                insertEntry(')');
        else if (isEligible(Element.BinaryOperators))
            insertEntry(BinaryOperator.MULT.symbol + "(");
        evaluated = false;
    }

    private void addUnaryOperator(UnaryOperator op) {
        Rule prevRule = Rule.getRule(getPreviousElement());
        if (!prevRule.obeysRule(Element.UnaryOperators))
            return;
        insertEntry(op.symbol+"(");
        evaluated = false;
    }

    private void addBinaryOperator(BinaryOperator op) {
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
        insertEntry(op.symbol);
        evaluated = false;
    }

    private void addRecip() {
        if (evaluated) clearExp();
        evaluated = false;
        if (isEligible(Element.BinaryOperators))
            insertEntry(BinaryOperator.MULT.symbol + Number.ONE.symbol
                    + BinaryOperator.DIV.symbol + "(");
        else if (isEligible(Element.MathConst))
            insertEntry(Number.ONE.symbol + BinaryOperator.DIV.symbol + "(");
    }

    private void addNumber(Number number) {
        if (evaluated) clearExp();
        evaluated = false;
        Element prevEle = getPreviousElement();
        if (prevEle == Element.CloseBlanket) {
            insertEntry(BinaryOperator.MULT.symbol + number.symbol);
            return;
        } else if (prevEle != Element.BinaryOperators && prevEle != null) {
            insertEntry(number.symbol);
            return;
        }

        Element nextEle = getNextElement();
        if (nextEle == Element.OpenBlanket || nextEle == Element.UnaryOperators) {
            insertEntry(number.symbol + BinaryOperator.MULT.symbol);
            return;
        }
        insertEntry(number.symbol);

    }

    private void addMathConst(MathConstant mathConstant) {
        if (evaluated) clearExp();
        evaluated = false;
        Element nextEle = getNextElement();
        Rule rule = Rule.MathConst;
        Rule binRule = Rule.BinaryOperators;
        if (isEligible(Element.BinaryOperators) && rule.obeysRule(nextEle))
            insertEntry(BinaryOperator.MULT.symbol + mathConstant.symbol);
        else if (isEligible(Element.MathConst) && binRule.obeysRule(nextEle))
            insertEntry(mathConstant.symbol + BinaryOperator.MULT.symbol);
        else if (isEligible(Element.MathConst))
            insertEntry(mathConstant.symbol);
    }

    private void negate() {
        evaluated = false;
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
            // Erase the negate sign and the nearest coming close blanket.
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
            // Add the negate sign in front of the current number.
            if (cursor < exp.length()) {
                char nextChar = exp.charAt(cursor);
                if (prevChar == '(' && nextChar == '-')
                    return;
            }
            insertEntryAt(searchCursor,"(-");
        }
    }

}
