package com.example.papertrader.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.papertrader.MainActivity;
import com.example.papertrader.R;
import com.example.papertrader.ui.login.AuthHandler;

public class EntryActivity extends AppCompatActivity {

    private Button signin_button;
    private Button register_button;
//    private LoginViewModel loginViewModel;
    private AuthHandler authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authHandler = new AuthHandler();

        if(authHandler.isLoggedIn(getApplicationContext())){
            Log.i("Alert: ", "Already logged in. Switching to main activity.");
            openMainActivity();
        }



        setContentView(R.layout.activity_entry);

        register_button = (Button) findViewById(R.id.button_entry_register);
        register_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRegisterActivity();
            }

        });



        signin_button = (Button) findViewById(R.id.button_entry_signin);
        signin_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openSignInActivity();

            }

        });
    }

    public void openSignInActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    public void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
