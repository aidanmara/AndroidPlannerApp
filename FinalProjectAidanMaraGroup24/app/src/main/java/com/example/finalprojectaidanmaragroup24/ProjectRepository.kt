package com.example.finalprojectaidanmaragroup24

import android.content.Context
import android.util.Log
import androidx.room.Room


class ProjectRepository constructor(context: Context) {

    companion object {
        private var instance: ProjectRepository? = null

        fun getInstance(context: Context): ProjectRepository {
            if (instance == null) {
                instance = ProjectRepository(context)
            }
            return instance!!
        }
    }

    private val database : ProjectDatabase = Room.databaseBuilder(
        context.applicationContext,
        ProjectDatabase::class.java,
        "project.db"
    )
        .allowMainThreadQueries()
        .build()

    private val projectDao = database.projectDao()

    init {
        if (projectDao.getAllProject().isEmpty()) {
            addProject(
                Project(
                name="None",
                color = "None"
                )
            )
        }
    }


    fun getProjects(): List<Project> = projectDao.getAllProject()
    fun addProject(project: Project) {projectDao.insert(project)}
    fun deleteProject(project: Project) = projectDao.delete(project)
    fun updateProject(project: Project) = projectDao.update(project)

    fun deleteALL() {
        for(event in projectDao.getAllProject()){
            projectDao.delete(event)
        }
    }

    fun getAllNames(): List<String> = projectDao.getAllNames()

    fun findProject(string: String):List<Project> = projectDao.getSpecificProject(string)



}