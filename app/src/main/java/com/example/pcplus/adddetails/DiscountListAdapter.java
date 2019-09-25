package com.example.pcplus.adddetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DiscountListAdapter extends BaseAdapter{

    private Context context;
    private int layout;
    private ArrayList<DiscountModel> discountList;


    public DiscountListAdapter(Context context, int layout, ArrayList<DiscountModel> discountList) {

        this.context = context;
        this.layout = layout;
        this.discountList = discountList;
    }


    @Override
    public int getCount() {
        return discountList.size();
    }

    @Override
    public Object getItem(int i) {
        return discountList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{

        ImageView imageView;
        TextView txtPrId, txtDiscount, txtMonth;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.txtPrId = row.findViewById(R.id.txtpid);
            holder.txtDiscount = row.findViewById(R.id.txtdiscount);
            holder.txtMonth = row.findViewById(R.id.txtmonth);

            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }

        DiscountModel dModel = discountList.get(i);

        holder.txtPrId.setText(dModel.getPId());
        holder.txtDiscount.setText(dModel.getDiscount()+"%");
        holder.txtMonth.setText(dModel.getMonth());


        return row;


    }


}
