package com.example.papertrader.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.papertrader.R;
import com.example.papertrader.ui.login.EntryActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VerificationEmailSentActivity extends AppCompatActivity {


    private FloatingActionButton button_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_email_sent);

        button_back = findViewById(R.id.button_verificationscreen_back);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openEntryActivity(); // Ends activity and returns to the previous

            }
        });

    }


    public void openEntryActivity(){
        Intent intent = new Intent(this, EntryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }



}
