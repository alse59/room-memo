package com.example.wataru.room_memo;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wataru.common.CommonConstants;
import com.example.wataru.fragment.InsertFragment;

public class InsertActivity extends AppCompatActivity implements InsertFragment.OnFragmentInteractionListener {
    private static final String TAG = "InsertActivity";
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        fragment = getFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.insert_container, fragment, TAG).commit();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * フラグメントを取得する
     * @return
     */
    public Fragment getFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(TAG);
        if (fragment == null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            Long objectId = bundle.getLong(CommonConstants.BUILDING_ID);
            fragment = InsertFragment.newInstance(objectId);
        }
        return fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(1, 1, 1, TAG).setIcon(android.R.drawable.ic_menu_camera).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public void onFragmentInteraction(int resultCode) {
        if (resultCode == RESULT_OK) {
            Intent i = new Intent(this, MainActivity.class);
            setResult(resultCode, i);
            finish();
        } else {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, resultCode);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        setResult(RESULT_CANCELED, i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        } else if (id == 1) {
            Fragment fragment = getFragment();
            ((InsertFragment)fragment).moveCamera();
        }
        return super.onOptionsItemSelected(item);
    }
}
