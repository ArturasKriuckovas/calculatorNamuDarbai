package com.example.arturas.calculatornamudarbai;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static String[] allSymbols = {"+", "-", "/", "*","(", ")"};
    public static String[] operators = {"+", "-", "/", "*"};
    TextView resultDisplay;
    String[] symbols = {"/", "-", "+", "*"};
    String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."};
    ArrayList<String> symbolsArrayList = new ArrayList<>(Arrays.asList(symbols));
    ArrayList<String> numbersArrayList = new ArrayList<>(Arrays.asList(numbers));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultDisplay = findViewById(R.id.numbersDisplayer);
        int[]buttons = {
                R.id.zero, R.id.one,
                R.id.two, R.id.three,
                R.id.four, R.id.five,
                R.id.six, R.id.seven,
                R.id.eight, R.id.nine,
                R.id.clear, R.id.leftBracket,
                R.id.rightBracket, R.id.root,
                R.id.deleteLast, R.id.subtract,
                R.id.dot, R.id.equals,
                R.id.minus, R.id.multiply,
                R.id.plus
        };
        for (int id : buttons){
            Button button = findViewById(id);
            button.setOnClickListener(this);
        }

    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        String oldValue = resultDisplay.getText().toString();
        switch (v.getId()){
            case R.id.zero:
                resultDisplay.setText(oldValue + "0");
                break;
            case R.id.one:
                resultDisplay.setText(oldValue + "1");
                break;
            case R.id.two:
                resultDisplay.setText(oldValue + "2");
                break;
            case R.id.three:
                resultDisplay.setText(oldValue + "3");
                break;
            case  R.id.four:
                resultDisplay.setText(oldValue + "4");
                break;
            case R.id.five:
                resultDisplay.setText(oldValue + "5");
                break;
            case R.id.six:
                resultDisplay.setText(oldValue + "6");
                break;
            case R.id.seven:
                resultDisplay.setText(oldValue + "7");
                break;
            case R.id.eight:
                resultDisplay.setText(oldValue + "8");
                break;
            case R.id.nine:
                resultDisplay.setText(oldValue + "9");
                break;
            case R.id.dot:
                resultDisplay.setText(validateInput(oldValue,"."));
                break;
            case R.id.leftBracket:
                resultDisplay.setText(validateInput(oldValue,"("));
                break;
            case R.id.rightBracket:
                resultDisplay.setText(validateInput(oldValue,")"));
                break;
            case R.id.root:
                resultDisplay.setText(oldValue + "√");
                break;
            case R.id.subtract:
                resultDisplay.setText(oldValue + "/");
                break;
            case R.id.minus:
                resultDisplay.setText(oldValue + "-");
                break;
            case R.id.plus:
                resultDisplay.setText(oldValue + "+");
                break;
            case R.id.multiply:
                resultDisplay.setText(oldValue + "*");
                break;
            case R.id.deleteLast:
                resultDisplay.setText(oldValue.equals("") ? "" :
                        oldValue.substring(0, oldValue.length() - 1));
                break;
            case R.id.clear:
                resultDisplay.setText("");
                break;
            case R.id.equals:
                resultDisplay.setText(evaluatePostFix(convertToPostfix(getListOfValues(oldValue))));
                break;
            default:
                break;
        }
    }

    public static ArrayList<String> getListOfValues(String expression){
        ArrayList<String> list = new ArrayList<>();
        String tempValue = "";
        for (int i = 0; i < expression.length(); i++){
            String item = Character.toString(expression.charAt(i));
            if(checkIfContains(item, allSymbols)){
                list.add(item);
            } else {
                tempValue += item;
                if(checkIfContains(Character.toString(expression.charAt(i+1)), allSymbols)){
                    list.add(tempValue);
                    tempValue = "";
                }
            }
        }
        return list;
    }

    public static String evaluatePostFix(ArrayList<String> value){
        Stack<String> mStack = new Stack<>();
        for (int i = 0; i < value.size(); i++){
            if(checkIfContains(value.get(i), operators)){
                String right = mStack.pop();
                String left = mStack.pop();
                mStack.push(evaluate(left, right, value.get(i)));
            } else {
                mStack.push(value.get(i));
            }
        }
        return mStack.pop();
    }

    public static String evaluate(String a, String b, String operator){
        BigDecimal left = new BigDecimal(a);
        BigDecimal right = new BigDecimal(b);
        BigDecimal result = new BigDecimal(0);
        switch (operator) {
            case "+":
                result = left.add(right);
                break;
            case "-":
                result = left.subtract(right);
                break;
            case "*":
                result = left.multiply(right);
                break;
            case "/":
                result = left.divide(right);
                break;
            default: break;
        }
        return String.valueOf(result);
    }

    static boolean checkIfContains(String c, String[] array) {
        for (String x : array) {
            if (x.equals(c)) {
                return true;
            }
        }
        return false;
    }
    static int getLevelOfOperator(String operator){
        int value = 0;
        if (operator.equals("+") || operator.equals("-")){
            value = 1;
        } else if (operator.equals("*") || operator.equals("/")){
            value = 2;
        }
        return value;

    }

    static ArrayList<String> convertToPostfix(ArrayList<String> infix){
        Stack<String> mStack = new Stack<>();
        ArrayList<String> list = new ArrayList<>();
        String postFix = "";
        for (int i = 0; i < infix.size(); i++){
            String item = infix.get(i);

            boolean isOperator = checkIfContains(item, operators);
            if(!isOperator && !item.equals("(") && !item.equals(")")){
                list.add(item);
            } else if (item.equals("(")){
                mStack.push(item);
            } else if (item.equals(")")){
                while(!mStack.isEmpty() && !mStack.peek().equals("(")){
                    list.add(mStack.pop());
                }
                mStack.pop();
            } else if (isOperator){
                if (mStack.empty() || mStack.peek().equals("(")){
                    mStack.push(item);
                } else {
                    while(!mStack.isEmpty() && !mStack.peek().equals("(")
                            && getLevelOfOperator(item) <= getLevelOfOperator(mStack.peek())) {
                        list.add(mStack.pop());
                    }
                    mStack.push(item);
                }
            }
        }
        while (!mStack.isEmpty()){
            list.add(mStack.pop());
        }
        return list;

    }

    String validateInput (String oldValue,String newEntry){
        // TODO need to update validation
        String result = oldValue;
        String[] separatedValues = resultDisplay.getText().toString().split(Arrays.toString(operators));
        String lastValue = separatedValues.length > 0 ? separatedValues[separatedValues.length - 1] : "";
        String lastSymbol = lastValue.length() == 0 ? "" : result.substring(result.length() - 1);
        Boolean lastCharIsOperator = false;
        for (String s: symbolsArrayList){
            if(s.equals(result.substring(result.length() == 0 ? 0: result.length() - 1))){
                lastCharIsOperator = true;
                break;
            }
        }
        if (newEntry.equals(".")){
            result =  (lastValue.split("\\.").length > 1 ||
                        resultDisplay.getText().toString().equals("") || lastCharIsOperator)
                        ? resultDisplay.getText().toString()
                        : resultDisplay.getText().toString() + newEntry;
        } else if (newEntry.equals(")") || newEntry.equals("(")){
            if(lastValue.equals("")){
                result = newEntry.equals("(") ? newEntry : "";
            } else  {
                result = ((symbolsArrayList.contains(lastSymbol) && newEntry.equals("(")) ||
                        (!symbolsArrayList.contains(lastSymbol) && newEntry.equals(")")))
                        ? resultDisplay.getText().toString() + newEntry : resultDisplay.getText().toString();
            }
        }
        return result;
    }

}
