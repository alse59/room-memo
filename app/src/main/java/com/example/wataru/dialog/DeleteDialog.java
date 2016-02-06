package com.example.wataru.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.example.wataru.common.CommonConstants;
import com.example.wataru.room_memo.R;

/**
 * Created by wataru on 2016/01/11.
 */
public class DeleteDialog extends DialogFragment {
    private static final String TAG = "DeleteDialog";

    public DeleteDialog() {
    }

    public static DeleteDialog newInstance(long dataId) {
        DeleteDialog fragment = new DeleteDialog();
        // Fragmentの再生成後でも使いたい値は、bundleに入れてsetArgumentしておく。
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConstants.DATA_ID, dataId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public long getDataId() {
        return getArguments().getLong(CommonConstants.DATA_ID, -1);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.delete_dialog_message));
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Fragment fragment = getTargetFragment();
                if (fragment != null) {
                    long dataId = getDataId();
                    Intent data = new Intent();
                    data.putExtra(CommonConstants.DATA_ID, dataId);
                    fragment.onActivityResult(CommonConstants.FROM_DELETE_DIALOG, Activity.RESULT_OK, data);
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
