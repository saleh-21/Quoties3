package com.goldenkey.quoties;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static FragmentManager fm;
    private TextView mTextMessage;
    /*  Permission request code to draw over other apps  */
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            AuthUI.getInstance().signOut(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm = getSupportFragmentManager();
        final mainFragment homefragment = new mainFragment();
        fm.beginTransaction().
                replace(R.id.mainFragment, homefragment).
                commit();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fm = getSupportFragmentManager();
                mainFragment explore = new mainFragment();
                favFragment favorite= new favFragment();
                switch(item.getItemId()) {
                    case R.id.navigation_home: {
                        fm.beginTransaction().
                                replace(R.id.mainFragment, homefragment).
                                commit();
                        break;
                    } case R.id.navigation_dashboard: {
                        fm.beginTransaction().
                                replace(R.id.mainFragment, explore).
                                commit();
                        break;
                    } case R.id.navigation_favorite: {
                        fm.beginTransaction().
                                replace(R.id.mainFragment, favorite).
                                commit();
                        break;
                    }
                }
                return true;
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.explore);
                    return true;
                case R.id.navigation_favorite:
                    mTextMessage.setText(R.string.star_quotes);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_floating_widget: {
                createFloatingWidget();
                return true;
            }
            case R.id.action_add_quote: {
                Intent i = new Intent(this, addQuoteActivity.class);
                startActivity(i);
                return true;
            }
            case R.id.action_profile: {
                Intent i = new Intent(this, ProfileActivity.class);
                i.putExtra("profile", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(i);
                return true;
            }
            case R.id.action_logout: {
                AuthUI.getInstance().signOut(this);
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Floating Widget
    /*  start floating widget service  */
    public void createFloatingWidget() {

        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
        } else
            //If permission is granted start floating widget service
            startFloatingWidgetService();
    }

    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService() {

        startService(new Intent(MainActivity.this, FloatingWidgetService.class));
        finish();
    }
    private void endFloatingWidgetService() {
        startService(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK)
                //If permission granted start floating widget service
                startFloatingWidgetService();
            else
                //Permission is not available then display toast
                Toast.makeText(this,
                        getResources().getString(R.string.draw_other_app_permission_denied),
                        Toast.LENGTH_SHORT).show();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void startOnTime (){

        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
        } else
            //If permission is granted start floating widget service
            startFloatingWidgetService();

    }
}
