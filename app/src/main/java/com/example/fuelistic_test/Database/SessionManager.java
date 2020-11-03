package com.example.fuelistic_test.Database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //variables
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_DATEOFBIRTH = "dateOfBirth";
    public static final String KEY_PHONENUMBER = "phoneNo";
    public static final String KEY_ADDRESS = "address";


    public SessionManager(Context _context) {
        context = _context;
        userSession = context.getSharedPreferences("UserLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String fullName, String username, String email, String phoneNo, String password, String dateOfBirth, String gender, String address ) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_PHONENUMBER, phoneNo);
        editor.putString(KEY_DATEOFBIRTH, dateOfBirth);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_ADDRESS, address);

        editor.commit();
    }

    public HashMap<String, String> getUserDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_FULLNAME, userSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_USERNAME, userSession.getString(KEY_USERNAME, null));
        userData.put(KEY_EMAIL, userSession.getString(KEY_EMAIL, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_GENDER, userSession.getString(KEY_GENDER, null));
        userData.put(KEY_DATEOFBIRTH, userSession.getString(KEY_DATEOFBIRTH, null));
        userData.put(KEY_PHONENUMBER, userSession.getString(KEY_PHONENUMBER, null));
        userData.put(KEY_ADDRESS, userSession.getString(KEY_ADDRESS, null));

        return userData;
    }



    public boolean checkLogin() {
        if (userSession.getBoolean(IS_LOGIN, false)) {
            return true;

        } else {
            return false;
        }
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }

}
