package com.example.papertrader.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.papertrader.ui.main.MainActivity;
import com.example.papertrader.R;
//import com.example.papertrader.data.LoginDataSource;
//import com.example.papertrader.data.LoginRepository;
//import com.example.papertrader.data.Result;
import com.example.papertrader.api.ApiConnection;
import com.example.papertrader.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-disk cache of login status and user credentials information.
 */
public class AuthHandler {

    FirebaseAuth mAuth;
    private ApiConnection apiConnection;

    private ProgressBar loadingProgressBar;
    private final Context context;
    private final Activity activity;

//    public Context context;

    public AuthHandler(Context context, Activity activity){
        this.context = context;
        this.activity = activity;

    }




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


    public void sendPasswordResetEmail(String email){
        mAuth = FirebaseAuth.getInstance();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, context.getString(R.string.forgot_password_reset_sent) + email,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context,  task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }


    // Gets the authentication token from shared preferences
    public String getAuthToken(){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_auth_key), Context.MODE_PRIVATE);

        String authKey = sharedPref.getString(context.getString(R.string.preference_auth_key), null);


        return authKey;
    }

    // Gets the username from shared preferences
    public String getUserName(){

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_user_name), Context.MODE_PRIVATE);

        String userName = sharedPref.getString(context.getString(R.string.preference_auth_key), null);

        return userName;

    }


    // Checks if user is logged in
    public boolean isLoggedIn() {

        String authKey = getAuthToken();

        return authKey != null;


    }

    // Logs user out of the app
    public void logout() {
//        dataSource.logout(context);
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_auth_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        // Remove key from shared preferences
        editor.remove(context.getString(R.string.preference_auth_key));
        editor.remove(context.getString(R.string.preference_user_name));

        editor.commit();
        Log.i("Alert: ", "Logged out and removed key from shared preferences");

        openEntryActivity();
    }


    // Sets the user logged in by caching token in shared preferences
    private void setLoggedInUserInSharedPreferences(LoggedInUser user) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_auth_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(context.getString(R.string.preference_auth_key), user.getUserId());
        editor.apply();

        Log.i("Alert: ", "Set user to logged in by storing auth token");

        openMainActivity();

    }

    private void openMainActivity(){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
//        ((Activity) context).finish();

    }

//    public Context getContext(){
//
//        return this.context;
//    }




    public void openEntryActivity(){
        Intent intent = new Intent(context, EntryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
//        ((Activity) context).finish();
    }

    public void openEmailVerifyActivity(){
        Intent intent = new Intent(context, VerificationEmailSentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
//        ((Activity) context).finish();
    }

    private void sendVerificationEmail(FirebaseUser user)
    {

        user.sendEmailVerification()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            // Navigate to sent verification email page
                            openEmailVerifyActivity();


                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            Toast.makeText(context, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void register(String username, String email, String password) {
        // handle registration
        mAuth = FirebaseAuth.getInstance();
//        this.context = context;

        // Set loading circle to visible
        loadingProgressBar = activity.findViewById(R.id.register_loading_bar);
        loadingProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Set loading bar invisible
                loadingProgressBar.setVisibility(View.INVISIBLE);

                if (task.isSuccessful()) {
                    // Sign up success, send email verification

                    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();

                    mUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        String username = mUser.getDisplayName();
                                        String email = mUser.getEmail();
                                        String uid = mUser.getUid();
                                        Log.i("Register info: ", username + " " + email + " " + uid);

                                        apiConnection = ApiConnection.getInstance();
                                        apiConnection.create_user_in_database(uid, username, email);
                                    }
                                }
                            });



                    sendVerificationEmail(mUser);



//                    setLoggedInUserInSharedPreferences(new LoggedInUser(mAuth.getCurrentUser().getUid(), username));
//                    openMainActivity();

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void login(String email, String password) {
        // handle login
        mAuth = FirebaseAuth.getInstance();
//        this.context = context;

        // Set loading circle to visible
        loadingProgressBar = activity.findViewById(R.id.login_loading_bar);
        loadingProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // Set loading bar invisible
                        loadingProgressBar.setVisibility(View.INVISIBLE);

                        FirebaseUser mUser = mAuth.getCurrentUser();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            if(mUser.isEmailVerified()){
                                Toast.makeText(context, "Authentication successful.",
                                        Toast.LENGTH_SHORT).show();
                                setLoggedInUserInSharedPreferences(new LoggedInUser(mUser.getUid(),mUser.getDisplayName()));
                                openMainActivity();
                            }
                            else{
                                Toast.makeText(context, "Please verify your email!",
                                        Toast.LENGTH_SHORT).show();
                            }



                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}

