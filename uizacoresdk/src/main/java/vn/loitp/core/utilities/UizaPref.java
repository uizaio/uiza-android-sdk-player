package vn.loitp.core.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import vn.loitp.core.common.Constants;
import vn.loitp.restapi.uiza.model.v2.auth.Auth;
import vn.loitp.restapi.uiza.model.v3.UizaWorkspaceInfo;
import vn.loitp.restapi.uiza.model.v3.authentication.gettoken.ResultGetToken;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * File created on 11/15/2016.
 *
 * @author loitp
 */
public class UizaPref {
    private String TAG = getClass().getSimpleName();

    private final static String PREFERENCES_FILE_NAME = "loitp";
    private final static String CHECK_APP_READY = "CHECK_APP_READY";
    private final static String PRE_LOAD = "PRE_LOAD";
    private final static String SLIDE_UIZA_VIDEO_ENABLED = "SLIDE_UIZA_VIDEO_ENABLED";
    private final static String INDEX = "INDEX";
    private final static String AUTH = "AUTH";
    public final static String API_END_POINT = "API_END_POINT";
    private final static String API_TRACK_END_POINT = "API_TRACK_END_POINT";
    private final static String TOKEN = "TOKEN";
    private final static String CLICKED_PIP = "CLICKED_PIP";
    private final static String ACITIVITY_CAN_SLIDE_IS_RUNNING = "ACITIVITY_CAN_SLIDE_IS_RUNNING";
    private final static String CLASS_NAME_OF_PLAYER = "CLASS_NAME_OF_PLAYER";

    //for api v3
    private final static String V3UIZAWORKSPACEINFO = "V3UIZAWORKSPACEINFO";
    private final static String V3UIZATOKEN = "V3UIZATOKEN";
    private final static String V3DATA = "V3DATA";
    //end for api v3

    //object
    public static UizaWorkspaceInfo getUizaWorkspaceInfo(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return new Gson().fromJson(pref.getString(V3UIZAWORKSPACEINFO, ""), UizaWorkspaceInfo.class);
    }

    public static void setUizaWorkspaceInfo(Context context, UizaWorkspaceInfo uizaWorkspaceInfo) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(V3UIZAWORKSPACEINFO, new Gson().toJson(uizaWorkspaceInfo));
        editor.apply();
    }

    public static ResultGetToken getResultGetToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return new Gson().fromJson(pref.getString(V3UIZATOKEN, ""), ResultGetToken.class);
    }

    public static void setResultGetToken(Context context, ResultGetToken resultGetToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(V3UIZATOKEN, new Gson().toJson(resultGetToken));
        editor.apply();
    }

    /////////////////////////////////STRING
    public static String getApiEndPoint(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return pref.getString(API_END_POINT, null);
    }

    public static void setApiEndPoint(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(API_END_POINT, value);
        editor.apply();
    }

    public static String getApiTrackEndPoint(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return pref.getString(API_TRACK_END_POINT, null);
    }

    public static void setApiTrackEndPoint(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(API_TRACK_END_POINT, value);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return pref.getString(TOKEN, null);
    }

    public static void setToken(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(TOKEN, value);
        editor.apply();
    }

    public static String getClassNameOfPlayer(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return pref.getString(CLASS_NAME_OF_PLAYER, null);
    }

    public static void setClassNameOfPlayer(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(CLASS_NAME_OF_PLAYER, value);
        editor.apply();
    }
    /////////////////////////////////BOOLEAN

    public static Boolean getCheckAppReady(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(CHECK_APP_READY, false);
    }

    public static void setCheckAppReady(Context context, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(CHECK_APP_READY, value);
        editor.apply();
    }

    public static Boolean getPreLoad(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(PRE_LOAD, false);
    }

    public static void setPreLoad(Context context, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(PRE_LOAD, value);
        editor.apply();
    }

    public static Boolean getSlideUizaVideoEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(SLIDE_UIZA_VIDEO_ENABLED, false);
    }

    public static void setSlideUizaVideoEnabled(Context context, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(SLIDE_UIZA_VIDEO_ENABLED, value);
        editor.apply();
    }

    public static Boolean getClickedPip(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(CLICKED_PIP, false);
    }

    public static void setClickedPip(Context context, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(CLICKED_PIP, value);
        editor.apply();
    }

    public static Boolean getAcitivityCanSlideIsRunning(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getBoolean(ACITIVITY_CAN_SLIDE_IS_RUNNING, false);
    }

    public static void setAcitivityCanSlideIsRunning(Context context, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putBoolean(ACITIVITY_CAN_SLIDE_IS_RUNNING, value);
        editor.apply();
    }

    /////////////////////////////////INT
    public static int getIndex(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        return prefs.getInt(INDEX, Constants.NOT_FOUND);
    }

    public static void setIndex(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putInt(INDEX, value);
        editor.apply();
    }

    //Object
    public static Auth getAuth(Context context, Gson gson) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        String json = pref.getString(AUTH, null);
        return gson.fromJson(json, Auth.class);
    }

    public static void setAuth(Context context, Auth auth, Gson gson) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(AUTH, gson.toJson(auth));
        editor.apply();
    }

    public static Data getData(Context context, Gson gson) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
        String json = pref.getString(V3DATA, null);
        return gson.fromJson(json, Data.class);
    }

    public static void setData(Context context, Data data, Gson gson) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0).edit();
        editor.putString(V3DATA, gson.toJson(data));
        editor.apply();
    }
}