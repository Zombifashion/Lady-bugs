package com.example.pcplus.adddetails;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DiscountListActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<DiscountModel> mList;
    DiscountListAdapter mAdapter = null;

   // SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_list);

        try {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Discount List");

            //to enable back button in actionbar set parent activity main avtivity in manifest

            mListView = findViewById(R.id.ListView);
            mList = new ArrayList<DiscountModel>();
            mAdapter = new DiscountListAdapter(this, R.layout.discountrow, mList);
            mListView.setAdapter((ListAdapter) mAdapter);

            //get all data from sqlite
            Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM DISCOUNTS");
            mList.clear();
            while (cursor.moveToNext()){

                int id = cursor.getInt(0);
                String pid = cursor.getString(1);
                String discount = cursor.getString(2);
                //String month = cursor.getString(3);
                String month = getMonthName(cursor.getInt(3));
                //add to list
                mList.add(new DiscountModel(id, pid, discount, month));
            }

            mAdapter.notifyDataSetChanged();
            if(mList.size()==0){

                //if there is no record in table of database which means listview is empty
                Toast.makeText(this,"No discount product found...", Toast.LENGTH_SHORT).show();
            }

            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                    //alert dialog to display options of update and delete
                    try  {
                        final CharSequence[] items = {"Update", "Delete"};

                        AlertDialog.Builder dialog = new AlertDialog.Builder(DiscountListActivity.this);

                        dialog.setTitle("Choose an action");
                        dialog.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try{
                                    if (i == 0) {

                                        //update
                                        Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM DISCOUNTS");
                                        ArrayList<Integer> arrID = new ArrayList<Integer>();
                                        while (c.moveToNext()) {

                                            arrID.add(c.getInt(0));

                                        }
                                        //show update dialog
                                        showDialogUpdate(DiscountListActivity.this, arrID.get(position));
                                    }
                                    if (i == 1) {
                                        //delete
                                        Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM DISCOUNTS");
                                        ArrayList<Integer> arrID = new ArrayList<Integer>();
                                        while (c.moveToNext()) {
                                            arrID.add(c.getInt(0));
                                        }
                                        showDialogDelete(arrID.get(position));
                                    }
                                } catch(Exception ex) {
                                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        dialog.show();

                        return true;
                    } catch(Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            });
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void showDialogDelete(final int idDiscount) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(DiscountListActivity.this);
        dialogDelete.setTitle("warning!!");
        dialogDelete.setMessage("Are you sure to delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    MainActivity.mSQLiteHelper.deleteDiscountDetails(idDiscount);
                    Toast.makeText(DiscountListActivity.this,"Delete Successfully", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(DiscountListActivity.this, DiscountListActivity.class));
                }
                catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateDiscountList();
            }
        });
        dialogDelete.setNegativeButton("Cansel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();

    }

    private void showDialogUpdate(final Activity activity, final int position) {

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.discount_update_dialog);
        dialog.setTitle("Update");


        final TextView txtPrId = dialog.findViewById(R.id.edtpid);
        final EditText edtDiscount = dialog.findViewById(R.id.edtdiscount);
        final EditText edtMonth = dialog.findViewById(R.id.edtmonth);

        //final Spinner edtMonth = dialog.findViewById(R.id.spinner);

       /* final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Months, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);*/

        Button btnDiscountUpdate = dialog.findViewById(R.id.btnDiscontUpdate);


        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM DISCOUNTS WHERE id="+position);
        mList.clear();
        while (cursor.moveToNext()){

            int did = cursor.getInt(0);

            String pid = cursor.getString(1);
            txtPrId.setText(pid);

            String discount = cursor.getString(2);
            edtDiscount.setText(discount);

            /*String month = cursor.getString(3);
            edtMonth.setText(month);*/

            String month = getMonthName(cursor.getInt(3));
            edtMonth.setText(month);

            //edtMonth.setSelection(cursor.getInt(3));

            mList.add(new DiscountModel(did, pid, discount, month));
        }



        //set width of dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);

        //set height of dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        btnDiscountUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int updatedMonthNo = getUpdatedMonthNumber( edtMonth.getText().toString());
                    MainActivity.mSQLiteHelper.updateDiscountDetails(
                            txtPrId.getText().toString().trim(),
                            edtDiscount.getText().toString().trim(),
                            //edtMonth.getText().toString(),
                            updatedMonthNo,
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update Successfull", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(DiscountListActivity.this, DiscountListActivity.class));
                }
                catch (Exception error){
                    Log.e("Update Error", error.getMessage());
                }
                updateDiscountList();

            }
        });
    }

    private void updateDiscountList() {
        //get all data from sqlite
        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM DISCOUNTS");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String pId = cursor.getString(1);
            String discount = cursor.getString(2);
            //String month = cursor.getString(3);
            String month = getMonthName(cursor.getInt(3));

            mList.add(new DiscountModel(id,pId,discount,month));
        }
        mAdapter.notifyDataSetChanged();
    }

    //get month from database
    public String getMonthName(int monthNo){

        String monthName = null;

        switch(monthNo){

            case 1 : monthName ="January";
            break;

            case 2 : monthName = "February";
            break;

            case 3 : monthName = "March";
            break;

            case 4 : monthName = "April";
            break;

            case 5 : monthName = "May";
            break;

            case 6 : monthName = "June";
            break;

            case 7 : monthName = "July";
            break;

            case 8 : monthName = "August";
            break;

            case 9 : monthName = "September";
            break;

            case 10 : monthName = "October";
            break;

            case 11 : monthName = "November";
            break;

            case 12 : monthName = "December";
            break;

        }
        return monthName;
    }

    //for get updated month number
    public int getUpdatedMonthNumber(String month){

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

            default : Toast.makeText(DiscountListActivity.this, "Please check  spellings", Toast.LENGTH_SHORT).show();
                break;
        }
        return no;
    }
}
