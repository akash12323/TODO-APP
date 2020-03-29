package com.example.todoapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TODO::class],version = 3)

abstract class AppDatabase:RoomDatabase(){
    abstract fun todoDao():TodoDao
}