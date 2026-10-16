package com.example.playmoneyapp.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.playmoneyapp.data.dao.TransactionDao;
import com.example.playmoneyapp.data.dao.UserDao;
import com.example.playmoneyapp.data.entity.CategoryEntity;
import com.example.playmoneyapp.data.entity.TransactionEntity;
import com.example.playmoneyapp.data.entity.UserEntity;

@Database(entities = {UserEntity.class, CategoryEntity.class, TransactionEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract TransactionDao transactionDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "playmoney_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }


    public static synchronized AppDatabase getDatabase(Context context) {
        return getInstance(context);
    }
}