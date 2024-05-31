package com.example.finalprojectaidanmaragroup24

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finalprojectaidanmaragroup24.Project
import java.util.Calendar
import java.util.Date

@Entity(tableName = "Events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @NonNull
    @ColumnInfo(name = "Name")
    var name: String,
    @NonNull
    @ColumnInfo(name = "Project")
    var project: String,
    @NonNull
    @ColumnInfo(name = "Start Time")
    var startDate: String,
    @NonNull
    @ColumnInfo(name = "End Date")
    var endDate: String,
    @ColumnInfo(name = "Description")
    var description: String
)