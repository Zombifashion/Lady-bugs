package com.example.pcplus.adddetails;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class main_product_custActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<Model> mList;
    RecordListAdapter mAdapter = null;

    ImageView imageViewIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //customer view
        setContentView(R.layout.activity_main_product_cust );

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Product List");

        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new RecordListAdapter(this, R.layout.row, mList);
        mListView.setAdapter(mAdapter);

        //get all data from sqlite
        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM RECORD");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            String qty = cursor.getString(3);
            byte[] image  = cursor.getBlob(4);
            //add to list
            mList.add(new Model(id, name, price, qty, image));
        }
        mAdapter.notifyDataSetChanged();
        if (mList.size()==0){
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "No product found...", Toast.LENGTH_SHORT).show();
        }

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //alert dialog to display options of update and delete
                final CharSequence[] items = {"View Product"};

                AlertDialog.Builder dialog = new AlertDialog.Builder( com.example.pcplus.adddetails.main_product_custActivity.this);

                dialog.setTitle("Want to buy ? ");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            //update
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            //show update dialog
                            showDialogUpdate( com.example.pcplus.adddetails.main_product_custActivity.this, arrID.get(position));
                        }
                        if (i==1){
                            //delete
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            //showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

    }



    private void showDialogUpdate(Activity activity, final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.view_product);
        dialog.setTitle("Product Details");

        imageViewIcon = dialog.findViewById(R.id.imageViewRecord);
        final TextView edtName = dialog.findViewById(R.id.edtName);
        final TextView edtPrice = dialog.findViewById(R.id.edtPrice);
        final TextView edtQty = dialog.findViewById(R.id.edtQty);
        //Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

        //get data of the row clicked from sqlite
        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM RECORD WHERE id="+position);
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            edtName.setText(name); //set product name to update
            String price = cursor.getString(2);
            edtPrice.setText(price); //set product price to update
            String qty = cursor.getString(3);
            edtQty.setText(qty); //set quantity to update
            byte[] image  = cursor.getBlob(4);
            //set image got from sqlite
            imageViewIcon.setImageBitmap( BitmapFactory.decodeByteArray(image, 0, image.length));
            //add to list
            mList.add(new Model(id, name, price, qty, image));
        }

        //set width of dialog
        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.95);
        //set hieght of dialog
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width,height);
        dialog.show();

        //in update dialog click image view to update image
            /*imageViewIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //check external storage permission
                    ActivityCompat.requestPermissions(
                            com.example.pcplus.adddetails.main_product_custActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            888
                    );
                }
            });*/


    }


}
