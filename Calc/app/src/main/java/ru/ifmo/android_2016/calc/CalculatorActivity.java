package ru.ifmo.android_2016.calc;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public final class CalculatorActivity extends Activity implements View.OnClickListener {

    public static final String KEY_FIRST_VALUE = "firstValue";
    public static final String KEY_SECOND_VALUE = "secondValue";
    public static final String KEY_RESULT = "result";
    public static final String KEY_NEED_TO_UPDATE = "needToUpdate";
    public static final String KEY_OPER = "oper";
    public static final String KEY_IS_ERROR = "isError";
    public static final String KEY_TEXT = "text";
    public static final String KEY_EMPTY_RESULT = "emptyResult";
    public static final String KEY_EMPTY_TEXT = "emptyText";
    public static final String KEY_EMPTY_FIRST = "emptyFirst";
    public static final String KEY_HAS_OPER = "hasOper";
    public static final String KEY_NEED_TO_CLEAN = "needToClean";

    Button[] d = new Button[10];
    Button add, sub, mul, div, clear, equal;
    TextView tv_input, tv_result;
    int firstValue;
    int secondValue;
    double result;
    String text;
    boolean needToUpdate;
    boolean isError;
    boolean emptyResult;
    boolean emptyText;
    boolean hasOper;
    boolean emptyFirst;
    boolean needToClean;
    char oper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        d[0] = (Button) findViewById(R.id.d0);
        d[1] = (Button) findViewById(R.id.d1);
        d[2] = (Button) findViewById(R.id.d2);
        d[3] = (Button) findViewById(R.id.d3);
        d[4] = (Button) findViewById(R.id.d4);
        d[5] = (Button) findViewById(R.id.d5);
        d[6] = (Button) findViewById(R.id.d6);
        d[7] = (Button) findViewById(R.id.d7);
        d[8] = (Button) findViewById(R.id.d8);
        d[9] = (Button) findViewById(R.id.d9);
        add = (Button) findViewById(R.id.add);
        sub = (Button) findViewById(R.id.sub);
        mul = (Button) findViewById(R.id.mul);
        div = (Button) findViewById(R.id.div);
        clear = (Button) findViewById(R.id.clear);
        equal = (Button) findViewById(R.id.eqv);
        tv_input = (TextView) findViewById(R.id.tv_input);
        tv_result = (TextView) findViewById(R.id.tv_result);

        for (int i = 0; i < 10; i++) {
            d[i].setOnClickListener(this);
        }
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        mul.setOnClickListener(this);
        div.setOnClickListener(this);
        clear.setOnClickListener(this);
        equal.setOnClickListener(this);

        firstValue = 0;
        secondValue = 0;
        result = 0;
        needToUpdate = true;
        needToClean = true;
        oper = ' ';
        isError = false;
        text = "";
        emptyResult = true;
        emptyText = true;
        hasOper = false;
        emptyFirst = true;


        if (savedInstanceState != null) {
            firstValue = savedInstanceState.getInt(KEY_FIRST_VALUE);
            secondValue = savedInstanceState.getInt(KEY_SECOND_VALUE);
            result = savedInstanceState.getDouble(KEY_RESULT);
            text = savedInstanceState.getString(KEY_TEXT);
            needToUpdate = savedInstanceState.getBoolean(KEY_NEED_TO_UPDATE);
            isError = savedInstanceState.getBoolean(KEY_IS_ERROR);
            emptyResult = savedInstanceState.getBoolean(KEY_EMPTY_RESULT);
            emptyText = savedInstanceState.getBoolean(KEY_EMPTY_TEXT);
            emptyFirst = savedInstanceState.getBoolean(KEY_EMPTY_FIRST);
            hasOper = savedInstanceState.getBoolean(KEY_HAS_OPER);
            oper = savedInstanceState.getChar(KEY_OPER);
            needToClean = savedInstanceState.getBoolean(KEY_NEED_TO_CLEAN);
        }

        update();
    }

    @Override
    public void onClick(View v) {
        if (needToClean) {
            clean();
            needToClean = false;
        }
        emptyResult = false;
        for (int i = 0; i < 10; i++) {
            if (v == d[i]) {
                if (firstValue < 1e8) {
                    firstValue = firstValue * 10 + i;
                    text += Integer.toString(i);
                }
                emptyText = false;
                update();
                emptyFirst = false;
                return;
            }

        }
        if (emptyFirst) {
            update();
            return;
        }
        if (v == add && !hasOper) {
            hasOper = true;
            secondValue = firstValue;
            firstValue = 0;
            emptyFirst = true;
            text += '+';
            oper = '+';
            update();
            return;
        }
        if (v == sub && !hasOper) {
            hasOper = true;
            secondValue = firstValue;
            firstValue = 0;
            emptyFirst = true;
            text += '-';
            oper = '-';
            update();
            return;
        }
        if (v == mul && !hasOper) {
            hasOper = true;
            secondValue = firstValue;
            firstValue = 0;
            emptyFirst = true;
            text += '*';
            oper = '*';
            update();
            return;
        }
        if (v == div && !hasOper) {
            hasOper = true;
            secondValue = firstValue;
            firstValue = 0;
            emptyFirst = true;
            text += '/';
            oper = '/';
            update();
            return;
        }
        if (v == equal || (hasOper && isArithmOper(v) && emptyFirst)) {
            if (oper == '+') {
                result = (double)secondValue + firstValue;
            }
            if (oper == '-') {
                result = (double)secondValue - firstValue;
            }
            if (oper == '*') {
                result = (double)secondValue * firstValue;
            }
            if (oper == '/') {
                if (firstValue == 0) {
                    isError = true;
                    update();
                    return;
                }
                result = (double)secondValue / firstValue;
            }
            update();
            needToClean = true;
            return;
        }
        if (v == clear) {
            clean();
            update();
            return;
        }
    }

    private void clean() {
        firstValue = 0;
        secondValue = 0;
        emptyResult = true;
        result = 0;
        emptyText = true;
        oper = ' ';
        isError = false;
        text = "";
        hasOper = false;
        emptyFirst = true;
    }
    private String printResult(double a) {
        double temp = (double)((int) a);
        if (temp == a) {
            return Integer.toString((int) a);
        } else {
            return Double.toString(a);
        }
    }

    private void update() {
        if (isError) {
            isError = false;
            tv_result.setText("ERROR");
            return;
        }
        if (!emptyResult) {
            tv_result.setText(printResult(result));
        } else {
            tv_result.setText("");
        }
        if (!emptyText) {
            tv_input.setText(text);
        } else {
            tv_input.setText("");
        }
        return;
    }
    private boolean isArithmOper(View v) {
        boolean flag = false;
        if (v == add) flag = true;
        if (v == sub) flag = true;
        if (v == mul) flag = true;
        if (v == div) flag = true;
        return flag;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_FIRST_VALUE, firstValue);
        outState.putInt(KEY_SECOND_VALUE, secondValue);
        outState.putDouble(KEY_RESULT, result);
        outState.putString(KEY_TEXT, text);
        outState.putBoolean(KEY_NEED_TO_UPDATE, needToUpdate);
        outState.putBoolean(KEY_IS_ERROR, isError);
        outState.putBoolean(KEY_EMPTY_RESULT, emptyResult);
        outState.putBoolean(KEY_EMPTY_TEXT, emptyText);
        outState.putBoolean(KEY_EMPTY_FIRST, emptyFirst);
        outState.putBoolean(KEY_HAS_OPER, hasOper);
        outState.putChar(KEY_OPER, oper);
        outState.putBoolean(KEY_NEED_TO_CLEAN, needToClean);
    }
}


