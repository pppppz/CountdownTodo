package com.app.countdowntodolist.Model;

import com.facebook.Profile;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {
    public User() {

    }

    public void setName(String name) {
        put("name", name);
    }

    public void setFirstName(String firstName) {
        put("first_name", firstName);
    }

    public void setLastName(String lastName) {
        put("last_name", lastName);
    }


    public String getDescription() {
        return getString("description");
    }


    public void setUserID(String user_id) {
        put("user_id", user_id);
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public void setProfileImg(Profile fbProfile) {
        put("profileThumb", fbProfile);
    }
}
