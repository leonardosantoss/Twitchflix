package com.example.twitchflix;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.getMenu().findItem(R.id.live_icon).setChecked(true); // start the app with live icon checked

        getSupportFragmentManager().beginTransaction() // start the app with the Live Fragment
                .replace(R.id.fragments_container, new LiveFragment())
                .commit();

        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                Fragment selectedFragment = null;

                // loop all items in bottom nav, update their state (Checked item will change color)
                for (int i = 0; i < bottomNavView.getMenu().size(); i++) {
                    MenuItem menuItem_aux = bottomNavView.getMenu().getItem(i);
                    boolean isChecked = menuItem_aux.getItemId() == menuItem.getItemId();
                    menuItem.setChecked(isChecked);
                }

                switch(menuItem.getItemId()){
                    case R.id.settings_icon:
                        selectedFragment = new SettingsFragment();
                        break;
                    case R.id.live_icon:
                        selectedFragment = new LiveFragment();
                        break;
                    case R.id.vod_icon:
                        selectedFragment = new VodsFragment();
                        break;
                    default:
                        break;

                }

                // Change fragment to the one the user selected on the bottomNavView
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragments_container, selectedFragment)
                        .commit();

                return false;
            }
        });
    }

}
