package com.hypernymbiz.logistics.utils;


import android.content.Context;
import android.content.Intent;

import com.hypernymbiz.logistics.model.User;

public class LoginUtils {

    public static boolean isUserLogin(Context context) {
        return PrefUtils.getBoolean(context, Constants.USER_AVAILABLE, false);
    }

    public static void saveUser(Context context, User user) {
        if (user == null)
            return;

        PrefUtils.persistString(context, Constants.USER, GsonUtils.toJson(user));
    }

    public static User getUser(Context context) {
        return GsonUtils.fromJson(PrefUtils.getString(context, Constants.USER), User.class);
    }

    public static void saveUserToken(Context context, String token,String assosiated_entity) {
        if (token == null)
            return;

        PrefUtils.persistString(context, Constants.USER_TOKEN, token);
        PrefUtils.persistString(context, Constants.USER_ASSOSIATED_ENTITY, assosiated_entity);
    }

    public static String getUserToken(Context context) {
        return PrefUtils.getString(context, Constants.USER_TOKEN);
    }
    public static String getUserAssociatedEntity(Context context) {
        return PrefUtils.getString(context, Constants.USER_ASSOSIATED_ENTITY);
    }

    public static void userLoggedIn(Context context) {
        PrefUtils.persistBoolean(context, Constants.USER_AVAILABLE, true);
    }

    public static void clearUser(Context context) {
        PrefUtils.remove(context, Constants.USER_AVAILABLE);
        PrefUtils.remove(context, Constants.USER);

    }
}
