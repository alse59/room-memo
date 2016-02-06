package com.example.wataru.room_memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wataru.common.CommonConstants;
import com.example.wataru.fragment.CardListFragment;
import com.example.wataru.fragment.PreviewConfirmFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CardListFragment.OnFragmentInteractionListener {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.building_table));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = getFragment();
        replaceFragment(fragment);
    }


    /**
     * 指定したフラグメントに入れ替える
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, fragment, TAG).commit();
    }

    public Fragment getFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = CardListFragment.newInstance();
        }
        return fragment;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
public static final int MENU_INSERT_ACTIVITY = 0;

    /**
     * オプションメニューを生成する
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menu.add(0, MENU_INSERT_ACTIVITY, 0, TAG)
                .setIcon(android.R.drawable.ic_menu_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    /**
     * 選択したオプションアイテムを検出する
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == MENU_INSERT_ACTIVITY) {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentByTag(TAG);
            if (fragment instanceof CardListFragment) {
                moveInsertActivity(new Bundle());
            } else if (fragment instanceof PreviewConfirmFragment) {
                ((PreviewConfirmFragment)fragment).showPCRegistDialog();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * インサートアクティビティに画面遷移する
     * @param bundle
     */
    public void moveInsertActivity(Bundle bundle) {
        Intent i = new Intent(this, InsertActivity.class);
        i.putExtras(bundle);
        startActivityForResult(i, CommonConstants.MAIN_TO_INSERT_CODE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        String title = null;
        if (id == R.id.building_table) {
            fragment = CardListFragment.newInstance();
            title = getString(R.string.building_table);
        } else if (id == R.id.pc_condition) {
            fragment = new PreviewConfirmFragment();
            title = getString(R.string.pc_condition);
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_manage) {
            fragment = CardListFragment.newInstance();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        getSupportActionBar().setTitle(title);
        replaceFragment(fragment);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        switch (requestCode) {
            case CommonConstants.MAIN_TO_INSERT_CODE:
                if (resultCode == RESULT_OK) {
                    // TODO 抽象的なクラスを使用していない
                    CardListFragment fragment = (CardListFragment)getFragment();
                    fragment.refleshCardList();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {
        moveInsertActivity(bundle);
    }
}
