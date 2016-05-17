//Assignment 2 COMP6442
package edu.anu.comp6442.assignment2;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import historyAdapter.HistoryAdapter;
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
    private static final String MODE_KEY = "edu.anu.comp6442.assignment2.MODEKEY";
    private static final String HISTORY_FILE = "edu.anu.comp6442.assignment2.HISTORY_FILE.txt";
    private static final String HIS_EXP_KEY = "edu.anu.comp6442.assignment2.HIS_EXP_KEY";
    private static final String HIS_VAL_KEY = "edu.anu.comp6442.assignment2.HIS_VAL_KEY";
    private static final String DEG_MODE = "DEG";
    private static final String RAD_MODE = "RAD";

    //Variable declarations
    LinearLayout historyView;
    ListView historyListView;
    EditText exp_field;
    TextView value_field; //for holding the value
    Button mode_button;
    StringBuilder exp;
    List<Map<String,String>> historyList;
    HistoryAdapter historyAdapter;
    int cursor = 0;
    boolean evaluated = false;
    boolean degreeMode = true;
    double result = 0;
    Pattern p = Pattern.compile(".*[a-zA-Z].*");

    //OnCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the status bar. For API 19 or above the immersive mode is enabled.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT < 19){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                  | View.SYSTEM_UI_FLAG_FULLSCREEN
                  | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }

        // The field to hold the current evaluation result.
        value_field = (TextView) findViewById(R.id.textView_value);

        // The mode button, for deg or rad modes.
        mode_button = (Button) findViewById(R.id.button_mode);

        // History related objects
        historyView = (LinearLayout) findViewById(R.id.history_view);
        historyListView = (ListView) findViewById(R.id.history_list);
        historyList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this,R.layout.row_view,historyList,new String[] {HIS_EXP_KEY,HIS_VAL_KEY});
        // Make sure the history file exists.
        try {
            FileOutputStream fos = openFileOutput(HISTORY_FILE,Context.MODE_APPEND);
            fos.close();
        } catch (IOException e) {}

        historyListView.setAdapter(historyAdapter);
        initializeHistoryList(); // Initialize the list.

        // Insert the selected value from history list directly to the expression at cursor.
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String val_string = historyList.get(position).get(HIS_VAL_KEY);
                if (evaluated) {
                    clearExp();
                    evaluated = false;
                }
                exp.insert(cursor,val_string);
                updateExpField();
                cursor += val_string.length();
                exp_field.setSelection(cursor);

                partialEvaluate();
            }
        });

        // The expression field
        exp_field = (EditText) findViewById(R.id.expression_field);
        exp_field.setRawInputType(InputType.TYPE_CLASS_TEXT);
        exp_field.setTextIsSelectable(true); //for selecting text in the expression field

        // Capture the cursor position if clicked
        exp_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = exp_field.getSelectionStart();
                if (!p.matcher(exp.toString()).find())
                    evaluated = false;
            }
        });

        // Restore previous state, if any
        if (savedInstanceState != null) {
            //Restoring values of members from saved state
            exp = new StringBuilder(savedInstanceState.getString(EXP_KEY));
            cursor = savedInstanceState.getInt(CURSOR_KEY);
            evaluated = savedInstanceState.getBoolean(STATE_KEY);
            updateExpField();
            exp_field.setSelection(cursor);
            degreeMode = savedInstanceState.getBoolean(MODE_KEY);
        } else {
            // Initialize exp for new instance
            exp = new StringBuilder();
        }

        // Display the current mode on the button.
        if (degreeMode)
            mode_button.setText(DEG_MODE);
        else
            mode_button.setText(RAD_MODE);

        // Consume the long click to prevent keyboard from showing
        exp_field.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    // Saving the current state to pass information
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(MODE_KEY,degreeMode);
        outState.putInt(CURSOR_KEY, cursor);
        outState.putString(EXP_KEY, exp.toString());
        outState.putBoolean(STATE_KEY, evaluated);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore the full screen / immerse mode.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT < 19){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save the history in file when stop.
        StringBuilder historyContent = new StringBuilder();
        for (Map<String,String> m : historyList) {
            historyContent.append(m.get(HIS_EXP_KEY));
            historyContent.append(',');
            historyContent.append(m.get(HIS_VAL_KEY));
            historyContent.append('\n');
        }
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(HISTORY_FILE, Context.MODE_PRIVATE);
            fos.write(historyContent.toString().trim().getBytes());
        } catch (IOException e) {
            Toast.makeText(this,"Failed saving history.",Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                Toast.makeText(this, "Failed saving history.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // This method clears the history.
    public void clearHistory(View view) {
        historyList.clear();
        historyAdapter.notifyDataSetChanged();
    }

    // This method enables / disables the history view.
    public void toppleHistoryView(View view) {
        if (historyView.getVisibility() == View.VISIBLE)
            historyView.setVisibility(View.GONE);
        else
            historyView.setVisibility(View.VISIBLE);
    }

    // This methods changes the mode to degree or radian.
    public void toppleMode(View view) {
        degreeMode = !degreeMode;
        if (degreeMode)
            mode_button.setText(DEG_MODE);
        else
            mode_button.setText(RAD_MODE);
    }

    //typing the values in the text area of the calculator
    public void typeEntry(View view) {
        // Clear the expression if the result is "Infinity" or "NaN".
        if (evaluated && p.matcher(exp.toString()).find())
            clearExp();

        // The work is distributed here.
        switch (view.getId()) {
            case R.id.cancel_button: // Clear the expression
                clearExp();
                updateExpField();
                break;
            case R.id.back_button: // Backspace
                if (cursor > 0) {
                    Element element = getPreviousElement2();
                    if (element == Element.UnaryOperators) {
                        erasePreviousElement();
                    }
                    erasePreviousElement();
                }
                break;
            //-------------------------------------------
            //  For entering numbers into exp_field
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
            // For entering math constants into exp_field
            case R.id.pi_button:
                addMathConst(MathConstant.PI); break;
            case R.id.e_button:
                addMathConst(MathConstant.E); break;
            // -------------------------------------------
            // For adding functions to the expression
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
            // --------------------
            // Adding binary operators to the operands
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
            // Other functions such as: brackets, evaluation ("="), unary minus "+/-" button and dot "."(for decimal points) .
            case R.id.blanket_button:
                addBlanket(); break;
            case R.id.eval_button:
                evaluate(); break;
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
        // Place the cursor to its current position.
        exp_field.setSelection(cursor);
        if (!evaluated)
            partialEvaluate();
    }

    // This method loads the history list from the HISTORY_FILE
    private void initializeHistoryList() {
        historyList.clear();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(openFileInput(HISTORY_FILE)));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty())
                    continue;
                String[] tokens = line.split(",");
                Map<String,String> item = new HashMap<>();
                item.put(HIS_EXP_KEY,tokens[0]);
                item.put(HIS_VAL_KEY,tokens[1]);
                historyList.add(item);
            }
        } catch (IOException e) {
            Toast.makeText(this,"Error loading history file.",Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {}
        }
        historyAdapter.notifyDataSetChanged();
    }

    private void updateHistoryList() {
        // Update the history list by adding the current result,
        // except for the "Infinity" and "NaN" cases.
        if (p.matcher(Double.toString(result)).find())
            return;
        Map<String,String> history = new HashMap<>();
        history.put(HIS_EXP_KEY,exp.toString());
        history.put(HIS_VAL_KEY,Double.toString(result));
        historyList.add(history);
        historyAdapter.notifyDataSetChanged();
    }

    // This method updates the expression field.
    private void updateExpField() {
        exp_field.setText(exp.toString());
    }

    // This method inserts entry string to the expression.
    private void insertEntry(String entry) {
        exp.insert(cursor,entry);
        cursor += entry.length();
        updateExpField();
    }

    // This method inserts entry character to the expression.
    private void insertEntry(char entry) {
        exp.insert(cursor,entry);
        cursor++;
        updateExpField();
    }

    // This method inserts a character to a particular position.
    // It is for the old parser, which is deprecated.
    @Deprecated
    private void insertEntryAt(int position, char entry) {
        exp.insert(position,entry);
        cursor++;
        updateExpField();
    }

    // This method inserts a string to a particular position.
    // The only method that uses it is negate().
    private void insertEntryAt(int position, String entry) {
        exp.insert(position, entry);
        cursor += entry.length();
        updateExpField();
    }

    // This method performs backspace by one character.
    // It is for the old parser, which is deprecated.
    @Deprecated
    private void backSpace() {
        if (cursor == 0)
            return;
        exp.delete(cursor - 1, cursor);
        cursor--;
        updateExpField();
    }

    // This method performs backspace by one character at the position specified.
    // The only method that uses it is negate().
    private void backSpaceAt(int position) {
        if (position <= 0)
            return;
        exp.delete(position-1,position);
        cursor = (cursor >= position) ? cursor-1 : cursor;
        updateExpField();
    }

    // This method clears the expression
    private void clearExp() {
        exp.setLength(0);
        cursor = 0;
    }

    // This method checks if the dot element can be added at the current position.
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

    // This method checks if the input element conforms the grammar
    // at the current position.
    private boolean isEligible(Element element) {
        assert(cursor > 0);
        Rule rule = Rule.getRule(getPreviousElement());
        return rule.obeysRule(element);
    }

    // This method returns the element which ends at the second previous character.
    private Element getPreviousElement2() {
        if (cursor < 2)
            return null;
        int earliestIndex = cursor-1;
        for (int i = cursor-2; i >= 0; i--) {
            Element element = Element.getElement(exp.substring(i, cursor-1));
            if (element != null)
                earliestIndex = i;
        }
        return Element.getElement(exp.substring(earliestIndex, cursor - 1));
    }

    // This method returns the previous element counting from the current position.
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

    // Get the next element counting from the current position.
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

    // This method evaluates the current expression if it's in correct format.
    private void partialEvaluate() {
        if (CalculatorParser.hasCorrectFormat(exp.toString())) {
            try {
                result = CalculatorParser.parse(exp.toString(),degreeMode).evaluate();
                value_field.setText(Double.toString(result));
            } catch (InvalidComputationException e) {

            }
        } else {
            value_field.setText("");
        }
    }

    // This method performs the housekeeping functionality,
    // e.g. updating history, showing the result, etc,
    // when the "=" button is pressed.
    private void evaluate() {
        if (CalculatorParser.hasCorrectFormat(exp.toString())) {
            updateHistoryList();
            clearExp();
            exp.append(result);
            cursor = exp.length();
            updateExpField();
            value_field.setText("");
            evaluated = true;
        } else {
            Toast.makeText(this, "Invalid format",Toast.LENGTH_SHORT).show();
        }
    }

    // This method erases the next element, only used by editing binary operators.
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

    // This method erases the previous element, and serves as the backspace.
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

    // This method adds an open/close blanket depending on the situation.
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

    // This method adds a unary operator to the expression.
    private void addUnaryOperator(UnaryOperator op) {
        Rule prevRule = Rule.getRule(getPreviousElement());
        if (!prevRule.obeysRule(Element.UnaryOperators))
            return;
        insertEntry(op.symbol+"(");
        evaluated = false;
    }

    // This method adds a binary operator to the expression.
    // It detects if there is a binary operator ahead, and changes it to
    // the specified one here if present.
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

    // This method adds the reciprocal. A multiplication operator is added
    // before it if there is a number before.
    private void addRecip() {
        if (evaluated) clearExp();
        evaluated = false;
        if (isEligible(Element.BinaryOperators))
            insertEntry(BinaryOperator.MULT.symbol + Number.ONE.symbol
                    + BinaryOperator.DIV.symbol + "(");
        else if (isEligible(Element.MathConst))
            insertEntry(Number.ONE.symbol + BinaryOperator.DIV.symbol + "(");
    }

    // This method adds a number to the expression.
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

    // This method adds a math constant. If a number is present before the current position,
    // then a multiplication binary operator would be added as well.
    private void addMathConst(MathConstant mathConstant) {
        if (evaluated) clearExp();
        evaluated = false;
        Element nextEle = getNextElement();
        Rule rule = Rule.MathConst;
        Rule binRule = Rule.BinaryOperators;
        if (isEligible(Element.BinaryOperators) && (rule.obeysRule(nextEle) || nextEle == Element.Null))
            insertEntry(BinaryOperator.MULT.symbol + mathConstant.symbol);
        else if (isEligible(Element.MathConst) && binRule.obeysRule(nextEle))
            insertEntry(mathConstant.symbol + BinaryOperator.MULT.symbol);
        else if (isEligible(Element.MathConst) && nextEle == Element.Null)
            insertEntry(mathConstant.symbol);
    }

    // This method detects if there is already a negation sign before the number.
    // It adds the negate sign if there is none, and removes it if there is one.
    private void negate() {
        evaluated = false;
        boolean negateDetected = false;
        int searchCursor = 0;
        char prevChar = (cursor > 0) ? exp.charAt(cursor-1) : 0;
        // Scan from the cursor position backward to the beginning to find negation operator.
        for (int i = cursor-1; i >= 0; i--) {
            char currChar = exp.charAt(i);
            Element currEle = Element.getElement(currChar);

            if (negateDetected) {
                if (currEle != Element.OpenBlanket) {
                    negateDetected = false;
                    searchCursor = i+2;
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
                if (i == 0) {
                    backSpaceAt(1);
                    return;
                }
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
