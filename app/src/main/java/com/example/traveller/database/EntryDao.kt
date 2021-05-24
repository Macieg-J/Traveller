package com.example.traveller.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface EntryDao {
    @Query("SELECT * FROM entry")
    fun getAll(): List<Entry>

    @Query("SELECT * FROM entry WHERE id IN (:entryIds)")
    fun loadAllByIds(entryIds: IntArray): List<Entry>

    @Query(
        "SELECT * FROM entry WHERE id = (:entryId)"
    )
    fun findByEntryId(entryId: String): Entry

    @Query(
        "SELECT * FROM entry WHERE place_name IN (:placeName)"
    )
    fun findByPlaceName(placeName: String): List<Entry>

    @Query(
        "SELECT * FROM entry WHERE latitude LIKE :latitude AND " +
                "longitude LIKE :longitude LIMIT 1"
    )
    fun findByLocationParams(latitude: Double, longitude: Double): Entry

    @Insert
    fun insertAll(vararg entries: Entry)

    @Delete
    fun delete(entry: Entry)
}