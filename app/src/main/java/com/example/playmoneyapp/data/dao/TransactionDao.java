package com.example.playmoneyapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.playmoneyapp.data.entity.TransactionEntity;
import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insertTransaction(TransactionEntity transaction);

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'RECEITA'")
    LiveData<Double> getTotalIncome();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'DESPESA'")
    LiveData<Double> getTotalExpense();

    @Query("SELECT * FROM transactions")
    List<TransactionEntity> getAllTransactions();
}