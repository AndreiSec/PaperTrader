package com.example.papertrader.ui.main;

import android.os.Bundle;

import com.example.papertrader.R;
import com.example.papertrader.ui.fragments.AccountFragment;
import com.example.papertrader.ui.fragments.HoldingsFragment;
import com.example.papertrader.ui.fragments.MarketFragment;
import com.example.papertrader.ui.login.AuthHandler;
import com.example.papertrader.api.ApiConnection;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
//import com.example.papertrader.data.LoginRepository;
//import com.example.papertrader.ui.login.LoginViewModel;
//import com.example.papertrader.ui.login.LoginViewModelFactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button logout_button;
    private Button api_test_button;
//    private LoginViewModel loginViewModel;
    private AuthHandler authHandler;
    private ApiConnection apiConnection;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        authHandler = new AuthHandler(this, this);
        apiConnection = new ApiConnection();

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(bottomNavMethod);
        bottomNav.setSelectedItemId(R.id.nav_market);

//        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);




//        api_test_button = (Button) findViewById(R.id.button_test_api);
//        api_test_button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                apiConnection.get_all_stock_info();
//
//            }
//
//        });


//        logout_button = (Button) findViewById(R.id.button_main_logout);
//        logout_button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                authHandler.logout();
//
//            }
//
//        });

    }

    // Method to switch fragments on the bottom nav bar
    private NavigationBarView.OnItemSelectedListener bottomNavMethod=new NavigationBarView.OnItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){

            Fragment fragment = null;
//            System.out.println(menuItem.getItemId());
            switch(menuItem.getItemId()){

                case R.id.nav_market:
                    fragment = new MarketFragment();
                    break;

                case R.id.nav_holdings:
                    fragment = new HoldingsFragment();
                    break;

                case R.id.nav_account:
                    fragment = new AccountFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            return true;
        }
    };



}
