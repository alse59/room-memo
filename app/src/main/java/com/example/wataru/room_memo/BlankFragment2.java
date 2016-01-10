package com.example.wataru.room_memo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.wataru.greendao.db.Object;
import com.example.wataru.greendao.db.ObjectImage;

import java.util.ArrayList;
import java.util.List;

import maneger.DatabaseManager;
import util.ImageUtils;
import view.button.CameraButton;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment2 extends Fragment {
    private EditText etObjectName;
    private EditText etAddress;
    private EditText etLayoutType;
    private EditText etRoomSize;
    private EditText etAge;
    private ImageView ivObjectImage;
    private ViewPager vpObjectImage;
    private Button btnInsert;
    private CameraButton btnCamera;
    private ScrollView mScrollView;
    private static final String ARG_OBJECT_ID = "object_id";
    private MyPagerAdapter mAdapter;
    private static final int RESULT_OK = -1;
    private static final int REQUEST_CAPTURE_IMAGE = 100;

    // TODO: Rename and change types of parameters
    private long mObjectId;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param objectId Parameter 1.
     * @return A new instance of fragment InsertFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment2 newInstance(long objectId) {
        BlankFragment2 fragment = new BlankFragment2();
        Bundle args = new Bundle();
        args.putLong(ARG_OBJECT_ID, objectId);
        fragment.setArguments(args);
        return fragment;
    }

    public BlankFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mObjectId = getArguments().getLong(ARG_OBJECT_ID);
        }
        // TODO 0だと1件目は取得できないか
        mMode = mObjectId == 0 ? INSERT_MODE : UPDATE_MODE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mObjectId = 0;
    }

    private static final int INSERT_MODE = 0;
    private static final int UPDATE_MODE = 1;
    private int mMode;
    private String[] btnRegistText = {"登録", "更新"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_blank_fragment2, container, false);
        setupComponent(contentView);

        // オブジェクトを検索する
        DatabaseManager manager = new DatabaseManager(getActivity());
        Object object = manager.findObjectById(mObjectId);

        // オブジェクトの内容をテキストに設定する
        setComponentText(object);

        // オブジェクトイメージを検索する
        List<ObjectImage> objectImages = manager.findObjectImagesByObjectId(mObjectId);

        // ビューページャーにイメージを設定する
        setViewPager(objectImages);

        // ボタンをセットアップする
        setupButton();

        // Inflate the layout for this fragment
        return contentView;
    }

    /**
     * 部品をセットアップする
     */
    private void setupComponent(View contentView) {
        etObjectName = (EditText)contentView.findViewById(R.id.et_object_name);
        etAddress = (EditText)contentView.findViewById(R.id.et_address);
        etLayoutType = (EditText)contentView.findViewById(R.id.et_layout_type);
        EditText etRoomSize = (EditText)contentView.findViewById(R.id.et_room_size);
        EditText etAge = (EditText)contentView.findViewById(R.id.et_age);
        ivObjectImage = (ImageView)contentView.findViewById(R.id.iv_object_image);
        btnInsert = (Button)contentView.findViewById(R.id.btn_insert);
        btnCamera = (CameraButton)contentView.findViewById(R.id.btn_camera);
        vpObjectImage = (ViewPager)contentView.findViewById(R.id.viewPager2);
        mScrollView = (ScrollView)contentView.findViewById(R.id.sv_insert);
    }

    /**
     * オブジェクトの内容をテキストに設定する
     */
    private void setComponentText(Object object) {
        if (object == null) return;
        etObjectName.setText(object.getObjectName());
        etAddress.setText(object.getAddress());
        etLayoutType.setText(object.getLayoutType());
    }

    /**
     * ビューページャーにイメージを設定する
     * @param objectImages
     */
    private void setViewPager(List<ObjectImage> objectImages) {
//        List<Bitmap> bitmaps = ImageUtils.toArrayBitmap(objectImages);
        List<Bitmap> bitmaps = new ArrayList<>();
        mAdapter = new MyPagerAdapter(getActivity(), bitmaps);
        vpObjectImage.setAdapter(mAdapter);

    }

    /**
     * ボタンをセットアップする
     */
    private void setupButton() {
        btnInsert.setText(btnRegistText[mMode]);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveMainFragment();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CAPTURE_IMAGE == requestCode && resultCode == RESULT_OK) {
            // 撮影したビットマップをイメージビューに表示する
            Bitmap capturedImage = (Bitmap)data.getExtras().get("data");

            mAdapter.add(capturedImage);
//            vpObjectImage.setAdapter(mAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        etObjectName = null;
        etAddress = null;
        etLayoutType = null;
        btnInsert = null;
        btnCamera = null;
        vpObjectImage = null;
    }

    public void moveMainFragment() {
        String objectName = etObjectName.getText().toString();
        com.example.wataru.greendao.db.Object object = new com.example.wataru.greendao.db.Object();
        object.setObjectName(objectName);

        String address = etAddress.getText().toString();
        object.setAddress(address);

        String layoutType = etLayoutType.getText().toString();
        object.setLayoutType(layoutType);


        String roomSize = etRoomSize.getText().toString();
        object.setRoomSize(Integer.parseInt(roomSize));


        String age = etAge.getText().toString();
        object.setAge(Integer.parseInt(age));

        DatabaseManager dbManager = new DatabaseManager(getActivity());
        if (mMode == INSERT_MODE) {
            mObjectId = dbManager.insertObject(object);
        } else if (mMode == UPDATE_MODE) {
            dbManager.updateObject(object);
        }

        // TODO ここらへん適当
        byte[] bytes = ImageUtils.toBites(ivObjectImage);
        if (bytes != ImageUtils.INIT_BYTES) {
            ObjectImage objectImage = new ObjectImage();
            objectImage.setObjectImage(bytes);
            objectImage.setObjectId(mObjectId);
            if (mMode == INSERT_MODE) {
                dbManager.insertObjectImage(objectImage);
            } else if (mMode == UPDATE_MODE){
                dbManager.updateObjectImage(objectImage);
            }
        }
//        onButtonPressed(Activity.RESULT_OK);
    }


}
