package edu.ranken.jbuesking.gettogether.data.entity;

import com.google.firebase.database.Exclude;

import java.util.List;

public class User {
    private String username;
    private String password;
    private String email;
    private String location;
    private List<String> interests;
    private List<String> groups;

    public User() {
        // Empty constructor for Firebase Database.
    }

    public User(String username, String password, String email, String location, List<String> interests, List<String> groups) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.location = location;
        this.interests = interests;
        this.groups = groups;
    }

    public User(String username, String password, String email, String location, List<String> interests) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.location = location;
        this.interests = interests;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getGroups() { return groups; }
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Exclude
    public int getGroupSize() {
        return groups == null ? 0: getGroups().size();
    }
}
