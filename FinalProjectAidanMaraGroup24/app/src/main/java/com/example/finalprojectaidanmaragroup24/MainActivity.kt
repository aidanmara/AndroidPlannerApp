package com.example.finalprojectaidanmaragroup24

import android.content.Context
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


class MainActivity : AppCompatActivity() {
        lateinit var eventRepository: EventRepository
        lateinit var projectRepository: ProjectRepository


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val navView = findViewById<BottomNavigationView>(R.id.nav_view)
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            val navController = navHostFragment.navController
            val appBarConfig = AppBarConfiguration.Builder(
                R.id.navigation_dash, R.id.navigation_event).build()

            //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig) //no action bar//
            NavigationUI.setupWithNavController(navView, navController)


            eventRepository = EventRepository.getInstance(this)
            projectRepository = ProjectRepository.getInstance(this)


            //clearProjectsList(projectList) //for reseting the projects list
            //eventRepository.deleteALL() //for resetting the roomdb
    }


    fun getNextEvents():List<Event>{
        val selectedDate = Calendar.getInstance()
        selectedDate.set(Calendar.HOUR_OF_DAY, 0);
        selectedDate.set(Calendar.MINUTE, 0);
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);

        return eventRepository.getTasksByDate(selectedDate.timeInMillis.toString())
    }


    fun returnToDash(fragment: Fragment) {
        val navController = findNavController(fragment)
        navController.navigate(
            R.id.navigation_dash, null, NavOptions.Builder()
                .setPopUpTo(R.id.navigation_dash, true)
                .build()
        )
    }

    fun addEventToDatabase(event:Event){
        eventRepository.addEvent(event)
    }

    fun clearProjectsList(mutableList: MutableList<Project>){

    }
}