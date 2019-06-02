package com.example.twitchflix;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileFragment extends Fragment {
    public boolean isLogged = false;
    View rootView;
    String username = null, password = null, login_response=null;
    Button sendLoginInfo, registerButton, logoutButton, deleteUserButton;
    TextView username_textView;
    Fragment fragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();

        isLogged = pref.getBoolean("Logged", false);
        username = pref.getString("Username", "default");

        if(isLogged){
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);

            username_textView = rootView.findViewById(R.id.username_textView);
            username_textView.setText("Hello, " + username);
            deleteUserButton = rootView.findViewById(R.id.delete_user_button);
            logoutButton = rootView.findViewById(R.id.logout_button);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Logout!");
                    editor.putBoolean("Logged", false);
                    editor.putString("Username", "default");
                    editor.commit();
                    fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragments_container);
                    final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.detach(fragment);
                    ft.attach(fragment);
                    ft.commit();
                }
            });


            deleteUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Account deleted!");
                    editor.putBoolean("Logged", false);
                    editor.putString("Username", "default");
                    editor.commit();
                    DeleteUser deleteUser = new DeleteUser();
                    deleteUser.execute(username);
                    fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragments_container);
                    final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.detach(fragment);
                    ft.attach(fragment);
                    ft.commit();
                }
            });

        }
        else{

            rootView = inflater.inflate(R.layout.fragment_profile_sign, container, false);
            sendLoginInfo = rootView.findViewById(R.id.login_button);
            registerButton = rootView.findViewById(R.id.register_button);

            sendLoginInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OkHttpHandler okHttpHandler = new OkHttpHandler();
                    EditText usernameEditText = rootView.findViewById(R.id.login_username);
                    EditText passwordEditText = rootView.findViewById(R.id.login_password);

                    username = usernameEditText.getText().toString();
                    password = passwordEditText.getText().toString();

                    try {
                        login_response = okHttpHandler.execute(username, password).get();
                        switch (login_response){
                            case "success":
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                editor.putBoolean("Logged", true);
                                editor.putString("Username", username);
                                editor.commit();
                                isLogged = pref.getBoolean("Logged", false);
                                username = pref.getString("Username", "default");
                                fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragments_container);
                                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        ft.detach(fragment);
                                        ft.attach(fragment);
                                        ft.commit();
                                break;
                            case "fail":
                                Toast.makeText(getContext(), "FAIL", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fragment = new RegisterFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragments_container, fragment)
                            .commit();
                }
            });

        }

        return rootView;
    }




    public class OkHttpHandler extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            username = strings[0];
            password = strings[1];

            RequestBody formBody = new FormBody.Builder()
                    .add("nick", username + "")
                    .add("passwd", password + "")
                    .build();
            Request request = new Request.Builder()
                    .url("https://twitchflix-240014.appspot.com/webapi/login")
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                System.out.println("Handle Response");
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    public class DeleteUser extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            username = strings[0];

            RequestBody formBody = new FormBody.Builder()
                    .add("nick", username + "")
                    .build();
            Request request = new Request.Builder()
                    .url("https://twitchflix-240014.appspot.com/webapi/delete_user")
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                System.out.println("Handle Response");
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
