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
 * Created by ПОДАРУНКОВИЙ on 07.09.2017.
 */

public class OwnersRVAdapter extends CursorRecyclerViewAdapter<OwnersRVAdapter.OwnersViewHolder> {

    Context context;

    OwnersRVAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
    }

    @Override
    public OwnersRVAdapter.OwnersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_owner, parent, false);
        OwnersRVAdapter.OwnersViewHolder ovh = new OwnersRVAdapter.OwnersViewHolder(v);
        return ovh;
    }

    @Override
    public void onBindViewHolder(OwnersRVAdapter.OwnersViewHolder holder, Cursor cursor) {
        Owner myItem = Owner.fromCursor(cursor);
        holder.ownerId.setText(context.getResources().getText(R.string.id) + String.valueOf(myItem.getId()));
        holder.ownerFirstname.setText((myItem.getFirstname() == null) ? context.getResources().getText(R.string.firstname) + "нет данных" : context.getResources().getText(R.string.firstname) + myItem.getFirstname());
        holder.ownerLastname.setText((myItem.getLastname() == null) ? context.getResources().getText(R.string.lastname) + "нет данных" : context.getResources().getText(R.string.lastname) + String.valueOf(myItem.getLastname()));
        holder.ownerAge.setText((myItem.getAge() == 0) ? context.getResources().getText(R.string.age) + "нет данных" : context.getResources().getText(R.string.age) + String.valueOf(myItem.getAge()));
        holder.ownerTelephone.setText((myItem.getTelephon() == null) ? context.getResources().getText(R.string.telephon) + "нет данных" : context.getResources().getText(R.string.telephon) + myItem.getTelephon());
        holder.ownerMail.setText((myItem.getMail() == null) ? context.getResources().getText(R.string.mail) + "нет данных" : context.getResources().getText(R.string.mail) + String.valueOf(myItem.getMail()));

    }


    public static class OwnersViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView ownerId;
        TextView ownerFirstname;
        TextView ownerLastname;
        TextView ownerAge;
        TextView ownerTelephone;
        TextView ownerMail;

        OwnersViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            ownerId = (TextView) itemView.findViewById(R.id.textViewIdOwner);
            ownerFirstname = (TextView) itemView.findViewById(R.id.textViewFirstname);
            ownerLastname = (TextView) itemView.findViewById(R.id.textViewLastname);
            ownerAge = (TextView) itemView.findViewById(R.id.textViewAge);
            ownerTelephone = (TextView) itemView.findViewById(R.id.textViewTelephon);
            ownerMail = (TextView) itemView.findViewById(R.id.textViewMail);
        }
    }
}