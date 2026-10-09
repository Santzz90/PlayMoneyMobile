package com.example.playmoneyapp.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class CategoryEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private long userId;
    private String name;
    private String type; // "RECEITA" ou "DESPESA" (RN05)[cite: 27]

    public CategoryEntity(long userId, String name, String type) {
        this.userId = userId;
        this.name = name;
        this.type = type;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getUserId() { return userId; }
    public String getName() { return name; }
    public String getType() { return type; }
}