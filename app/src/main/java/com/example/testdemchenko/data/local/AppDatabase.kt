package com.example.testdemchenko.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testdemchenko.domain.model.DatabaseMessage
import com.example.testdemchenko.data.local.dao.MessagesDao

@Database(entities = [DatabaseMessage::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
     abstract fun messagesDao(): MessagesDao

     companion object {
         @Volatile
         private var INSTANCE: AppDatabase? = null

         @JvmStatic
         fun getInstance(context: Context): AppDatabase {
             synchronized(this) {
                 var instance = INSTANCE
                 if (instance == null) {
                     instance = Room.databaseBuilder(
                         context.applicationContext,
                         AppDatabase::class.java,
                         "app_database"
                     ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                     INSTANCE = instance
                 }
                 return instance
             }

         }
     }
}
