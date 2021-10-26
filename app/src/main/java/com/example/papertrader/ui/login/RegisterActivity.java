package com.example.papertrader.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.papertrader.MainActivity;
import com.example.papertrader.ui.login.AuthHandler;
import com.example.papertrader.ui.login.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.papertrader.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private FloatingActionButton button_back;
//    private LoginViewModel loginViewModel;
    private ImageView viewPasswordButton;
    private Button already_member;
    private AuthHandler authHandler;
//    private FirebaseAuth mAuth;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.textentry_register_username);
        emailEditText = findViewById(R.id.textentry_register_email);
        passwordEditText = findViewById(R.id.textentry_register_password);
        final Button registerButton = findViewById(R.id.button_register_register);

        authHandler = new AuthHandler(this, this);

//        mAuth = FirebaseAuth.getInstance();


        already_member = findViewById(R.id.button_register_already_member);
        already_member.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openSignInActivity();
            }
        });




        viewPasswordButton = findViewById(R.id.imageView_register_viewPassword);
        viewPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("Alert: ", "View Password Button Pressed");

                if(passwordEditText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    //Show Password
                    viewPasswordButton.setColorFilter(ContextCompat.getColor(viewPasswordButton.getContext(), R.color.lightBlue), android.graphics.PorterDuff.Mode.MULTIPLY);
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    //Hide Password
                    viewPasswordButton.setColorFilter(ContextCompat.getColor(viewPasswordButton.getContext(), R.color.darkBlue), android.graphics.PorterDuff.Mode.MULTIPLY);
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
                passwordEditText.setSelection(passwordEditText.length()); // Reset Cursor back to original position after input type change

            }
        });

        button_back = (FloatingActionButton) findViewById(R.id.button_register_back);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish(); // Ends activity and returns to the previous

            }
        });



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                registerUser(usernameEditText.getText().toString(),emailEditText.getText().toString(),
                        passwordEditText.getText().toString());


                return;
//                authHandler.register(getApplicationContext(), usernameEditText.getText().toString(),emailEditText.getText().toString(),
//                        passwordEditText.getText().toString());
            }
        });




    }

    public void registerUser(String username, String email, String password){
        boolean errorOccurred = false;

        FormResult usernameResult = AuthHandler.isUserNameValid(username);
        FormResult emailResult = AuthHandler.isEmailValid(email);
        FormResult passwordResult = AuthHandler.isPasswordValid(password);


        if(!usernameResult.result){
            usernameEditText.setError(usernameResult.message);
            errorOccurred = true;
        }
        if(!emailResult.result){
            emailEditText.setError(emailResult.message);
            errorOccurred = true;
        }
        if(!passwordResult.result){
            passwordEditText.setError(passwordResult.message);
            errorOccurred = true;
        }

        // Do not continue with registration if an error occurred.
        if(errorOccurred) return;

        // If no error occurred, use the AuthHandler to handle registration

        authHandler.register(username, email, password);



    }



    public void openSignInActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



    public void openMainActivity(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

//    private void showLoginFailed(@StringRes Integer errorString) {
//        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
//    }


//    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }

    //        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
//            @Override
//            public void onChanged(@Nullable LoginFormState loginFormState) {
//                if (loginFormState == null) {
//                    return;
//                }
//                registerButton.setEnabled(loginFormState.isDataValid());
//                if (loginFormState.getUsernameError() != null) {
//                    emailEditText.setError(getString(loginFormState.getUsernameError()));
//                }
//                if (loginFormState.getPasswordError() != null) {
//                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
//                }
//            }
//        });



//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
//            }
//        });
//
//        TextWatcher afterTextChangedListener = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // ignore
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // ignore
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                loginViewModel.loginDataChanged(emailEditText.getText().toString(),
//                        passwordEditText.getText().toString());
//            }
//        };
//        emailEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//                    registerUser(usernameEditText.getText().toString(),emailEditText.getText().toString(),
//                            passwordEditText.getText().toString());
//
////                    authHandler.register(getApplicationContext(), usernameEditText.getText().toString(),emailEditText.getText().toString(),
////                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });




}
