package com.example.pcplus.adddetails;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DiscountCustomrViewAdapter extends BaseAdapter{

    private Context context;
    private int layout;
    private ArrayList<DiscountModel> cusViewList;
    private SQLiteHelper sqLiteHelper;

    public DiscountCustomrViewAdapter(Context context, int layout, ArrayList<DiscountModel> cusViewList, SQLiteHelper sqLiteHelper) {
        this.context = context;
        this.layout = layout;
        this.cusViewList = cusViewList;
        this.sqLiteHelper = sqLiteHelper;
    }


    @Override
    public int getCount() {
        return cusViewList.size();
    }

    @Override
    public Object getItem(int i) {
        return cusViewList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtDiscount,txtOriginalPrice,txtDiscountPrice;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtDiscount = row.findViewById(R.id.txtCustViewdiscount);
            holder.txtOriginalPrice = row.findViewById(R.id.txtCustomerViewOrgPrice);
            holder.txtDiscountPrice = row.findViewById(R.id.txtCustomerViewDiscountPrice);
            holder.imageView = row.findViewById(R.id.custViewimg);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }

        DiscountModel discountModel = cusViewList.get(i);
        Model productModel = getProduct(Integer.parseInt(discountModel.getPId()));
        Double productDiscountModel = calculateDiscountPrice(productModel.getPrice(), discountModel.getDiscount());


        holder.txtDiscount.setText(productModel.getName() + " - " + discountModel.getDiscount() + "% off");
        holder.txtOriginalPrice.setText("Original price : Rs." + productModel.getPrice());
        holder.txtDiscountPrice.setText("Discounted price : Rs." + productDiscountModel);

        byte[] custViewImage = productModel.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(custViewImage, 0, custViewImage.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }

    private Model getProduct(int id){
        Model model = new Model(id);
        Cursor cursor = sqLiteHelper.getProductDetails(id);
        if(cursor.moveToNext()){
            model.setName(cursor.getString(1));
            model.setPrice(cursor.getString(2));
            model.setImage(cursor.getBlob(4));
        }
        return model;
    }

    //for calculate discount price
    private double calculateDiscountPrice(String OrgPrice, String discount) {
        return (Double.parseDouble(OrgPrice) * (1-Double.parseDouble(discount)/100));
    }
}

