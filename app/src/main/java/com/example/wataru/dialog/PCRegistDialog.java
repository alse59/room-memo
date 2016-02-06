package com.example.wataru.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.wataru.room_memo.R;


/**
 * A simple {@link DialogFragment} subclass.
 * Use the {@link PCRegistDialog} factory method to
 * create an instance of this fragment.
 */
public class PCRegistDialog extends DialogFragment {
    private static final String TAG = "PCRegistDialog";
    private EditText mEtPcName;

//    private DialogListener mListener;

    public PCRegistDialog() {
        // Required empty public constructor
    }

    public static PCRegistDialog newInstance() {
        PCRegistDialog fragment = new PCRegistDialog();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("新規内覧条件グループを生成");
        mEtPcName = new EditText(getContext());
        mEtPcName.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        builder.setView(mEtPcName);
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pcName = mEtPcName.getText().toString();

                Fragment fragment = getTargetFragment();
                if (fragment != null) {
                    Intent data = new Intent();
                    data.putExtra(TAG, pcName);
                    fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);

                    dialog.cancel();
                }


            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }
}
