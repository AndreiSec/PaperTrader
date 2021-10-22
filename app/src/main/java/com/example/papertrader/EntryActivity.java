package com.example.papertrader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.papertrader.ui.login.LoginActivity;

public class EntryActivity extends AppCompatActivity {

    private Button signin_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        signin_button = (Button) findViewById(R.id.button_signin);
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


}
