package com.example.megakit_10;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ПОДАРУНКОВИЙ on 05.09.2017.
 */

public class CarsRVAdapter extends CursorRecyclerViewAdapter<CarsRVAdapter.CarsViewHolder>{


    CarsRVAdapter(Context context, Cursor cursor){
        super(context,cursor);

    }

    @Override
    public CarsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        CarsViewHolder cvh = new CarsViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CarsViewHolder holder, Cursor cursor) {
        Auto myItem = Auto.fromCursor(cursor);
        holder.carId.setText(String.valueOf(myItem.getId()));
        holder.carNumber.setText(myItem.getNumber());
        holder.carPrice.setText(String.valueOf(myItem.getPrice()));
        holder.carYear.setText(String.valueOf(myItem.getYear()));
        holder.carModel.setText(myItem.getModel());
        holder.carOwner.setText(myItem.getOwner());

    }



    public static class CarsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView carId;
        TextView carModel;
        TextView carYear;
        TextView carPrice;
        TextView carOwner;
        TextView carNumber;

        CarsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            carId = (TextView)itemView.findViewById(R.id.textViewId);
            carModel = (TextView)itemView.findViewById(R.id.textViewModel);
            carYear = (TextView)itemView.findViewById(R.id.textViewYear);
            carPrice = (TextView)itemView.findViewById(R.id.textViewPrice);
            carOwner = (TextView)itemView.findViewById(R.id.textViewOwner);
            carNumber = (TextView)itemView.findViewById(R.id.textViewPlate);
        }
    }
}