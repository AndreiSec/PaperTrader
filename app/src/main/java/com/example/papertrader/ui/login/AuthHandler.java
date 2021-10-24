package com.example.papertrader.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.papertrader.MainActivity;
import com.example.papertrader.R;
//import com.example.papertrader.data.LoginDataSource;
//import com.example.papertrader.data.LoginRepository;
//import com.example.papertrader.data.Result;
import com.example.papertrader.data.model.LoggedInUser;
import com.example.papertrader.ui.login.EntryActivity;
import com.example.papertrader.ui.login.LoginActivity;
import com.example.papertrader.ui.login.RegisterActivity;
import com.example.papertrader.ui.login.FormResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-disk cache of login status and user credentials information.
 */
public class AuthHandler {

    FirebaseAuth mAuth;

//    public Context context;


    // Username validation check
    public static FormResult isUserNameValid(String username) {
        if (username.length() == 0) {
            return new FormResult(false, "Username cannot be blank.");
        }
        if(username.length() < 3){
            return new FormResult(false, "Username must be longer than 3 characters.");
        }

        return new FormResult(true, "Success");



    }

    // Email validation check
    public static FormResult isEmailValid(String email) {
        if (email.length() == 0) {
            return new FormResult(false, "Email cannot be blank.");
        }
        if(email.length() < 3){
            return new FormResult(false, "Email must be longer than 3 characters.");
        }

        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(email);

        if(matcher.matches()){
            return new FormResult(true, "Success");
        }
        else{
            return new FormResult(false, "Not a valid email.");
        }

    }

    // Password validation check
    public static FormResult isPasswordValid(String password) {
        if (password.length() == 0) {
            return new FormResult(false, "Password cannot be blank.");
        }
        if(password.length() < 6){
            return new FormResult(false, "Password must be 6 or more characters.");
        }

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        if(matcher.matches()){
            return new FormResult(true, "Success");
        }
        else{
            return new FormResult(false, "Password must have an uppercase, lowercase, special, and number.");
        }

    }



    // Gets the authentication token from shared preferences
    public static String getAuthToken(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_auth_key), Context.MODE_PRIVATE);

        String authKey = sharedPref.getString(context.getString(R.string.preference_auth_key), null);


        return authKey;
    }

    // Gets the username from shared preferences
    public static String getUserName(Context context){

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_user_name), Context.MODE_PRIVATE);

        String userName = sharedPref.getString(context.getString(R.string.preference_auth_key), null);

        return userName;

    }


    // Checks if user is logged in
    public boolean isLoggedIn(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_auth_key), Context.MODE_PRIVATE);

        String authKey = sharedPref.getString(context.getString(R.string.preference_auth_key), null);

        return authKey != null;
    }

    // Logs user out of the app
    public void logout(Context context) {
//        dataSource.logout(context);

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_auth_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        // Remove key from shared preferences
        editor.remove(context.getString(R.string.preference_auth_key));
        editor.remove(context.getString(R.string.preference_user_name));

        editor.commit();
        Log.i("Alert: ", "Logged out and removed key from shared preferences");

        openEntryActivity(context);
    }


    // Sets the user logged in by caching token in shared preferences
    private void setLoggedInUserInSharedPreferences(Context context, LoggedInUser user) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_auth_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(context.getString(R.string.preference_auth_key), user.getUserId());
        editor.apply();

        Log.i("Alert: ", "Set user to logged in by storing auth token");

        openMainActivity(context);

    }

    private static void openMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
//        ((Activity) context).finish();

    }

//    public Context getContext(){
//
//        return this.context;
//    }




    public static void openEntryActivity(Context context){
        Intent intent = new Intent(context, EntryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
//        ((Activity) context).finish();
    }

    public void register(Context context, String username, String email, String password) {
        // handle registration
        mAuth = FirebaseAuth.getInstance();
//        this.context = context;

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(((Activity) context), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    setLoggedInUserInSharedPreferences(context, new LoggedInUser(mAuth.getCurrentUser().getUid(), username));
                    openMainActivity(context);

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Unable to sign up.",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void login(Context context, String email, String password) {
        // handle login
        mAuth = FirebaseAuth.getInstance();
//        this.context = context;

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(context, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            setLoggedInUserInSharedPreferences(context, new LoggedInUser(mAuth.getCurrentUser().getUid(),"tempUsername"));
                            openMainActivity(context);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}

