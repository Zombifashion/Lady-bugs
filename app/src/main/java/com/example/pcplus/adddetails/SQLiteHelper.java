package com.example.pcplus.adddetails;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.SimpleDateFormat;

public class SQLiteHelper extends SQLiteOpenHelper{

    //constructor
    SQLiteHelper(Context context,
                 String name,
                 SQLiteDatabase.CursorFactory factory,
                 int version){
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //insertData
    public void insertData(String name, String price, String qty, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        //query to insert record in database table
        String sql = "INSERT INTO RECORD VALUES(NULL, ?, ?, ?, ?)"; //where "RECORD" is table name in database we will create in mainActivity

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, price);
        statement.bindString(3, qty);
        statement.bindBlob(4, image);

        statement.executeInsert();
    }

    //insert Discount data
    public void insertDiscountDetails(String pid, String discount, int month){

        SQLiteDatabase database = getWritableDatabase();
        //query to insert record in database table

        String sql = "INSERT INTO DISCOUNTS VALUES(NULL, ?, ?, ?)";//where "DISCOUNTS" is table name in database table we created in main activity
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, pid);
        statement.bindString(2, discount);
        //statement.bindString(3, month);
        statement.bindString(3, String.valueOf(month));

        statement.executeInsert();
    }

    //updateData
    public void updateData(String name, String price, String qty, byte[] image, int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to update record
        String sql = "UPDATE RECORD SET name=?, price=?, qty=?, image=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, price);
        statement.bindString(3, qty);
        statement.bindBlob(4, image);
        statement.bindDouble(5, (double)id);

        statement.execute();
        database.close();
    }

    //update discount details
    public void updateDiscountDetails(String prId, String discount, int month, int id){

        SQLiteDatabase database = getWritableDatabase();
        //query to update data

        String sql = "UPDATE DISCOUNTS SET productId=?, discount=?, month=?  WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, prId);
        statement.bindString(2, discount);
        //statement.bindString(3, month);
        statement.bindString(3, String.valueOf(month));
        statement.bindDouble(4, (double)id);


        statement.execute();
        database.close();
    }

    //deleteData
    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to delete record using id
        String sql = "DELETE FROM RECORD WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    //delete discount details
    public void deleteDiscountDetails(int id){

        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM DISCOUNTS WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }


    /**
     * Get Discount Object form discount table
     * @param id
     * @return Model
     */
    public Cursor getProductDetails(int id){
        return getData("select * FROM RECORD where id=?", new String[]{String.valueOf(id)});
    }

    //retrieve disount products from database
    public Bitmap getImage(Integer id){

//        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap bt = null;
        Cursor cursor = getData("select image FROM RECORD r,DISCOUNTS d where id=? and r.id=d.productId" ,new String[]{String.valueOf(id)});

        if(cursor.moveToNext()){
            byte[] image = cursor.getBlob(1);
            bt = BitmapFactory.decodeByteArray(image, 0,image.length);
        }
        return bt;
    }

    //get details for customer view from database
    public Cursor getCustomerViewDetails(String month){
        Cursor cursor = getData("select * from DISCOUNTS where month=?",new String[]{String.valueOf(month)});
//        Cursor cursor = getData("select * from DISCOUNTS");
        return cursor;
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public Cursor getData(String sql, String args[]){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, args);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}