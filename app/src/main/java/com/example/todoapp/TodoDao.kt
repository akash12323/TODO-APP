package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(user:TODO)

    @Insert
    fun insertAllUser(user:List<TODO>)

    @Query ("Delete from TODO where id = :user_id")
    fun delete(user_id:Long)

    @Query("Select * from TODO")
    fun getAllUser() : LiveData<List<TODO>>

}
