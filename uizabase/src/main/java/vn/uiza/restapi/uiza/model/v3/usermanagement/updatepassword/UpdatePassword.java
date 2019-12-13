package vn.uiza.restapi.uiza.model.v3.usermanagement.updatepassword;

import com.squareup.moshi.Json;

public class UpdatePassword {

    @Json(name = "oldPassword")
    private String oldPassword;
    @Json(name = "newPassword")
    private String newPassword;
    @Json(name = "id")
    private String id;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}