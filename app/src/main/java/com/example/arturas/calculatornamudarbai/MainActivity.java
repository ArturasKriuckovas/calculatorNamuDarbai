package com.example.arturas.calculatornamudarbai;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
                resultDisplay.setText(equals(oldValue));
                break;
            default:
                break;
        }
    }

    String validateInput (String oldValue,String newEntry){
        String result = oldValue;
        String[] separatedValues = resultDisplay.getText().toString().split(Arrays.toString(symbols));
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

    String equals(String item){
        ArrayList<String> listWithoutBrackets = new ArrayList<>();
        String[] mathExpressions = item.split("[\\(||\\)]");
        for(String value: mathExpressions){
            if(!value.equals("") && value.length() > 0){
                String[] listOfValueChar = value.split("");
                String first = listOfValueChar[1];
                String last = listOfValueChar[listOfValueChar.length - 1];
                if(numbersArrayList.contains(first) && numbersArrayList.contains(last)){
                    listWithoutBrackets.add(value);
                } else {
                    listWithoutBrackets.addAll(Arrays.asList(value.split("")));
                }
            }
            listWithoutBrackets.removeAll(Arrays.asList(""));
        }
        String expression = TextUtils.join("", getRidOfSqrt(calculateInsideBrackets(listWithoutBrackets)));
        return evaluate(expression);
    }

    ArrayList<String> getRidOfSqrt(ArrayList<String> items){
        ArrayList<String> result = new ArrayList<>();
        for(Integer i = 0; i < items.size(); i++){
            if(items.get(i).equals("√")){
                Double item = Double.parseDouble(items.get(i+1));
                result.add(String.valueOf(Math.sqrt(item)));
                i++;
            } else {
                result.add(items.get(i));
            }
        }
        result.removeAll(Arrays.asList(""));
        return result;
    }

    ArrayList<String> calculateInsideBrackets(ArrayList<String> items){
        ArrayList<String> list = new ArrayList<>();
        for (String s: items){
            Boolean contains = false;
            for(String item : symbolsArrayList){
               if(s.contains(item)){
                   contains = s.contains(item);
                   break;
               }
            }
            if(s.length() > 1 && contains && !s.contains("√")){
                list.add(evaluate(s));
            } else {
                list.addAll(Arrays.asList(s.split("")));
            }
        }
        list.removeAll(Arrays.asList(""));
        return list;
    }

    String evaluate(String s){
        // not mine: https://www.sanfoundry.com/java-program-implement-evaluate-expression-using-stacks/
        Stack<Integer> op  = new Stack<Integer>();
        Stack<Double> val = new Stack<Double>();
        Stack<Integer> optmp  = new Stack<Integer>();
        Stack<Double> valtmp = new Stack<Double>();
        String input = s;
        input = input.replaceAll("-","+-");
        /* Store operands and operators in respective stacks */
        String temp = "";
        for (int i = 0;i < input.length();i++)
        {
            char ch = input.charAt(i);
            if (ch == '-')
                temp = "-" + temp;
            else if (ch != '+' &&  ch != '*' && ch != '/')
                temp = temp + ch;
            else
            {
                val.push(Double.parseDouble(temp));
                op.push((int)ch);
                temp = "";
            }
        }
        val.push(Double.parseDouble(temp));
        /* Create char array of operators as per precedence */
        /* -ve sign is already taken care of while storing */
        char operators[] = {'/','*','+'};
        /* Evaluation of expression */
        for (int i = 0; i < 3; i++)
        {
            boolean it = false;
            while (!op.isEmpty())
            {
                int optr = op.pop();
                double v1 = val.pop();
                double v2 = val.pop();
                if (optr == operators[i])
                {
                    /* if operator matches evaluate and store in temporary stack */
                    if (i == 0)
                    {
                        valtmp.push(v2 / v1);
                        it = true;
                        break;
                    }
                    else if (i == 1)
                    {
                        valtmp.push(v2 * v1);
                        it = true;
                        break;
                    }
                    else if (i == 2)
                    {
                        valtmp.push(v2 + v1);
                        it = true;
                        break;
                    }

                }
                else
                {
                    valtmp.push(v1);
                    val.push(v2);
                    optmp.push(optr);
                }
            }
            /* Push back all elements from temporary stacks to main stacks */
            while (!valtmp.isEmpty())
                val.push(valtmp.pop());
            while (!optmp.isEmpty())
                op.push(optmp.pop());
            if (it)
                i--;
        }
        return val.pop().toString();
    }
}
