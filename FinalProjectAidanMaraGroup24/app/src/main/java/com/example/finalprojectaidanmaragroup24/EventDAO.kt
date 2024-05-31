package com.example.finalprojectaidanmaragroup24

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.finalprojectaidanmaragroup24.Event
import java.util.Calendar

@Dao
interface EventDAO {
    @Insert
    fun insert(event: Event)

    @Update
    fun update(event: Event)

    @Query("SELECT * FROM events")
    fun getAllEvents(): List<Event>

    @Query("SELECT * FROM events WHERE `End Date` > :currDate ORDER BY `End Date` ASC")
    fun getNextEvents(currDate:String): List<Event>

    @Query("SELECT * FROM events WHERE `Project` = :project")
    fun getEventsByProject(project: String): List<Event>

    @Query("SELECT * FROM events WHERE `id` = :id LIMIT 1")
    fun getEventByID(id: Int): List<Event>

    @Delete
    fun delete(event: Event)

}
