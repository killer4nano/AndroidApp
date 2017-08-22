package com.example.kille.ostdeploy;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    private boolean loggedIn = false;
    private ServerCommunication serverCommunication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Login");
        setContentView(R.layout.activity_main);
        setTitle("Login");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loggedIn = true;
        if (ServerCommunication.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, Main2Activity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("tasks");
    }


    public void login(View v) {
        String usernameS = username.getText().toString();
        String passwordS = password.getText().toString();
        try {
            if (ServerCommunication.login(usernameS, passwordS)) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                Transfer.setClient(usernameS);
            } else {
                showMessage("Wrong password or username!");
            }
        }catch (Exception e) {
            Log.e("TEST",e.getMessage());
            showMessage("Some stupid error! try again!");
        }

    }


    public void showMessage(String message) {
        Snackbar snack = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        snack.show();
    }
}