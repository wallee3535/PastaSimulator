package com.wallee3535.pastasimulator;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Walter on 7/16/2016.
 * Prompts the user to enter a quantity of pasta
 */
public class EnterQuantity extends AppCompatActivity {
    //display amount and type of unit entered by user
    private TextView amountEditText;
    private RadioGroup unitRadioGroup;

    //saved data to return to EditDish activity
    private double enteredAmount;
    private String enteredUnit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_quantity);

        amountEditText = (EditText) findViewById(R.id.amountEditText);
        unitRadioGroup = (RadioGroup) findViewById(R.id.unitRadioGroup);

        //get extras from EditDish
        enteredAmount = getIntent().getExtras().getDouble("enteredAmount");
        enteredUnit = getIntent().getExtras().getString("enteredUnit");

        //set Views to show extras
        if (enteredAmount > 0) amountEditText.setText(Double.toString(enteredAmount));
        if (!enteredUnit.isEmpty()) {
            for(int i = 0 ; i< unitRadioGroup.getChildCount(); i++){
                RadioButton r = (RadioButton)unitRadioGroup.getChildAt(i);
                if(r.getText().toString().equals(enteredUnit)){
                    r.setChecked(true);
                    break;
                }
            }
        }
    }

    /** Checks for valid input, saves data, and finishes activity
     * @param view
     */
    public void saveButtonClicked(View view) {

        try {
            enteredAmount = Double.parseDouble(amountEditText.getText().toString());
            //entered amount must be greater than zero
            if(enteredAmount == 0){
                Log.e(Globals.TAG, "tried to enter 0 as amount");
                Toast.makeText(getApplicationContext(), "must enter a nonzero amount", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {//for empty string
            Log.e(Globals.TAG, "tried to enter empty string as amount");
            Toast.makeText(getApplicationContext(), "must enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            //some unit must be selected
            int id = unitRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = (RadioButton) unitRadioGroup.findViewById(id);
            enteredUnit = selectedRadioButton.getText().toString();
        } catch (NullPointerException e) {
            Log.e(Globals.TAG, "save clicked without selecting unit");
            Toast.makeText(getApplicationContext(), "must enter a unit", Toast.LENGTH_SHORT).show();
            return;
        }

        //save data and finish activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("pastaAmount", enteredAmount);
        resultIntent.putExtra("pastaUnit", enteredUnit);
        setResult(RESULT_OK, resultIntent);
        finish();

    }

    public void onBackClicked(View view){
        setResult(RESULT_CANCELED, null);
        finish();
    }

    /**
     * sets result to RESULT_CANCELED and finishes activity
     */
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        finish();
    }

}
