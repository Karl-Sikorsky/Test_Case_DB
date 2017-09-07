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
 * Created by Kalevych_tech@ukr.net on 06.09.2017.
 * реализация диалогового окна для редактирования Таблицы Автомобилей
 *
 */

public class DialogFragmentEditTableCars extends android.app.DialogFragment implements View.OnClickListener {
//Основные константы
    final String LOG_TAG = "myLogs";
    final int ITEM_FIND = 0;
    final int ITEM_ADD = 1;
    final int ITEM_CHANGE = 2;
    final int ITEM_DELETE = 3;
    boolean CHANGE_APPLY;
    //для метода Update форму записей нужно заполнить дважды -  сначала получить where, потом insert, данные про изменяемые записи
    //будут храниться в Карте:
    HashMap<String, String> dataBeforeUpdate;

    EditText editTextID, editTextModel, editTextNumber, editTextOwner, editTextPrice, editTextYear;
    DialogFragmentEditTableListener mListener;
    Spinner spinnerCRUD, spinnerID, spinnerModel, spinnerNumber, spinnerOwner, spinnerPrice, spinnerYear;
    TextView tvCRUD, tvID;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // удостоверяемся, что активити реализовалась от DialogFragmentEditTableListener
        try {

            mListener = (DialogFragmentEditTableListener) activity;
        } catch (ClassCastException e) {
            // если активити не реализовалась
            throw new ClassCastException(activity.toString()
                    + " must implement DialogListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.fragment_edit_cars, null);
        v.findViewById(R.id.buttonDone).setOnClickListener(this);

        tvCRUD = (TextView)v.findViewById(R.id.textViewChooseCRUD);
        tvID = (TextView)v.findViewById(R.id.textViewID) ;


          //спиннер выбора операции (удаление, поиск, вставка...
        spinnerCRUD = (Spinner)v.findViewById(R.id.spinnerCRUD);
        //переменная, которая просигнализирует диалог о том, что данные "каким записям делать апдейт" в случае Update уже заполнены
        CHANGE_APPLY = false;


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

        return v;
    }

    public void onClick(View v) {
        //проверяем, правильно ли введены данные (например, если в поле для числа ввели символы, то убедим пользователя
        //ввести данные корректно
        if(checkDataValid()) {
            //параметры запроса собираем из формы, отправляем в Активити, и оттуда дергаем Базу Данных
            HashMap params = new HashMap<String, String>();

            //Здесь много boilerplate кода, нам нужно для каждого поля ввода обработать данные, чтоб отправить их в запрос
            //банально не успел придумать, как это все расфасовать по списках и обработать циклом
            if (!editTextID.getText().toString().equals("")) {

                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("_id", editTextID.getText().toString());
                } else {
                    params.put("_id ", spinnerID.getSelectedItem().toString() + " " + editTextID.getText().toString());
                }
            }
            if (!editTextModel.getText().toString().equals("")) {

                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("model", editTextModel.getText().toString());
                } else {
                    params.put("model ", spinnerModel.getSelectedItem().toString() + " '" + editTextModel.getText().toString()+"'");
                }
            }
            if (!editTextNumber.getText().toString().equals("")) {

                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("number", editTextNumber.getText().toString());
                } else {
                    params.put("number ", spinnerNumber.getSelectedItem().toString() + " '" + editTextNumber.getText().toString()+"'");
                }
            }
            if (!editTextOwner.getText().toString().equals("")) {

                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("owner", editTextOwner.getText().toString());
                } else {
                    params.put("owner ", spinnerOwner.getSelectedItem().toString() + " " + editTextOwner.getText().toString());
                }
            }
            if (!editTextYear.getText().toString().equals("")) {

                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("year", editTextYear.getText().toString());
                } else {
                    params.put("year ", spinnerYear.getSelectedItem().toString() + " " + editTextYear.getText().toString());
                }
            }
            if (!editTextPrice.getText().toString().equals("")) {

                if ((spinnerCRUD.getSelectedItemPosition() == ITEM_ADD) || (CHANGE_APPLY)) {
                    params.put("price", editTextPrice.getText().toString());
                } else {
                    params.put("price ", spinnerPrice.getSelectedItem().toString() + " " + editTextPrice.getText().toString());
                }
            }

                //изменяем внешний вид формы, если мы редактируем данные:
            // мы считали данные, какие записи изменять
            //теперь считываем данные, какие значения заносить в выбраные записи
            if (spinnerCRUD.getSelectedItemPosition() == ITEM_CHANGE) {
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
                if (CHANGE_APPLY) {
                    //отправляем параметры запроса в Активити
                    mListener.onDialogDoneClickUpdate(DialogFragmentEditTableCars.this, spinnerCRUD.getSelectedItemPosition(), dataBeforeUpdate, params);
                    dismiss();
                }
                CHANGE_APPLY = true;
                dataBeforeUpdate = new HashMap<String, String>(params);

            } else {
                  //отправляем параметры запроса в Активити
                mListener.onDialogDoneClick(DialogFragmentEditTableCars.this, spinnerCRUD.getSelectedItemPosition(), params);


                dismiss();
            }
        }else{
            //если валидация не пройдена
            Toast.makeText(getActivity().getApplicationContext(),"ПРОВЕРЬТЕ ПРАВИЛЬНОСТЬ ВВЕДЕННЫХ ДАННЫХ",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkDataValid() {
        boolean isValid = true;

        if(!(String.valueOf(editTextID.getText()).matches("[-+]?\\d+")||editTextID.getText().toString().equals(""))){editTextID.setText("!");isValid=false;}
        if(!(String.valueOf(editTextYear.getText()).matches("[-+]?\\d+")||editTextYear.getText().toString().equals(""))){editTextYear.setText("!");isValid=false;}
        if(!(String.valueOf(editTextPrice.getText()).matches("[-+]?\\d+")||editTextPrice.getText().toString().equals(""))){editTextPrice.setText("!");isValid=false;}
        if(!(String.valueOf(editTextOwner.getText()).matches("[-+]?\\d+")||editTextOwner.getText().toString().equals(""))){editTextOwner.setText("!");isValid=false;}

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
