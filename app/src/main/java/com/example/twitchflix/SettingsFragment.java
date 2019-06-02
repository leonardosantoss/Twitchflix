package com.example.twitchflix;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;


public class SettingsFragment extends PreferenceFragmentCompat {
    private SharedPreferences.OnSharedPreferenceChangeListener listener;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if(key.equals("option_1")){
                    Toast.makeText(getContext(), "Option 1", Toast.LENGTH_SHORT).show();
                }
                else if(key.equals("option_2")){
                    Toast.makeText(getContext(), "Option_2", Toast.LENGTH_SHORT).show();
                }
            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);


    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

    // permissions tutorial https://demonuts.com/android-runtime-permissions/
}
