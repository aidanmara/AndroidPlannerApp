package com.example.finalprojectaidanmaragroup24

import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    @NonNull
    @ColumnInfo
    var name: String,
    @NonNull
    @ColumnInfo(name = "Color")
    var color: String,
)