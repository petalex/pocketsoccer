package com.example.pocketsoccer.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Pair {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String player1;

    public String player2;
}
