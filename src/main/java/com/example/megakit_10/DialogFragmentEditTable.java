package com.example.megakit_10;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.layout.simple_spinner_item;

/**
 * Created by ПОДАРУНКОВИЙ on 06.09.2017.
 */

public class DialogFragmentEditTable extends android.app.DialogFragment implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    final int ITEM_FIND = 0;
    final int ITEM_ADD = 1;
    final int ITEM_CHANGE = 2;
    final int ITEM_DELETE = 3;
    boolean CHANGE_APPLY;
    HashMap<String, String> dataBeforeUpdate;

    EditText editTextID, editTextModel, editTextNumber, editTextOwner, editTextPrice, editTextYear;
    DialogFragmentEditTableListener mListener;
    Spinner spinnerCRUD, spinnerID, spinnerModel, spinnerNumber, spinnerOwner, spinnerPrice, spinnerYear;
    TextView tvCRUD, tvID;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogFragmentEditTableListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] data_for_operation = {"Найти", "Добавить", "Изменить", "Удалить"};
        String[] data_for_selector = {" = ", " > ", " < ", " >= ", " =< "};

        getDialog().setTitle("Управление таблицой");
        View v = inflater.inflate(R.layout.fragment_edit, null);
        v.findViewById(R.id.buttonDone).setOnClickListener(this);

        tvCRUD = (TextView)v.findViewById(R.id.textViewChooseCRUD);
        tvID = (TextView)v.findViewById(R.id.textViewID) ;

        ArrayAdapter<String> adapter_CRUD_spinner = new ArrayAdapter<String>(getActivity().getApplicationContext(), simple_spinner_item, data_for_operation);
        adapter_CRUD_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCRUD = (Spinner)v.findViewById(R.id.spinnerCRUD);
        //spinnerCRUD.setAdapter(adapter_CRUD_spinner);
        //spinnerCRUD.setPrompt("выберите пункт");
        //spinnerCRUD.setSelection(1, true);
        //spinnerCRUD.setPromptId(3);
        CHANGE_APPLY = false;

        ArrayAdapter<String> adapter_select_spinner = new ArrayAdapter<String>(getActivity().getApplicationContext(), simple_spinner_item, data_for_selector);
        adapter_select_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //List<Spinner> spinners_selector = new ArrayList<>();
        spinnerID = ((Spinner)v.findViewById(R.id.spinnerID));
        spinnerModel = ((Spinner)v.findViewById(R.id.spinnerModel));
        spinnerNumber = ((Spinner)v.findViewById(R.id.spinnerNumber));
        spinnerOwner = ((Spinner)v.findViewById(R.id.spinnerOwner));
        spinnerPrice = ((Spinner)v.findViewById(R.id.spinnerPrice));
        spinnerYear = ((Spinner)v.findViewById(R.id.spinnerYear));

        editTextID = (EditText)v.findViewById(R.id.editTextID);
        editTextModel = (EditText)v.findViewById(R.id.editTextModel);
        editTextNumber = (EditText)v.findViewById(R.id.editTextNumber);
        editTextOwner = (EditText)v.findViewById(R.id.editTextOwner);
        editTextPrice = (EditText)v.findViewById(R.id.editTextPrice);
        editTextYear = (EditText)v.findViewById(R.id.editTextYear);




        // устанавливаем обработчик нажатия
       /* spinnerCRUD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                spinnerCRUD.setSelection(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/




        return v;
    }

    public void onClick(View v) {

        String queryAll = "";
        HashMap params = new HashMap< String, String>();
        queryAll = queryAll + "operation id = " + String.valueOf(spinnerCRUD.getSelectedItemPosition()) + " parameters is: \n ";
        if(!editTextID.getText().toString().equals("")){
            queryAll = queryAll + " id " + spinnerID.getSelectedItem().toString() + " " + editTextID.getText().toString() + "\n";
            if((spinnerCRUD.getSelectedItemPosition()==ITEM_ADD)||(CHANGE_APPLY)){params.put("_id",editTextID.getText().toString());}
            else{params.put("_id ",spinnerID.getSelectedItem().toString() + " " + editTextID.getText().toString());}
        }
        if(!editTextModel.getText().toString().equals("")){
            queryAll = queryAll + " Model " + spinnerModel.getSelectedItem().toString() + " " + editTextModel.getText().toString() + "\n";
            if((spinnerCRUD.getSelectedItemPosition()==ITEM_ADD)||(CHANGE_APPLY)){params.put("model",editTextModel.getText().toString());}
            else{params.put("model ",spinnerModel.getSelectedItem().toString() + " " + editTextModel.getText().toString());}
        }
        if(!editTextNumber.getText().toString().equals("")){
            queryAll = queryAll + " Number " + spinnerNumber.getSelectedItem().toString() + " " + editTextNumber.getText() + "\n";
            if((spinnerCRUD.getSelectedItemPosition()==ITEM_ADD)||(CHANGE_APPLY)){params.put("number",editTextNumber.getText().toString());}
            else{params.put("number ",spinnerNumber.getSelectedItem().toString() + " " + editTextNumber.getText().toString());}
        }
        if(!editTextOwner.getText().toString().equals("")){
            queryAll = queryAll + " Owner " + spinnerOwner.getSelectedItem().toString() + " " + editTextOwner.getText() + "\n";
            if((spinnerCRUD.getSelectedItemPosition()==ITEM_ADD)||(CHANGE_APPLY)){params.put("owner",editTextOwner.getText().toString());}
            else{params.put("owner ",spinnerOwner.getSelectedItem().toString() + " " + editTextOwner.getText().toString());}
        }
        if(!editTextYear.getText().toString().equals("")){
            queryAll = queryAll + " Year " + spinnerYear.getSelectedItem().toString() + " " + editTextYear.getText() + "\n";
            if((spinnerCRUD.getSelectedItemPosition()==ITEM_ADD)||(CHANGE_APPLY)){params.put("year",editTextYear.getText().toString());}
            else{params.put("year ",spinnerYear.getSelectedItem().toString() + " " + editTextYear.getText().toString());}
        }
        if(!editTextPrice.getText().toString().equals("")){
            queryAll = queryAll + " Price " + spinnerPrice.getSelectedItem().toString() + " " + editTextPrice.getText() + "\n";
            if((spinnerCRUD.getSelectedItemPosition()==ITEM_ADD)||(CHANGE_APPLY)){params.put("price",editTextPrice.getText().toString());}
            else{ params.put("price ",spinnerPrice.getSelectedItem().toString() + " " + editTextPrice.getText().toString());}
        }
        Log.d(LOG_TAG, queryAll);

        if(spinnerCRUD.getSelectedItemPosition()==ITEM_CHANGE){
         tvCRUD.setVisibility(View.GONE);
            tvID.setText("Введите новые даные для редактированых записей ");
            spinnerCRUD.setVisibility(View.GONE);
            spinnerID.setVisibility(View.GONE);
            editTextID.setVisibility(View.GONE);
            editTextModel.setText("");
            editTextYear.setText("");
            editTextOwner.setText("");
            editTextNumber.setText("");
            editTextPrice.setText("");
            if(CHANGE_APPLY){
                mListener.onDialogDoneClickUpdate(DialogFragmentEditTable.this, spinnerCRUD.getSelectedItemPosition(),dataBeforeUpdate, params);
            dismiss();
            }
            CHANGE_APPLY = true;
            dataBeforeUpdate = new HashMap<String, String>(params);

        }else{

        mListener.onDialogDoneClick(DialogFragmentEditTable.this, spinnerCRUD.getSelectedItemPosition(), params);


        dismiss();}
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
