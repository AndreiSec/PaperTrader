package com.example.papertrader;

import android.os.Bundle;

import com.example.papertrader.ui.login.AuthHandler;
//import com.example.papertrader.data.LoginRepository;
//import com.example.papertrader.ui.login.LoginViewModel;
//import com.example.papertrader.ui.login.LoginViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.papertrader.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private Button logout_button;
//    private LoginViewModel loginViewModel;
    private AuthHandler authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());


        authHandler = new AuthHandler(this, this);

//        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);



        logout_button = (Button) findViewById(R.id.button_main_logout);
        logout_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                authHandler.logout();

            }

        });

    }
}
