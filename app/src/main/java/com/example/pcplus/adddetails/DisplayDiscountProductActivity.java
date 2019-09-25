package com.example.pcplus.adddetails;

import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DisplayDiscountProductActivity extends AppCompatActivity {

    ListView custViewListView;
    ArrayList<DiscountModel> pdList;
    DiscountCustomrViewAdapter customrViewAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_discount_product);
        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Our Offers");

            //to enable back button in actionbar set parent activity main avtivity in manifest

            //for get system date
            String currentDateAndtime =getMonthNumber();

            custViewListView = findViewById(R.id.custViewListView);
            pdList = new ArrayList<DiscountModel>();
            SQLiteHelper sqLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);
            customrViewAdapter = new DiscountCustomrViewAdapter(this, R.layout.customer_view_row, pdList, sqLiteHelper);
            custViewListView.setAdapter((ListAdapter) customrViewAdapter);

            Cursor cursor = sqLiteHelper.getCustomerViewDetails(currentDateAndtime);
            pdList.clear();
            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                String pid = cursor.getString(1);
                String discount = cursor.getString(2);
                String month = cursor.getString(3);
                //add to list
                pdList.add(new DiscountModel(id, pid, discount, month));
            }
            customrViewAdapter.notifyDataSetChanged();
            if(pdList.size()==0){

                //if there is no record in table of database which means listview is empty
                Toast.makeText(this,"No discount product found...", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private String getMonthNumber(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        int monthNumber = Integer.parseInt(sdf.format(new Date()));
        return String.valueOf(monthNumber);
    }
}
