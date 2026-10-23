package com.example.playmoneyapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.playmoneyapp.data.entity.TransactionEntity;
import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insert(TransactionEntity transaction);

    @Update
    void update(TransactionEntity transaction);

    @Delete
    void delete(TransactionEntity transaction);

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    LiveData<List<TransactionEntity>> getAllTransactions();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'RECEITA'")
    LiveData<Double> getTotalIncome();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'DESPESA'")
    LiveData<Double> getTotalExpense();
}