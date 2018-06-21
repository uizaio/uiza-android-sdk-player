package vn.loitp.restapi.uiza.util;

import android.content.Context;

import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LPref;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.model.v3.UizaWorkspaceInfo;

/**
 * Created by LENOVO on 6/21/2018.
 */

public class UizaV3Util {
    public static void initUizaWorkspace(Context context, UizaWorkspaceInfo uizaWorkspaceInfo) {
        if (uizaWorkspaceInfo == null || uizaWorkspaceInfo.getUsername() == null || uizaWorkspaceInfo.getPassword() == null || uizaWorkspaceInfo.getUrlApi() == null) {
            throw new NullPointerException("UizaWorkspaceInfo cannot be null or empty!");
        }
        LPref.setUizaWorkspaceInfo(context, uizaWorkspaceInfo);
        RestClientV3.init(Constants.PREFIXS + uizaWorkspaceInfo.getUrlApi());
    }

    public static UizaWorkspaceInfo getUizaWorkspace(Context context) {
        if (context == null) {
            return null;
        }
        return LPref.getUizaWorkspaceInfo(context);
    }
}
