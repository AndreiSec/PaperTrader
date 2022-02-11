package com.example.papertrader.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.papertrader.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StockActivity extends AppCompatActivity {

    private FloatingActionButton button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);


        button_back = findViewById(R.id.button_signin_back);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish(); // Ends activity and returns to the previous

            }
        });
    }
}
