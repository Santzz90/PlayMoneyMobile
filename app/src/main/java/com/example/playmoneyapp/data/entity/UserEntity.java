package com.example.playmoneyapp.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String userCode;
    private String name;
    private String email;
    private String password;
    private String profile;
    private boolean isActive;

    public UserEntity(@NonNull String userCode, String name, String email, String password, String profile) {
        this.userCode = userCode;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.isActive = true;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    @NonNull
    public String getUserCode() { return userCode; }
    public void setUserCode(@NonNull String userCode) { this.userCode = userCode; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getProfile() { return profile; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}