package com.example.playmoneyapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class TransactionEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private long userId;
    private long categoryId;
    private double amount;
    private String description;
    private String type;       // "RECEITA" ou "DESPESA"[cite: 13, 16]
    private String subType;    // "FIXA" ou "VARIAVEL"[cite: 14, 16]
    private String dueDate;
    private boolean isPaid;    // Controlo de contas pendentes / baixa (RF15)[cite: 21, 28]

    public TransactionEntity(long userId, long categoryId, double amount, String description, String type, String subType, String dueDate, boolean isPaid) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.subType = subType;
        this.dueDate = dueDate;
        this.isPaid = isPaid;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getUserId() { return userId; }
    public long getCategoryId() { return categoryId; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getSubType() { return subType; }
    public String getDueDate() { return dueDate; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
}