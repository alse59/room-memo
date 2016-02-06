package com.example.wataru.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wataru.common.CommonConstants;
import com.example.wataru.dialog.DeleteDialog;
import com.example.wataru.greendao.db.ObjectImage;
import com.example.wataru.room_memo.R;

import java.util.ArrayList;
import java.util.List;

import maneger.DatabaseManager;

public class CardListFragment extends ListFragment {
    private static final String TAG = "CardListFragment";

    private OnFragmentInteractionListener mListener;

    public static CardListFragment newInstance() {
        return new CardListFragment();
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupList();

        refleshCardList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);

        return contentView;
    }

    /**
     * リストを設定する
     */
    private void setupList() {
        ListView listView = getListView();
        // 区切り線を消す
        listView.setDivider(null);
        // スクロールバーを表示しない
        listView.setVerticalScrollBarEnabled(false);
        // カード部分をselectorにするので、リストのselectorは透明にする
        listView.setSelector(android.R.color.transparent);
        // カードをクリックした際に画面を遷移する
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView)parent;
                // TODO なんでできへんねん
//                Item item = (Item)listView.getSelectedItem();
                Item item = (Item)listView.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putLong(CommonConstants.BUILDING_ID, item.id);
                onButtonPressed(bundle);
            }
        });
        // カードを長くクリックした場合にカードを削除するか確認する
        listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                Item item = (Item) listView.getItemAtPosition(position);
                showDialogFragment(item.id);
                return true;
            }
        });

        listView.addFooterView(LayoutInflater.from(getActivity()).inflate(
                R.layout.card_footer, listView, false));
    }

    // FragmentManagerでDialogを管理するクラス
    private void showDialogFragment(long selectedItemId) {
        FragmentManager manager = getFragmentManager();
        DeleteDialog fragment = DeleteDialog.newInstance(selectedItemId);
        fragment.setTargetFragment(this, CommonConstants.FROM_DELETE_DIALOG);

        fragment.show(manager, TAG);
    }

    /**
     * カードリストを更新する
     */
    public void refleshCardList() {
        DatabaseManager dbManager = DatabaseManager.getInstance(getActivity());
        List<com.example.wataru.greendao.db.Object> objects = dbManager.listObjects();
        Item[] data = new Item[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            long objectId = objects.get(i).getId();
            List<ObjectImage> objectImages = dbManager.findObjectImagesByObjectId(objectId);

            Bitmap bmp = null;
            if (objectImages != null && objectImages.size() != 0) {
                byte[] bytes = objectImages.get(0).getObjectImage();
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } else {
                bmp = getBitmapNoData();
            }
            data[i] = new Item(objects.get(i).getId(), objects.get(i).getObjectName(), bmp);
        }
        CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.card_item2, data);
        setListAdapter(adapter);
    }

    //ビットマップ(NO DATA)を取得する
    private Bitmap getBitmapNoData() {
        return BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.no_data);
    }

    public static class Item {
        public long id;
        public String objectName;
        public Bitmap bitmap;

        public Item(long id, String objectName, Bitmap bitmap) {
            super();
            this.id = id;
            this.objectName = objectName;
            this.bitmap = bitmap;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CommonConstants.FROM_DELETE_DIALOG) {
                long selectedItemId = data.getExtras().getLong(CommonConstants.DATA_ID);
                DatabaseManager manager = DatabaseManager.getInstance(getActivity());
                List<ObjectImage> objectImages = manager.findObjectImagesByObjectId(selectedItemId);
                List<Long> objectImagesId = new ArrayList<>();
                for (ObjectImage objectImage : objectImages) {
                    objectImagesId.add(objectImage.getId());
                }

                manager.deleteObjectImageByKeys(objectImagesId);
                manager.deleteObjectByKey(selectedItemId);
                refleshCardList();
            }
        }
    }

    public static class CustomAdapter extends ArrayAdapter<Item> {

        LayoutInflater mInflater;
        int mResId;

        public CustomAdapter(Context context, int resource, Item[] data) {
            super(context, 0, data);
            mResId = resource;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(mResId, parent, false);
            }

            Item item = getItem(position);

            ImageView iv = (ImageView) convertView.findViewById(R.id.image);
            iv.setImageBitmap(item.bitmap);

            TextView tv = (TextView) convertView.findViewById(R.id.title);
            tv.setText(item.objectName);

            return convertView;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Bundle bundle) {
        if (mListener != null) {
            mListener.onFragmentInteraction(bundle);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Bundle bundle);
    }
}
