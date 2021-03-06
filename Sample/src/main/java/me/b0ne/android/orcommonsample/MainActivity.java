package me.b0ne.android.orcommonsample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        setTitle(getResources().getString(R.string.app_name));

        Intent intent = getIntent();
        if (savedInstanceState == null) {
            if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SEND)) {
                if (intent.getType().startsWith("image/")) {
                    Uri imgUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                    Log.v("TEST", "EXTRA_STREAM = " + imgUri);
                    mFragmentManager.beginTransaction()
                            .replace(R.id.main_content, OptimizeBitmapFragment.newInstance(imgUri))
                            .commit();
                }
            } else {

                    mFragmentManager.beginTransaction()
                            .replace(R.id.main_content, new MenuListFragment())
                            .commit();
                }
        }

        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                ActionBar actionBar = getSupportActionBar();
                if (mFragmentManager.getBackStackEntryCount() == 0) {
                    setTitle(getResources().getString(R.string.app_name));
                    actionBar.setDisplayHomeAsUpEnabled(false);
                } else {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                if (mFragmentManager.getBackStackEntryCount() > 0) {
                    mFragmentManager.popBackStack();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mFragmentManager.getBackStackEntryCount() > 0) {
                mFragmentManager.popBackStack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
