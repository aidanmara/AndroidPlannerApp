package com.example.finalprojectaidanmaragroup24

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast


class ProjectFragment : Fragment() {

    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_project, container, false)

        mainActivity = requireActivity() as MainActivity

        //Spinner Settings hardcoded bc its gonna be too much to do other
        val spinner: Spinner = view.findViewById(R.id.color_spinner)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.ProjectSpinnerColors,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }


        val projectSpinnerFill: Spinner = view.findViewById(R.id.pro_spinner)
        val pList: List<String> = mainActivity.projectRepository.getAllNames()

        if (pList != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                pList
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            projectSpinnerFill.adapter = adapter
        }

        projectSpinnerFill.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view2: View?,
                position: Int,
                id: Long
            ) {
                if (projectSpinnerFill.selectedItem == "None") {
                    view.findViewById<Button>(R.id.create_pc_button)?.isEnabled = true
                    view.findViewById<Button>(R.id.update_pc_button)?.isEnabled = false
                    view.findViewById<Button>(R.id.delete_pc_button)?.isEnabled = false
                } else {
                    view.findViewById<Button>(R.id.create_pc_button)?.isEnabled = false
                    view.findViewById<Button>(R.id.update_pc_button)?.isEnabled = true
                    view.findViewById<Button>(R.id.delete_pc_button)?.isEnabled = true
                }
            }
        }

        //Button Listener
        view.findViewById<Button>(R.id.create_pc_button)
            .setOnClickListener {
                addProject(view)
            }

        view.findViewById<Button>(R.id.update_pc_button)
            .setOnClickListener {
                updateProject(view)
            }

        view.findViewById<Button>(R.id.delete_pc_button)
            .setOnClickListener {
                deleteProject(view)
            }


        return view
    }

    fun addProject(view: View){
        val editText: EditText = view.findViewById(R.id.name_editText)
        val spinner: Spinner = view.findViewById(R.id.color_spinner)
            // Get data from the EditText fields
            val data1: String = editText.text.toString()
            val data2: String = spinner.selectedItem.toString()

        if(data1.isEmpty() || data2.isEmpty()){
            Toast.makeText(mainActivity, "Please fill out all required fields.", Toast.LENGTH_LONG).show()
            return
        }

        if(!(mainActivity.projectRepository.findProject(data1).isEmpty())){
            Toast.makeText(mainActivity, "Please select a unique name.", Toast.LENGTH_LONG).show()
            return
        }



        val project = Project(
            name = data1,
            color = data2,
        )


        mainActivity.projectRepository.addProject(project)

        mainActivity.returnToDash(this)
    }


    fun updateProject(view: View){
        val editText: EditText = view.findViewById(R.id.name_editText)
        val spinner: Spinner = view.findViewById(R.id.color_spinner)
        val projectSpinner: Spinner = view.findViewById(R.id.pro_spinner)

        val pName: String = projectSpinner.selectedItem.toString()

        if(pName == "None"){
            Toast.makeText(mainActivity, "You Cannot Edit None", Toast.LENGTH_LONG).show()
            return
        }

        val projectList = mainActivity.projectRepository.findProject(pName)
        val projectToChange: Project =  projectList.first()


        // Get data from the EditText fields
        val data1: String = editText.text.toString()
        val data2: String = spinner.selectedItem.toString()


        if(data1.isEmpty() || data2.isEmpty()){
            Toast.makeText(mainActivity, "Please fill out all required fields.", Toast.LENGTH_LONG).show()
            return
        }

        if(!(mainActivity.projectRepository.findProject(data1).isEmpty()) && mainActivity.projectRepository.findProject(data1).first() != projectToChange ){
            Toast.makeText(mainActivity, "Please select a unique name.", Toast.LENGTH_LONG).show()
            return
        }

        projectToChange.name = data1
        projectToChange.color = data2


        mainActivity.eventRepository.updateTasks(pName,data1)
        mainActivity.projectRepository.updateProject(projectToChange)

        mainActivity.returnToDash(this)
    }

    fun deleteProject(view: View){
        val projectSpinner: Spinner = view.findViewById(R.id.pro_spinner)

        val pName: String = projectSpinner.selectedItem.toString()

        if(pName == "None"){
            Toast.makeText(mainActivity, "You Cannot Delete None", Toast.LENGTH_LONG).show()
            return
        }

        val projectList = mainActivity.projectRepository.findProject(pName)
        val projectToDelete: Project =  projectList.first()


        // Get data from the EditText fields
        val data1: String = "None"


        mainActivity.eventRepository.updateTasks(pName,data1)
        mainActivity.projectRepository.deleteProject(projectToDelete)

        mainActivity.returnToDash(this)
    }

}
