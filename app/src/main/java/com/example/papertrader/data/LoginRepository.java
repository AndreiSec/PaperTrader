//package com.example.papertrader.data;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import com.example.papertrader.MainActivity;
//import com.example.papertrader.R;
//import com.example.papertrader.data.model.LoggedInUser;
//import com.example.papertrader.ui.login.EntryActivity;
//import com.example.papertrader.ui.login.LoginActivity;
//import com.example.papertrader.ui.login.RegisterActivity;
//
///**
// * Class that requests authentication and user information from the remote data source and
// * maintains an in-memory cache of login status and user credentials information.
// */
//public class LoginRepository {
//
//    private static volatile LoginRepository instance;
//    private Context context;
//    private LoginDataSource dataSource;
//
//    // If user credentials will be cached in local storage, it is recommended it be encrypted
//    // @see https://developer.android.com/training/articles/keystore
//    private LoggedInUser user = null;
//
//    // private constructor : singleton access
//    private LoginRepository(LoginDataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    public static LoginRepository getInstance(LoginDataSource dataSource) {
//        if (instance == null) {
//            instance = new LoginRepository(dataSource);
//        }
//        return instance;
//    }
//
//    public boolean isLoggedIn(Context context) {
//
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                context.getString(R.string.preference_auth_key), Context.MODE_PRIVATE);
//
//        String authKey = sharedPref.getString(context.getString(R.string.preference_auth_key), null);
//
//        return authKey != null;
//    }
//
//    public void logout(Context context) {
//        dataSource.logout(context);
//
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                context.getString(R.string.preference_auth_key), Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPref.edit();
//
//        // Remove key from shared preferences
//        editor.remove(context.getString(R.string.preference_auth_key));
//        editor.commit();
//        Log.i("Alert: ", "Logged out and removed key from shared preferences");
//
//        openEntryActivity(context);
//    }
//
//    private void setLoggedInUser(Context context, LoggedInUser user) {
//        this.user = user;
//
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                context.getString(R.string.preference_auth_key), Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPref.edit();
//
//        editor.putString(context.getString(R.string.preference_auth_key), user.getUserId());
//        editor.apply();
//
//        Log.i("Alert: ", "Set user to logged in by storing auth token");
//
//        openMainActivity(context);
//
//    }
//
//    public void openMainActivity(Context context){
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//
//    }
//
//
//    public void openEntryActivity(Context context){
//        Intent intent = new Intent(context, EntryActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
//
//    public Result<LoggedInUser> register(Context context, String username, String email, String password) {
//        // handle login
//        Result<LoggedInUser> result = dataSource.register(username, email, password);
//        if (result instanceof Result.Success) {
//            setLoggedInUser(context, ((Result.Success<LoggedInUser>) result).getData());
//        }
//        return result;
//    }
//
//    public Result<LoggedInUser> login(Context context, String email, String password) {
//        // handle login
//        Result<LoggedInUser> result = dataSource.login(email, password);
//        if (result instanceof Result.Success) {
//            setLoggedInUser(context, ((Result.Success<LoggedInUser>) result).getData());
//        }
//        return result;
//    }
//}
