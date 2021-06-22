package com.example.testdemchenko.data.local.dao


import androidx.room.*
import com.example.testdemchenko.domain.model.DatabaseMessage
import io.reactivex.Single

@Dao
interface MessagesDao {
    @Query("SELECT * FROM messages")
    fun getAll(): Single<List<DatabaseMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messages: List<DatabaseMessage>)

    @Update
    fun update(message: DatabaseMessage)
}