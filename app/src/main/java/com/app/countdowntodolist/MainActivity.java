package com.app.countdowntodolist;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.app.countdowntodolist.Function.switchFragment;
import com.app.countdowntodolist.Model.Task;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    CoordinatorLayout rootLayout;

    @Override
    public void onResume() {
        super.onResume();
        collapsingToolbarLayout.setTitle("Countdown 365");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parseLogIn();
        initToolbar();
        initInstances();


        setFirstState(savedInstanceState);
    }

    private void setFirstState(Bundle state) {
        // if instance state is null choose first nav item (position = 0) for display
        if (state == null) {
            displayView(0);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new MainList_Fragment();
                break;

            default:
                fragment = new MainList_Fragment();
                break;
        }

        //create fragment manager for manage to switching fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        new switchFragment(fragment, fragmentManager).doSwitch();

    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initInstances() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);


        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
/*        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    private void parseLogIn() {

        // start initial parse with key
        Parse.initialize(this, "SIzGKpVr1ITnt629TkiQZfxSQE6VA2HddvwbdJQ4", "36WLOG5LBo0AcmIivQRdqUoHbdF1MNJj56zUNyoq");
        //parse analytic is track
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        //register class use in project such as this project use table Car in parse also register subclass Car (in Project)
        ParseObject.registerSubclass(Task.class);

        // get user data if null go to login
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
