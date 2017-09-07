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
 * Created by ПОДАРУНКОВИЙ on 07.09.2017.
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
        // String[] data_for_operation = {"Найти", "Добавить", "Изменить", "Удалить"};
        // String[] data_for_selector = {" = ", " > ", " < ", " >= ", " =< "};

        //getDialog().setTitle("Управление таблицой");
        View v = inflater.inflate(R.layout.fragment_edit_owners, null);
        v.findViewById(R.id.buttonDone).setOnClickListener(this);

        tvCRUD = (TextView)v.findViewById(R.id.textViewChooseCRUD);
        tvID = (TextView)v.findViewById(R.id.textViewID) ;

        //ArrayAdapter<String> adapter_CRUD_spinner = new ArrayAdapter<String>(getActivity().getApplicationContext(), simple_spinner_item, data_for_operation);
        //adapter_CRUD_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCRUD = (Spinner)v.findViewById(R.id.spinnerCRUD);
        //spinnerCRUD.setAdapter(adapter_CRUD_spinner);
        //spinnerCRUD.setPrompt("выберите пункт");
        //spinnerCRUD.setSelection(1, true);
        //spinnerCRUD.setPromptId(3);
        CHANGE_APPLY = false;

        //ArrayAdapter<String> adapter_select_spinner = new ArrayAdapter<String>(getActivity().getApplicationContext(), simple_spinner_item, data_for_selector);
        //adapter_select_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //List<Spinner> spinners_selector = new ArrayList<>();
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
        if(checkDataValid()) {
            //String queryAll = "";
            HashMap params = new HashMap<String, String>();
            //queryAll = queryAll + "operation id = " + String.valueOf(spinnerCRUD.getSelectedItemPosition()) + " parameters is: \n ";
            if (!editTextID.getText().toString().equals("")) {
                //queryAll = queryAll + " id " + spinnerID.getSelectedItem().toString() + " " + editTextID.getText().toString() + "\n";
                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("_id", editTextID.getText().toString());
                } else {
                    params.put("_id ", spinnerID.getSelectedItem().toString() + " " + editTextID.getText().toString());
                }
            }
            if (!editTextFirstname.getText().toString().equals("")) {
                //queryAll = queryAll + " Model " + spinnerModel.getSelectedItem().toString() + " " + editTextModel.getText().toString() + "\n";
                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("firstname", editTextFirstname.getText().toString());
                } else {
                    params.put("firstname ", spinnerFirstname.getSelectedItem().toString() + " '" + editTextFirstname.getText().toString()+"'");
                }
            }
            if (!editTextLastname.getText().toString().equals("")) {
                //queryAll = queryAll + " Number " + spinnerNumber.getSelectedItem().toString() + " " + editTextNumber.getText() + "\n";
                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("lastname", editTextLastname.getText().toString());
                } else {
                    params.put("lastname ", spinnerLastname.getSelectedItem().toString() + " '" + editTextLastname.getText().toString()+"'");
                }
            }
            if (!editTextAge.getText().toString().equals("")) {
                // queryAll = queryAll + " Owner " + spinnerOwner.getSelectedItem().toString() + " " + editTextOwner.getText() + "\n";
                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("age", editTextAge.getText().toString());
                } else {
                    params.put("age ", spinnerAge.getSelectedItem().toString() + " " + editTextAge.getText().toString());
                }
            }
            if (!editTextTelephone.getText().toString().equals("")) {
                //queryAll = queryAll + " Year " + spinnerYear.getSelectedItem().toString() + " " + editTextYear.getText() + "\n";
                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("telephone", editTextTelephone.getText().toString());
                } else {
                    params.put("telephone ", spinnerTelephone.getSelectedItem().toString() + " " + editTextTelephone.getText().toString());
                }
            }
            if (!editTextMail.getText().toString().equals("")) {
                //queryAll = queryAll + " Price " + spinnerPrice.getSelectedItem().toString() + " " + editTextPrice.getText() + "\n";
                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("price", editTextMail.getText().toString());
                } else {
                    params.put("price ", spinnerMail.getSelectedItem().toString() + " " + editTextMail.getText().toString());
                }
            }
            // Log.d(LOG_TAG, queryAll);

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
