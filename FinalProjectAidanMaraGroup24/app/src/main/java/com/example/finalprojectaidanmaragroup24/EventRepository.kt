package com.example.finalprojectaidanmaragroup24

import android.content.Context
import android.util.Log
import androidx.room.Room


class EventRepository private constructor(context: Context) {

    companion object {
        private var instance: EventRepository? = null

        fun getInstance(context: Context): EventRepository {
            if (instance == null) {
                instance = EventRepository(context)
            }
            return instance!!
        }
    }

    private val database : EventDatabase = Room.databaseBuilder(
        context.applicationContext,
        EventDatabase::class.java,
        "event.db"
    )
        .allowMainThreadQueries()
        .build()

    private val eventDao = database.eventDao()

    init {
        if (eventDao.getAllEvents().isEmpty()) {

        }
    }

    //fun getSubject(subjectId: Long): Event? = Event().ge(subjectId)

    fun getEvents(): List<Event> = eventDao.getAllEvents()
    fun addEvent(event: Event) { eventDao.insert(event) }
    fun deleteEvent(event: Event) = eventDao.delete(event)
    fun updateEvent(event: Event) = eventDao.update(event)

    fun deleteALL() {
        for(event in eventDao.getAllEvents()){
            eventDao.delete(event)
        }
    }

    fun getTasksByDate(currDate:String): List<Event>{
        return eventDao.getNextEvents(currDate)
    }

    fun getTaskByID(int: Int):List<Event>{
        return eventDao.getEventByID(int)
    }

    fun updateTasks(string: String, newName: String){
        for(item in eventDao.getEventsByProject(string)){
            item.project = newName
            eventDao.update(item)
        }
    }




}