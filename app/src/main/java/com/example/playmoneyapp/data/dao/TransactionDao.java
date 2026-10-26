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

    // Métodos globais (compatíveis com o Dashboard atual)
    @Query("SELECT * FROM transactions ORDER BY id DESC")
    LiveData<List<TransactionEntity>> getAllTransactions();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'RECEITA'")
    LiveData<Double> getTotalIncome();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'DESPESA'")
    LiveData<Double> getTotalExpense();

    // Métodos específicos por utilizador (para relatórios ou evolução futura)
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY id DESC")
    LiveData<List<TransactionEntity>> getAllTransactionsByUser(long userId);

    @Query("SELECT SUM(amount) FROM transactions WHERE userId = :userId AND type = 'RECEITA'")
    LiveData<Double> getTotalIncomeByUser(long userId);

    @Query("SELECT SUM(amount) FROM transactions WHERE userId = :userId AND type = 'DESPESA'")
    LiveData<Double> getTotalExpenseByUser(long userId);

    @Query("SELECT * FROM transactions WHERE id = :transactionId AND userId = :userId LIMIT 1")
    TransactionEntity getTransactionByIdAndUser(long transactionId, long userId);

    @Query("DELETE FROM transactions WHERE id = :transactionId AND userId = :userId")
    int deleteTransactionByIdAndUser(long transactionId, long userId);
}
