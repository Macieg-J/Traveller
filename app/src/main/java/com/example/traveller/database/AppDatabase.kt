package com.example.traveller.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Entry::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
}