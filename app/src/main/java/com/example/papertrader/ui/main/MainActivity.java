package com.example.papertrader.ui.main;

import android.content.ContentValues;
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
import androidx.fragment.app.FragmentManager;

import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button logout_button;
    private Button api_test_button;
//    private LoginViewModel loginViewModel;
    public ApiConnection apiConnection;
//    private ApiConnection apiConnection;
    private BottomNavigationView bottomNav;

    private static final String TAG_FRAGMENT_MARKET = "fragment_market";
    private static final String TAG_FRAGMENT_HOLDINGS = "fragment_holdings";
    private static final String TAG_FRAGMENT_ACCOUNT = "fragment_account";

    private final MarketFragment marketFragment = MarketFragment.getInstance();

    private FragmentManager fragmentManager;
    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();


        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(bottomNavMethod);
        bottomNav.setSelectedItemId(R.id.nav_market);





//        System.out.println("Market fragment set...");
//        System.out.println(marketFragment.toString());
//        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);



    }

    protected void onStop() {
        // call the superclass method first
        super.onStop();
        System.out.println("Main Activity Stopping..");
        marketFragment.deleteInstance();

    }


//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        //Save the fragment's instance
//        getSupportFragmentManager().putFragment(outState, TAG_FRAGMENT_MARKET, mMyFragment);
//    }

    // Method to switch fragments on the bottom nav bar
    private final NavigationBarView.OnItemSelectedListener bottomNavMethod=new NavigationBarView.OnItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
            Fragment fragment = null;

            switch(menuItem.getItemId()){


                case R.id.nav_market:
                    fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_MARKET);
                    if (fragment == null) {
                        fragment = MarketFragment.getInstance();
                    }
                    replaceFragment(fragment, TAG_FRAGMENT_MARKET);
                    break;

                case R.id.nav_holdings:
                    fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_HOLDINGS);
                    if (fragment == null) {
                        fragment = new HoldingsFragment();
                    }
                    replaceFragment(fragment, TAG_FRAGMENT_HOLDINGS);
                    break;

                case R.id.nav_account:
                    fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_ACCOUNT);
                    if (fragment == null) {
                        fragment = new AccountFragment();
                    }
                    replaceFragment(fragment, TAG_FRAGMENT_ACCOUNT);
                    break;
            }
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            return true;
        }
    };

    private void replaceFragment(@NonNull Fragment fragment, @NonNull String tag) {
        if (!fragment.equals(currentFragment)) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .commit();
            currentFragment = fragment;
        }
    }

}
