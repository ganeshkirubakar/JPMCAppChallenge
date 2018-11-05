package com.jpmc.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jpmc.R;

public class DialogUtils {

    private static DialogUtils dialogUtils;

    /**
     * Create Instance of DialogUtils if not created else use existing instance
     * @return DialogUtils Instance
     */
    public static DialogUtils getInstance(){
        if(dialogUtils == null)
            dialogUtils = new DialogUtils();

        return dialogUtils;
    }

    /**
     * Show Alert Dialog if Any error form api
     * @param context context of dialog
     * @param message message to be shown in dialog
     */
    public void showAlertDialog(Context context, String message){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        // set the custom dialog components - text
        TextView text = dialog.findViewById(R.id.dialog_message);
        text.setText(message);

        Button dialogButton = dialog.findViewById(R.id.ok);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
