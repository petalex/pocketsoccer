package com.example.pocketsoccer.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Pair.class, parentColumns = "id", childColumns = "pairId", onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)})
public class Game {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int time;

    public int score1;

    public int score2;

    public int pairId;
}