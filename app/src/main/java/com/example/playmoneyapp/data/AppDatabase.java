package com.example.playmoneyapp.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.playmoneyapp.data.dao.UserDao;
import com.example.playmoneyapp.data.entity.CategoryEntity;
import com.example.playmoneyapp.data.entity.TransactionEntity;
import com.example.playmoneyapp.data.entity.UserEntity;

@Database(entities = {UserEntity.class, CategoryEntity.class, TransactionEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "playmoney_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}