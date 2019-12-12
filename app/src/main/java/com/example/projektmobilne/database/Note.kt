package com.example.projektmobilne.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0L,
    @ColumnInfo(name = "time_created")
    val timeCreated: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "note")
    var noteText: String = ""
)