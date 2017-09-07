package com.example.megakit_10;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ПОДАРУНКОВИЙ on 05.09.2017.
 */

public class CarsRVAdapter extends CursorRecyclerViewAdapter<CarsRVAdapter.CarsViewHolder>{

    Context context;
    CarsRVAdapter(Context context, Cursor cursor){
        super(context,cursor);
      this.context = context;
    }

    @Override
    public CarsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_car, parent, false);
        CarsViewHolder cvh = new CarsViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CarsViewHolder holder, Cursor cursor) {
        Car myItem = Car.fromCursor(cursor);
        holder.carId.setText(  context.getResources().getText(R.string.id)+String.valueOf(myItem.getId()));
        holder.carNumber.setText((myItem.getNumber()==null) ? context.getResources().getText(R.string.number)+ "нет данных" : context.getResources().getText(R.string.number)+myItem.getNumber());
        holder.carPrice.setText((myItem.getPrice()==0) ? context.getResources().getText(R.string.price)+ "нет данных" : context.getResources().getText(R.string.price)+String.valueOf(myItem.getPrice()));
        holder.carYear.setText((myItem.getYear()==0) ? context.getResources().getText(R.string.year)+ "нет данных" : context.getResources().getText(R.string.year)+String.valueOf(myItem.getYear()));
        holder.carModel.setText((myItem.getModel()==null) ? context.getResources().getText(R.string.model)+ "нет данных" : context.getResources().getText(R.string.model)+myItem.getModel());
        holder.carOwner.setText((myItem.getOwner()==0) ? context.getResources().getText(R.string.owner)+ "нет данных" : context.getResources().getText(R.string.owner)+String.valueOf(myItem.getOwner()));

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