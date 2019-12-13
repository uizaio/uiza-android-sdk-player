package vn.uiza.restapi.uiza.model.v3.usermanagement.createanuser;

import com.squareup.moshi.Json;

public class CreateUser {
    @Json(name = "id")
    private String id;
    @Json(name = "status")
    private long status;
    @Json(name = "username")
    private String username;
    @Json(name = "email")
    private String email;
    @Json(name = "password")
    private String password;
    @Json(name = "avatar")
    private String avatar;
    @Json(name = "fullname")
    private String fullname;
    @Json(name = "dob")
    private String dob;
    @Json(name = "gender")
    private long gender;
    @Json(name = "isAdmin")
    private long isAdmin;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public long getGender() {
        return gender;
    }

    public void setGender(long gender) {
        this.gender = gender;
    }

    public long getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(long isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}