//package com.example.papertrader.data;
//
//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//
//import com.example.papertrader.data.model.LoggedInUser;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import java.io.IOException;
//
///**
// * Class that handles authentication w/ login credentials and retrieves user information.
// */
//
//public class LoginDataSource {
//
//    FirebaseAuth mAuth;
//    FirebaseUser mUser;
//
//    public Result<LoggedInUser> register(String username, String email, String password) {
//
//        try {
//            mAuth = FirebaseAuth.getInstance();
//            mUser = mAuth.getCurrentUser();
//            LoggedInUser newUser;
//            mAuth.createUserWithEmailAndPassword(email, password);
//            mAuth.signInWithEmailAndPassword(email, password);
//            newUser =
//                    new LoggedInUser(
//                            mAuth.getCurrentUser().getUid(),
//                            username);
//            Log.i("Alert: ", mAuth.getCurrentUser().getUid() + " " + username);
//
//            return new Result.Success<>(newUser);
//
//        } catch (Exception e) {
//            Log.e("Error Signing up: ", e.toString());
//            return new Result.Error(new IOException("Error logging in", e));
//        }
//    }
//
//    public Result<LoggedInUser> login(String email, String password) {
//
//        try {
//            mAuth = FirebaseAuth.getInstance();
//            mUser = mAuth.getCurrentUser();
//
//            try{
//                mAuth.signInWithEmailAndPassword(email, password);
//                LoggedInUser loggedInUser =
//                        new LoggedInUser(
//                                mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getDisplayName());
//                return new Result.Success<>(loggedInUser);
//            }
//            catch(Exception e){
//                Log.e("Error Logging in: ", e.toString());
//                return new Result.Error(new IOException("Error logging in", e));
//            }
//
//
//        } catch (Exception e) {
//            return new Result.Error(new IOException("Error logging in", e));
//        }
//    }
//
//    public void logout(Context context) {
//        mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
//    }
//}
