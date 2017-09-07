package com.example.megakit_10;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Kalevych_tech@ukr.net on 07.09.2017.
 * этот класс почти близнец класса  DialogFragmentEditTableCars, весь функционал постарался прокомментировать там
 */

public class DialogFragmentEditTableOwners extends android.app.DialogFragment implements View.OnClickListener {

    final String LOG_TAG = "myLogs";

    final int ITEM_FIND = 0;
    final int ITEM_ADD = 1;
    final int ITEM_CHANGE = 2;
    final int ITEM_DELETE = 3;

    boolean CHANGE_APPLY;

    HashMap<String, String> dataBeforeUpdate;

    EditText editTextID, editTextFirstname, editTextLastname, editTextAge, editTextTelephone, editTextMail;
    DialogFragmentEditTableListener mListener;
    Spinner spinnerCRUD, spinnerFirstname, spinnerLastname, spinnerAge, spinnerTelephone, spinnerMail, spinnerID;
    TextView tvCRUD, tvID;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {

            mListener = (DialogFragmentEditTableListener) activity;
        } catch (ClassCastException e) {

            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit_owners, null);
        v.findViewById(R.id.buttonDone).setOnClickListener(this);

        tvCRUD = (TextView)v.findViewById(R.id.textViewChooseCRUD);
        tvID = (TextView)v.findViewById(R.id.textViewID) ;


        spinnerCRUD = (Spinner)v.findViewById(R.id.spinnerCRUD);

        CHANGE_APPLY = false;


        spinnerID = ((Spinner)v.findViewById(R.id.spinnerID));
        spinnerFirstname = ((Spinner)v.findViewById(R.id.spinnerFirstname));
        spinnerLastname = ((Spinner)v.findViewById(R.id.spinnerLastname));
        spinnerAge = ((Spinner)v.findViewById(R.id.spinnerAge));
        spinnerTelephone = ((Spinner)v.findViewById(R.id.spinnerTelephone));
        spinnerMail = ((Spinner)v.findViewById(R.id.spinnerMail));

        editTextID = (EditText)v.findViewById(R.id.editTextID);
        editTextFirstname = (EditText)v.findViewById(R.id.editTextFirstname);
        editTextLastname = (EditText)v.findViewById(R.id.editTextLastname);
        editTextAge = (EditText)v.findViewById(R.id.editTextAge);
        editTextTelephone = (EditText)v.findViewById(R.id.editTextTelephone);
        editTextMail = (EditText)v.findViewById(R.id.editTextMail);



        return v;
    }

    public void onClick(View v) {
        if(checkDataValid()) {

            HashMap params = new HashMap<String, String>();

            if (!editTextID.getText().toString().equals("")) {

                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("_id", editTextID.getText().toString());
                } else {
                    params.put("_id ", spinnerID.getSelectedItem().toString() + " " + editTextID.getText().toString());
                }
            }
            if (!editTextFirstname.getText().toString().equals("")) {

                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("firstname", editTextFirstname.getText().toString());
                } else {
                    params.put("firstname ", spinnerFirstname.getSelectedItem().toString() + " '" + editTextFirstname.getText().toString()+"'");
                }
            }
            if (!editTextLastname.getText().toString().equals("")) {
                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("lastname", editTextLastname.getText().toString());
                } else {
                    params.put("lastname ", spinnerLastname.getSelectedItem().toString() + " '" + editTextLastname.getText().toString()+"'");
                }
            }
            if (!editTextAge.getText().toString().equals("")) {
                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("age", editTextAge.getText().toString());
                } else {
                    params.put("age ", spinnerAge.getSelectedItem().toString() + " " + editTextAge.getText().toString());
                }
            }
            if (!editTextTelephone.getText().toString().equals("")) {
                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("telephone", editTextTelephone.getText().toString());
                } else {
                    params.put("telephone ", spinnerTelephone.getSelectedItem().toString() + " " + editTextTelephone.getText().toString());
                }
            }
            if (!editTextMail.getText().toString().equals("")) {
                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("price", editTextMail.getText().toString());
                } else {
                    params.put("price ", spinnerMail.getSelectedItem().toString() + " " + editTextMail.getText().toString());
                }
            }

            if (spinnerCRUD.getSelectedItemPosition() == ITEM_CHANGE) {
                tvCRUD.setVisibility(View.GONE);
                tvID.setText("Введите новые даные для редактированых записей ");
                spinnerCRUD.setVisibility(View.GONE);
                spinnerID.setVisibility(View.GONE);
                editTextID.setVisibility(View.GONE);
                editTextFirstname.setText("");
                editTextLastname.setText("");
                editTextTelephone.setText("");
                editTextAge.setText("");
                editTextMail.setText("");
                if (CHANGE_APPLY) {
                    mListener.onDialogDoneClickUpdate(DialogFragmentEditTableOwners.this, spinnerCRUD.getSelectedItemPosition(), dataBeforeUpdate, params);
                    dismiss();
                }
                CHANGE_APPLY = true;
                dataBeforeUpdate = new HashMap<String, String>(params);

            } else {

                mListener.onDialogDoneClick(DialogFragmentEditTableOwners.this, spinnerCRUD.getSelectedItemPosition(), params);


                dismiss();
            }
        }else{
            Toast.makeText(getActivity().getApplicationContext(),"ПРОВЕРЬТЕ ПРАВИЛЬНОСТЬ ВВЕДЕННЫХ ДАННЫХ",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkDataValid() {
        boolean isValid = true;

        if(!(String.valueOf(editTextID.getText()).matches("[-+]?\\d+")||editTextID.getText().toString().equals(""))){editTextID.setText("!");isValid=false;}
        if(!(String.valueOf(editTextAge.getText()).matches("[-+]?\\d+")||editTextAge.getText().toString().equals(""))){editTextAge.setText("!");isValid=false;}

        return isValid;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }
}
