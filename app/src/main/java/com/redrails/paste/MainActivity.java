package com.redrails.paste;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        TinyDB tdb = new TinyDB(this);
        Intent intent = getIntent();
        if(Intent.ACTION_SEND.equals(intent.getAction()) && intent.getType() != null){
            if("text/plain".equals(intent.getType())){
                handleSendText(intent);
                return;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new MainFrame()).commit();
        navigationView.setCheckedItem(R.id.paste_home);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            Intent intent = new Intent();
//            intent.setClassName(this, "com.redrails.paste.SettingsActivity");
//            this.startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fm = getFragmentManager();


        int id = item.getItemId();

        if (id == R.id.paste_home) {

            fm.beginTransaction().replace(R.id.content_frame, new MainFrame()).commit();
            navigationView.setCheckedItem(R.id.paste_home);
        } else if (id == R.id.paste_make) {

            fm.beginTransaction().replace(R.id.content_frame, new MakePasteFragment()).commit();
            navigationView.setCheckedItem(R.id.paste_make);

        } else if (id == R.id.paste_view) {
            fm.beginTransaction().replace(R.id.content_frame, new ViewPasteFragment()).commit();
            navigationView.setCheckedItem(R.id.paste_view);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleSendText(Intent intent){
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        FragmentManager fm = getFragmentManager();
        if(sharedText != null){
            Bundle bundle = new Bundle();
            bundle.putString("sharedpaste", sharedText);
            MakePasteFragment mp = new MakePasteFragment();
            mp.setArguments(bundle);
            fm.beginTransaction().replace(R.id.content_frame, mp).commit();
            navigationView.setCheckedItem(R.id.paste_make);
        }
    }

}
