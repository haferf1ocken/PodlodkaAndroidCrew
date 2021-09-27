package com.example.podlodkaandroidcrew.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SessionDatabase::class], version = 2, exportSchema = false)
abstract class PodlodkaAndroidCrewDatabase : RoomDatabase() {

    abstract fun sessionDao(): SessionDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PodlodkaAndroidCrewDatabase? = null

        fun getDatabase(context: Context): PodlodkaAndroidCrewDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PodlodkaAndroidCrewDatabase::class.java,
                    "word_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}