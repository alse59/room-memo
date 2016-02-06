package com.example.wataru.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wataru.common.CommonConstants;
import com.example.wataru.dialog.DeleteDialog;
import com.example.wataru.dialog.PCRegistDialog;
import com.example.wataru.greendao.db.PreviewConfirm;
import com.example.wataru.greendao.db.PreviewConfirmDetail;
import com.example.wataru.greendao.db.PreviewConfirmDetailDao;
import com.example.wataru.room_memo.MyCursorLoader;
import com.example.wataru.room_memo.PreviewConfirmAdapter;
import com.example.wataru.room_memo.R;

import java.util.List;

import maneger.DatabaseManager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreviewConfirmFragment} factory method to
 * create an instance of this fragment.
 */
public class PreviewConfirmFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "PreviewConfirmFragment";
    private Spinner mSpinner;
    private EditText mEditText;
    private PreviewConfirmAdapter mSpinnerAdapter;
    private SimpleCursorAdapter mAdapter;
    private ListView mListView;
    private static final String EMPTY = "";

    public PreviewConfirmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_preview_confirm, container, false);

        // 使用するコンポーネントをセットアップする
        setupComponent(contentView);
        // 内覧確認用のスピナーをセットアップする
        setupSpinner();
        // 内覧確認詳細追加用のエディットテキストをセットアップする
        setupEditText();
        // 内覧確認詳細用のスピナーをセットアップする
        setupList();

        return contentView;
    }

    /**
     * 使用するコンポーネントをセットアップする
     * @param contentView フラグメントのビュー
     */
    private void setupComponent(View contentView) {
        mSpinner = (Spinner)contentView.findViewById(R.id.sp_pc);
        mEditText = (EditText)contentView.findViewById(R.id.et_pcd);
        mListView = (ListView)contentView.findViewById(R.id.lv_pcd);
    }

    /**
     * 内覧確認用のスピナーをセットアップする
     */
    private void setupSpinner() {
        DatabaseManager dm = DatabaseManager.getInstance(getContext());
        List<PreviewConfirm> previewConfirms = dm.listPreviewConfirms();
        mSpinnerAdapter = new PreviewConfirmAdapter(getContext(), R.layout.list_row, previewConfirms);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO 最初に２階ローダーを読んでしまう
                getLoaderManager().restartLoader((int) mSpinner.getSelectedItemId(), null, PreviewConfirmFragment.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * 内覧確認詳細追加用のエディットテキストをセットアップする
     */
    private void setupEditText() {
        // テキストでエンターキーを押した際に、内覧確認詳細を追加する
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String text = ((TextView) v).getText().toString();
                    PreviewConfirmDetail pcd = new PreviewConfirmDetail();
                    pcd.setPcDetailName(text);
                    pcd.setPcId(mSpinner.getSelectedItemId());

                    DatabaseManager manager = DatabaseManager.getInstance(getContext());
                    manager.insertPreviewConfirmDetail(pcd);
                    restartLoader();
                    ((TextView) v).setText(EMPTY);

                    //キーボードを閉じる
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }
                return false;
            }
        });
    }



    /**
     * 内覧確認詳細用のスピナーをセットアップする
     */
    private void setupList() {
        // リスト用のリソースを生成する
        String[] fromColumn = {PreviewConfirmDetailDao.Properties.PcDetailName.columnName};
        int[] toResource = {android.R.id.text1};

        // リスト用のアダプターを生成する
        mAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_1, null, fromColumn, toResource, 0);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(id);
                return true;
            }
        });
        int pcId = (int)mSpinner.getSelectedItemId();
        getLoaderManager().initLoader(pcId, null, this);
    }


    // FragmentManagerでDialogを管理するクラス
    public void showPCRegistDialog() {
        FragmentManager manager = getFragmentManager();
        DialogFragment fragment = PCRegistDialog.newInstance();
        fragment.setTargetFragment(this, CommonConstants.TO_PC_REGIST_DIALOG_CODE);
        fragment.show(manager, TAG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CommonConstants.TO_PC_REGIST_DIALOG_CODE) {
                Bundle b = data.getExtras();
                String pcName = b.getString("PCRegistDialog");
                DatabaseManager dm = DatabaseManager.getInstance(getContext());
                PreviewConfirm pc = new PreviewConfirm();
                pc.setPcName(pcName);
                dm.insertPreviewConfirm(pc);
                mSpinnerAdapter.add(pc);
            } else if (requestCode == CommonConstants.FROM_DELETE_DIALOG) {
                long dataId = data.getExtras().getLong(CommonConstants.DATA_ID);
                DatabaseManager dm = DatabaseManager.getInstance(getContext());
                dm.deletePreviewConfirmDetail(dataId);
                restartLoader();
            }
        }
    }

    private void showDeleteDialog(long dataId) {
        FragmentManager manager = getFragmentManager();
        DialogFragment fragment = DeleteDialog.newInstance(dataId);
        fragment.setTargetFragment(this, CommonConstants.FROM_DELETE_DIALOG);

        fragment.show(manager, TAG);
    }

    /**
     * 内覧確認詳細ローダーを再描画する
     */
    private void restartLoader() {
        getLoaderManager().restartLoader((int) mSpinner.getSelectedItemId(), null, PreviewConfirmFragment.this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(getContext(), id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.swapCursor(null);
    }


}
