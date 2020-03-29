package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TODO(val descrition:String,
                val detail:String,
                @PrimaryKey(autoGenerate = true)
                val id:Long = 0L
)