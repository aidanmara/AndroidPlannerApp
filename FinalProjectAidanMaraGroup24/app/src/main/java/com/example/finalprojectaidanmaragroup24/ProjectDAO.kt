package com.example.finalprojectaidanmaragroup24

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.finalprojectaidanmaragroup24.Event
import java.util.Calendar

@Dao
interface ProjectDAO {
    @Insert
    fun insert(project: Project)

    @Update
    fun update(project: Project)

    @Query("SELECT * FROM Projects")
    fun getAllProject(): List<Project>

    @Query("SELECT name FROM Projects")
    fun getAllNames(): List<String>

    @Query("SELECT * FROM Projects WHERE name IS :s")
    fun getSpecificProject(s:String): List<Project>

    @Delete
    fun delete(project: Project)

}
