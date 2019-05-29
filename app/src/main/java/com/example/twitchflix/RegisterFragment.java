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
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterFragment extends Fragment {

    Button signInButton, sendRegisterInfo;
    Fragment fragment;
    String username, password;
    String register_reponse = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        final SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();


        signInButton = rootView.findViewById(R.id.signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragments_container, fragment)
                        .commit();

            }
        });

        sendRegisterInfo = rootView.findViewById(R.id.register_button);
        sendRegisterInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText usernameEditText, passwordEditText;

                usernameEditText = rootView.findViewById(R.id.register_username);
                passwordEditText = rootView.findViewById(R.id.register_password);

                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                OkHttpHandler okHttpHandler = new OkHttpHandler();
                try {
                    register_reponse = okHttpHandler.execute(username, password).get();

                    switch (register_reponse){
                        case "success":
                            Toast.makeText(getContext(), "Sign Up Success", Toast.LENGTH_SHORT).show();
                            editor.putBoolean("Logged", true);
                            editor.commit();

                            fragment = new ProfileFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragments_container, fragment)
                                    .commit();

                            break;
                        case "fail_nick_already_exists":
                            Toast.makeText(getContext(), "Nick is already in use", Toast.LENGTH_SHORT).show();
                            break;
                        case "fail":
                            Toast.makeText(getContext(), "Not able to sign in", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        return rootView;
    }


    public class OkHttpHandler extends AsyncTask<String, String, String>{

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
                    .url("https://twitchflix-240014.appspot.com/webapi/register_user")
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
    }

}
