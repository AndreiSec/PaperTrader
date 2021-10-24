//package com.example.papertrader.ui.login;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import android.content.Context;
//import android.util.Patterns;
//
//import com.example.papertrader.data.LoginRepository;
//import com.example.papertrader.data.Result;
//import com.example.papertrader.data.model.LoggedInUser;
//import com.example.papertrader.R;
//
//public class LoginViewModel extends ViewModel {
//
//    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
//    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
//    private LoginRepository loginRepository;
//
//    LoginViewModel(LoginRepository loginRepository) {
//        this.loginRepository = loginRepository;
//    }
//
//    LiveData<LoginFormState> getLoginFormState() {
//        return loginFormState;
//    }
//    LiveData<LoginResult> getLoginResult() {
//        return loginResult;
//    }
//
//    public boolean isLoggedIn(Context context) {
//        // can be launched in a separate asynchronous job
//        return loginRepository.isLoggedIn(context);
//
//    }
//
//    public void logout(Context context) {
//        // can be launched in a separate asynchronous job
//        loginRepository.logout(context);
//
//    }
//
//    public void login(Context context, String email, String password) {
//        // can be launched in a separate asynchronous job
//        Result<LoggedInUser> result = loginRepository.login(context, email, password);
//
//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
//    }
//
//    public void register(Context context, String username, String email, String password) {
//        // can be launched in a separate asynchronous job
//        Result<LoggedInUser> result = loginRepository.register(context, username, email, password);
//
//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
//    }
//
//    public void loginDataChanged(String username, String password) {
//        if (!isUserNameValid(username)) {
//            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
//        } else if (!isPasswordValid(password)) {
//            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
//        } else {
//            loginFormState.setValue(new LoginFormState(true));
//        }
//    }
//
//    // A placeholder username validation check
//    private boolean isUserNameValid(String username) {
//        if (username == null) {
//            return false;
//        }
//        if (username.contains("@")) {
//            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
//        } else {
//            return !username.trim().isEmpty();
//        }
//    }
//
//    // A placeholder password validation check
//    private boolean isPasswordValid(String password) {
//        return password != null && password.trim().length() > 5;
//    }
//}
