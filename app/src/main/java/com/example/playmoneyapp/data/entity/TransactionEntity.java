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

    // Getters e Setters completos para todas as propriedades
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getCategoryId() { return categoryId; }
    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSubType() { return subType; }
    public void setSubType(String subType) { this.subType = subType; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
}