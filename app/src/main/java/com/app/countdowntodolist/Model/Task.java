package com.app.countdowntodolist.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Task")
public class Task extends ParseObject {
    public Task() {

    }

    public boolean isCompleted() {
        return getBoolean("completed");
    }

    public void setCompleted(boolean complete) {
        put("completed", complete);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getObjectID() {
        return getString("objectId");
    }

    public void setUser(ParseUser currentUser) {
        put("user", currentUser);
    }

    public void setDeadline(Date date) {
        put("deadlineAt", date);
    }

    public Date getdateDeadline() {
        return getDate("deadlineAt");

    }

}
