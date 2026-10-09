package com.example.playmoneyapp.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.playmoneyapp.data.entity.UserEntity;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(UserEntity user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    UserEntity findByEmail(String email);

    @Query("SELECT * FROM users")
    List<UserEntity> getAllUsers();
}