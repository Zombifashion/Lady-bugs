package com.example.pcplus.adddetails;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText mEdtName, mEdtPrice, mEdtQty,mEdtPrId;
    Button mBtnAdd, mBtnList,mBtnProduct,mBtnDiscountAdd,mBtnDiscontList;
    ImageView mImageView;
    Spinner month;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Product");

        //for products
        mEdtName = findViewById(R.id.edtName);
        mEdtPrice = findViewById(R.id.edtPrice);
        mEdtQty = findViewById(R.id.edtQty);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnList = findViewById(R.id.btnList);
        mBtnProduct = findViewById(R.id.btnProduct);
        mImageView = findViewById(R.id.imageView);

        //for discounts
        //mEdtPrId = findViewById(R.id.edt);
        mBtnDiscountAdd = findViewById(R.id.btnDiscountAdd);

        //for drop down list
        /*Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Months, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);*/


        //creating database
        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        //creating table in database
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR, qty VARCHAR, image BLOB)");
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS DISCOUNTS(id INTEGER PRIMARY KEY AUTOINCREMENT, productId INTEGER, discount VARCHAR, month VARCHAR, FOREIGN KEY (productId) REFERENCES RECORD(id) )");

        //select image by on imageview click
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //read external storage permission to select image from gallery
                //runtime permission for devices android 6.0 and above
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        //add record to sqlite
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;

                if (mEdtName.getText().toString().isEmpty()) {
                    mEdtName.setError(getString(R.string.Product_Name_validation_massage));
                    isValid = false;
                }
               /* else {
                    productIdEditText.setErrorEnabled(false);
                }*/

                if (mEdtPrice.getText().toString().isEmpty()) {
                    mEdtPrice.setError(getString(R.string.Price_validation_massage));
                    isValid = false;
                }
                /*else {
                    discountEdittext.setErrorEnabled(false);
                }*/
                if (mEdtQty.getText().toString().isEmpty()) {
                    mEdtQty.setError(getString(R.string.Product_quantitty_validation_massage));
                    isValid = false;
                }
                /*else {
                    discountEdittext.setErrorEnabled(false);
                }*/

                if (isValid) {

                    try {
                        mSQLiteHelper.insertData(
                                mEdtName.getText().toString().trim(),
                                mEdtPrice.getText().toString().trim(),
                                mEdtQty.getText().toString().trim(),
                                imageViewToByte(mImageView)

                        );
                        Toast.makeText(MainActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                        //reset views
                        mEdtName.setText("");
                        mEdtPrice.setText("");
                        mEdtQty.setText("");
                        mImageView.setImageResource(R.drawable.addphoto);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //show record list
        mBtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start recordlist activity
                startActivity(new Intent(MainActivity.this, RecordListActivity.class));
            }
        });

        mBtnDiscountAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddDiscounts.class));
            }
        });

        //show product layout list
      mBtnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, main_product_custActivity.class));
            }
        });


    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(this, "Don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                    .setAspectRatio(1,1)// image will be square
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                //set image choosed from gallery to image view
                mImageView.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
