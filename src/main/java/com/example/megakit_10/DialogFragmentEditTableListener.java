package com.example.megakit_10;

import android.app.DialogFragment;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kalevych_tech@ukr.net on 06.09.2017.
 * Интерфейс, от которого будут унаследованы Диалоги редактирования таблиц.
 * Активити, которое вызывает диалог, унаследуется от интерфейса. Сам же диалог имеет интерфейс как поле,
 *  подвязывается к активити в методе onAttach диалогФрагмента
 */

public interface DialogFragmentEditTableListener {
     void onDialogDoneClick(DialogFragment dialogFragment, int operation, HashMap<String, String> params);
     void onDialogDoneClickUpdate(DialogFragment dialogFragment, int operation, HashMap<String, String> paramsFrom, HashMap<String, String> paramsTo);
}
