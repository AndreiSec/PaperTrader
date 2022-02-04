package com.example.papertrader.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.example.papertrader.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FloatingActionButton button_back;
    private Button resetPasswordButton;
    private AuthHandler authHandler;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        authHandler = new AuthHandler(this, this);

        emailEditText = findViewById(R.id.textentry_forgotpassword_email);

        resetPasswordButton = findViewById(R.id.button_forgotpassword_reset);

        resetPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            authHandler.sendPasswordResetEmail(emailEditText.getText().toString());

            }
        });



        button_back = findViewById(R.id.button_forgotpassword_back);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish(); // Go back to login activity

            }
        });




    }






}
