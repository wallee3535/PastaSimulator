package com.wallee3535.pastasimulator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Walter on 7/16/2016.
 */
public class EditDish extends AppCompatActivity {
    //request code for SelectPastaType and EnterPastaQuantity
    private static final int REQ_CODE_PASTA_TYPE = 1;
    private static final int REQ_CODE_PASTA_QUANTITY = 2;
    //TextViews for pasta type and pasta quantity
    private TextView typeTextView;
    private TextView quantityTextView;
    //keep track of data to pass back to MainActivity
    private String selectedType;
    private double enteredAmount;
    private String enteredUnit;
    //keep track of mode, add or edit
    private String mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_dish);
        //get references to TextViews
        typeTextView = (TextView) findViewById(R.id.typeTextView);
        quantityTextView = (TextView) findViewById(R.id.quantityTextView);

        mode = getIntent().getExtras().getString("mode");
        //update TextViews according to mode
        if(mode.equals("edit")){
            selectedType = getIntent().getExtras().getString("prevType");
            enteredAmount = getIntent().getExtras().getDouble("prevAmount");
            enteredUnit = getIntent().getExtras().getString("prevUnit");

            typeTextView.setText(selectedType);
            quantityTextView.setText(enteredAmount + " " + enteredUnit);
        }else if(mode.equals("add")){
            selectedType = "";
            enteredAmount = 0;
            enteredUnit = "";
        }
    }

    /** Starts the SelectPastaType activity
     * @param view
     */
    public void whatTypeOfPastaClicked(View view) {

        Intent child = new Intent(this, SelectPastaType.class);
        child.putExtra("selectedType", selectedType);

        startActivityForResult(child, REQ_CODE_PASTA_TYPE);

    }

    /** Starts the EnterQuantity activity
     * @param view
     */
    public void howMuchClicked(View view) {
        Intent child = new Intent(this, EnterQuantity.class);
        child.putExtra("enteredAmount" , enteredAmount);
        child.putExtra("enteredUnit", enteredUnit);
        startActivityForResult(child, REQ_CODE_PASTA_QUANTITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_CANCELED){
            return;
        }
        if (requestCode == REQ_CODE_PASTA_TYPE) {
            try {
                selectedType = (String) data.getExtras().getSerializable("pastaType");

                typeTextView.setText(selectedType);

            } catch (NullPointerException e) {
                Log.e(Globals.TAG, "couldnt get extra from select pasta");

            }
        } else if (requestCode == REQ_CODE_PASTA_QUANTITY) {
            try {
                enteredAmount = (double) data.getExtras().get("pastaAmount");
                enteredUnit = (String) data.getExtras().get("pastaUnit");

                quantityTextView.setText(enteredAmount + " " + enteredUnit);

            } catch (NullPointerException e) {
                Log.e(Globals.TAG, "couldnt get extra from enter quantity");

            }
        }
    }

    /** sets result as RESULT_CANCELED and finishes the activity
     * @param view
     */
    public void cancelButtonClicked(View view){
        setResult(RESULT_CANCELED, null);
        finish();
    }

    /** sets result as RESULT_OK, passes extra data, and finishes the activity
     * @param view
     */
    public void saveButtonClicked(View view) {
        if (selectedType.equals("")) {
            Log.e(Globals.TAG, "tried to save the dish without type (possibly also without quantity) entered");
            Toast.makeText(getApplicationContext(), "must enter pasta type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (enteredUnit.equals("")) {
            Log.e(Globals.TAG, "tried to save the dish without quantity entered");
            Toast.makeText(getApplicationContext(), "must enter pasta unit", Toast.LENGTH_SHORT).show();
            return;
        }
        if (enteredAmount<0) {
            Log.e(Globals.TAG, "tried to save the dish without amount entered");
            Toast.makeText(getApplicationContext(), "must enter nonzero pasta amount", Toast.LENGTH_SHORT).show();
            return;
        }

        //pass data and return to MainActivity
        Intent intent = new Intent();
        intent.putExtra("selectedType", selectedType);
        intent.putExtra("enteredAmount", enteredAmount);
        intent.putExtra("enteredUnit", enteredUnit);
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        finish();
    }


}
