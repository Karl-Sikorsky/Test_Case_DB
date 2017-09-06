package com.example.megakit_10;

import android.app.DialogFragment;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ПОДАРУНКОВИЙ on 06.09.2017.
 */

public interface DialogFragmentEditTableListener {
    public void onDialogDoneClick(DialogFragment dialogFragment, int operation, HashMap<String, String> params);
    public void onDialogDoneClickUpdate(DialogFragment dialogFragment, int operation, HashMap<String, String> paramsFrom, HashMap<String, String> paramsTo);
}
