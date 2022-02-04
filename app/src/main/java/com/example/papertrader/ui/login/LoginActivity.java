package com.example.papertrader.ui.login;

import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.papertrader.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LoginActivity extends AppCompatActivity {
    private FloatingActionButton button_back;
//    private LoginViewModel loginViewModel;
    private AuthHandler authHandler;
    private ImageView viewPasswordButton;
    private Button forgotPasswordButton;

    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
//                .get(LoginViewModel.class);

        emailEditText = findViewById(R.id.textentry_signin_email);
       passwordEditText = findViewById(R.id.textentry_signin_password);
//        passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final Button loginButton = findViewById(R.id.button_signin_signin);

        authHandler = new AuthHandler(this, this);

        viewPasswordButton = findViewById(R.id.imageView_login_viewPassword);
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

        button_back = findViewById(R.id.button_signin_back);
        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openEntryActivity(); // Ends activity and returns to the previous

            }
        });




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());

                return;
            }
        });


        forgotPasswordButton = findViewById(R.id.button_signin_forgot_password);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openForgotPasswordActivity();

            }
        });

    }


    public void loginUser(String email, String password){
        boolean errorOccurred = false;

        FormResult emailResult = AuthHandler.isEmailValid(email);


        if(!emailResult.result){
            emailEditText.setError(emailResult.message);
            errorOccurred = true;
        }
        if (password.length() == 0) {
            passwordEditText.setError("Password cannot be blank.");
            errorOccurred = true;
        }


        // Do not continue with registration if an error occurred.
        if(errorOccurred) return;

        // If no error occurred, use the AuthHandler to handle registration

        authHandler.login(email, password);

    }




    public void openEntryActivity(){
        Intent intent = new Intent(this, EntryActivity.class);
        startActivity(intent);
    }


    public void openForgotPasswordActivity(){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }




    //        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
//            @Override
//            public void onChanged(@Nullable LoginFormState loginFormState) {
//                if (loginFormState == null) {
//                    return;
//                }
//                loginButton.setEnabled(loginFormState.isDataValid());
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

//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    authHandler.login(getApplicationContext(), emailEditText.getText().toString(),
//                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });

    //    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }

//    private void showLoginFailed(@StringRes Integer errorString) {
//        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
//    }
}
