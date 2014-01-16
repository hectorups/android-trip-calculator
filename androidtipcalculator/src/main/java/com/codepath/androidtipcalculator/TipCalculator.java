package com.codepath.androidtipcalculator;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Locale;

public class TipCalculator extends Activity {

    private EditText mEtTipInput;
    private Button mBtn20Percent;
    private Button mBtn15Percent;
    private Button mBtn10Percent;
    private TextView mTvOverallValue;
    private TextView mTvFinalTipValue;
    private Button mBtnClearInput;

    private int mCurrentTipPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        mEtTipInput = (EditText)findViewById(R.id.etTipInput);
        mBtn10Percent = (Button) findViewById(R.id.btn10percent);
        mBtn15Percent = (Button) findViewById(R.id.btn15Percent);
        mBtn20Percent = (Button) findViewById(R.id.btn20Percent);
        mTvFinalTipValue = (TextView) findViewById(R.id.tvFinalTipValue);
        mTvOverallValue = (TextView) findViewById(R.id.tvOverallValue);
        mBtnClearInput = (Button) findViewById(R.id.btnClearInput);

        mBtn10Percent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipBtnClick(10);
            }
        });

        mBtn15Percent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipBtnClick(15);
            }
        });

        mBtn20Percent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipBtnClick(20);
            }
        });

        mBtnClearInput.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearUi();
            }
        });

        mEtTipInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(mCurrentTipPercentage > 0 && isValidCost()) {
                    tipBtnClick(mCurrentTipPercentage);
                }
            }
        });

    }

    private float calculateTip(){
        float float_percentage = new Float(mCurrentTipPercentage);
        return getCost() * (float_percentage/100);
    }

    private void updateUi( float tip ){
        float roundedTip = round(tip, 2);

        mTvFinalTipValue.setText( formatPrice(roundedTip) );

        mTvOverallValue.setText( formatPrice(getCost() + roundedTip) );
    }

    private String formatPrice(float value){
        return String.format(Locale.US, "$ \t%.2f", value);
    }

    private void tipBtnClick( int percentage ){
        mCurrentTipPercentage = percentage;
        if(!isValidCost()){
            showInvalidPriceMsg();
            return;
        }

        float tip = calculateTip();
        updateUi( tip );
    }

    private float round(float valueToRound, int numDecimalPlaces) {
        BigDecimal bd = new BigDecimal(Float.toString(valueToRound));
        bd = bd.setScale(numDecimalPlaces, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private float getCost(){
        String costString = mEtTipInput.getText().toString();
        float result = 0;
        try{
            result = Float.parseFloat(costString);
        } catch(NumberFormatException e){}

        return result;
    }

    private void clearUi(){
        mTvFinalTipValue.setText("");
        mEtTipInput.setText("");
        mTvOverallValue.setText("");
    }

    private void showInvalidPriceMsg(){
        Context context = getApplicationContext();
        CharSequence text = getResources().getString(R.string.tvInvalidValueEnteredString);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private boolean isValidCost(){
        return getCost() > 0;
    }

}
