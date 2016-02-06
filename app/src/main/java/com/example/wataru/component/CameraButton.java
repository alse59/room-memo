package com.example.wataru.component;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.wataru.room_memo.R;

/**
 * Created by wataru on 2015/12/21.
 */
public class CameraButton extends ImageButton {
    public CameraButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImageResource(android.R.drawable.ic_menu_camera);
    }


}
