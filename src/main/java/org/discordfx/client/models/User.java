package org.discordfx.client.models;

import java.net.URL;

public class User {

    private final String username;
    private final URL avatarURL;

    public User(String username, URL avatarURL) {
        this.username = username;
        this.avatarURL = avatarURL;
    }

    public String getInitials() {
        String[] splitUsername = username.trim().split("\\s+");
        if (splitUsername.length > 1) {
            return "" + splitUsername[0].charAt(0) + splitUsername[1].charAt(0);
        }
        return "" + splitUsername[0].charAt(0);
    }

}
