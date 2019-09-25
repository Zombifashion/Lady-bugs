package com.example.pcplus.adddetails;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddDiscounts extends AppCompatActivity {

    EditText prId, prDiscount;

    Spinner month;

    Button btnAdd, btnList, btnOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discounts);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add new discount");

        //for drop down list
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Months, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
//        spinner.setSelection(5);


        prId = findViewById(R.id.edtProductId);
        prDiscount = findViewById(R.id.edtDiscount);
        month = findViewById(R.id.spinner);
        btnAdd = findViewById(R.id.btnAdd);
        btnList = findViewById(R.id.btnList);
        btnOffers = findViewById(R.id.btnOffers);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int monthNo = getMonthNumber(month.getSelectedItem().toString());
                boolean isValid = true;

                if (prId.getText().toString().isEmpty()) {
                    prId.setError(getString(R.string.Product_Id_validation_massage));
                    isValid = false;
                }
               /* else {
                    productIdEditText.setErrorEnabled(false);
                }*/

                if (prDiscount.getText().toString().isEmpty()) {
                    prDiscount.setError(getString(R.string.Discount_validation_massage));
                    isValid = false;
                }
                /*else {
                    discountEdittext.setErrorEnabled(false);
                }*/

                if (isValid) {

                    try {
                        MainActivity.mSQLiteHelper.insertDiscountDetails(
                                prId.getText().toString(),
                                prDiscount.getText().toString(),
                               // month.getSelectedItem().toString()
                                monthNo


                        );
                        Toast.makeText(AddDiscounts.this, "Stored successfully", Toast.LENGTH_SHORT).show();
                        //reset views
                        prId.setText("");
                        prDiscount.setText("");





                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(AddDiscounts.this, DiscountListActivity.class));
                } catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        btnOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(AddDiscounts.this, DisplayDiscountProductActivity.class));
                } catch (Exception ex){
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public int getMonthNumber(String month){

        int no = 0;

        switch (month){

            case "January": no = 1;
            break;

            case "February" : no = 2;
            break;

            case "March" : no = 3;
            break;

            case "April" : no = 4;
            break;

            case "May" : no = 5;
            break;

            case "June" : no = 6;
            break;

            case "July" : no = 7;
            break;

            case "August" : no = 8;
            break;

            case "September" : no = 9;
            break;

            case "October" : no = 10;
            break;

            case "November" : no = 11;
            break;

            case "December" : no = 12;
            break;

            default : Toast.makeText(AddDiscounts.this, "Please check  spellings", Toast.LENGTH_SHORT).show();
            break;
        }
        return no;
    }
}
