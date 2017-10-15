package com.muledog.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
{
    private TextView _screen;
    private String display = "";
    private String currentOperator = "";
    private String result = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _screen = (TextView) findViewById(R.id.textView);
        _screen.setText(display);
    }

    private void updateScreen()
    {
        _screen.setText(display);
    }

    public void onClickNumber(View v)
    {
        if (result != "")
        {
            clear();
            updateScreen();
        }
        Button b = (Button) v;
        display += b.getText();
        updateScreen();
    }

    private void clear()
    {
        display = "";
        currentOperator = "";
        result = "";
    }

    private boolean isOperator(char op)
    {
        switch (op)
        {
            case '+':
            case '-':
            case 'x':
            case '÷':
                return true;
            default:
                return false;
        }
    }

    public void onClickClear(View v)
    {
        clear();
        updateScreen();
    }

    public void onClickMinus(View v)
    {
        if (display.length() > 0)
        {

            if (currentOperator != "")
            {
                String[] operation = display.split(Pattern.quote(currentOperator));
                if (operation.length == 1)
                {
                    display = (operation[0] + currentOperator + "-");
                    updateScreen();
                    return;
                } else if (Objects.equals(operation[1].substring(0, 1), "-"))
                {
                    display = (operation[0] + currentOperator + operation[1].substring(1));
                    updateScreen();
                } else
                {
                    display = (operation[0] + currentOperator + "-" + operation[1]);
                    updateScreen();
                }
            } else if (Objects.equals(display.substring(0, 1), "-"))
            {
                display = display.substring(1);
                updateScreen();
            } else
            {
                display = "-" + display;
                updateScreen();
            }
        } else
        {
            display = "-";
            updateScreen();
        }
    }

    public void onClickPercent(View v)
    {
        String[] operation = (display.split("%"));
        result = String.valueOf(Double.valueOf(operation[0]) / 100.0);
        _screen.setText(display + "\n" + String.valueOf(result));
    }

    public void onClickOperator(View v)
    {
        if (display == "") return;

        Button b = (Button) v;

        if (result != "")
        {
            String _display = result;
            clear();
            display = _display;
        }

        if (currentOperator != "")
        {
            Log.d("CalcX", "" + display.charAt(display.length() - 1));
            if (isOperator(display.charAt(display.length() - 1)))
            {
                display = display.replace(display.charAt(display.length() - 1), b.getText().charAt(0));
                updateScreen();
                return;
            } else
            {
                getResult();
                display = result;
                result = "";
            }
            currentOperator = b.getText().toString();
        }
        display += b.getText();
        currentOperator = b.getText().toString();
        updateScreen();
    }

    private double operate(String a, String b, String op)
    {
        switch (op)
        {
            case "+":
                return Double.valueOf(a) + Double.valueOf(b);
            case "-":
                return Double.valueOf(a) - Double.valueOf(b);
            case "X":
                return Double.valueOf(a) * Double.valueOf(b);
            case "÷":
                try
                {
                    return Double.valueOf(a) / Double.valueOf(b);
                } catch (Exception e)
                {
                    Log.d("Calc", e.getMessage());
                }
            default:
                return -1;
        }
    }

    private boolean getResult()
    {
        if (currentOperator == "") return false;
        String[] operation = display.split(Pattern.quote(currentOperator), 2);
        if (operation.length == 1) return false;
        try
        {
            result = String.valueOf(operate(operation[0], operation[1], currentOperator));
        } catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public void onClickEqual(View v)
    {
        if (display == "") return;
        if (!getResult()) return;
        _screen.setText(display + "\n" + String.valueOf(result));
    }
}
