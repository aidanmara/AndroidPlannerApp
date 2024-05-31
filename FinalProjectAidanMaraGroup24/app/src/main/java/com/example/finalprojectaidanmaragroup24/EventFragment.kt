package com.example.finalprojectaidanmaragroup24

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.Calendar
import java.util.Date

class EventFragment : Fragment() {
    lateinit var dateToWrite:Calendar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)


        //Spinner Settings hardcoded bc its gonna be too much to do other
        val spinner: Spinner = view.findViewById(R.id.project_spinner)
        val mainActivity = requireActivity() as MainActivity

        val clearEventBtn: Button = view.findViewById(R.id.clear_button)

        clearEventBtn.setOnClickListener {
                mainActivity.eventRepository.deleteALL()
        }



        val projectList = (activity as? MainActivity)?.projectRepository?.getAllNames()


        if (projectList != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                projectList
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        //Date Dialog Picker
        val pickDateBtn: Button = view.findViewById(R.id.date_button)

        pickDateBtn.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val createBtn: Button = view.findViewById(R.id.create_event_button)
            createBtn.isEnabled = false


            //Button Listener
            createBtn
                .setOnClickListener {
                    addEvent(view, mainActivity)

                }

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val selectedDate = Calendar.getInstance()

                    selectedDate.set(Calendar.YEAR, selectedYear)
                    selectedDate.set(Calendar.MONTH, selectedMonth)
                    selectedDate.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)

                    pickDateBtn.text = "$selectedMonth/$selectedDayOfMonth/$selectedYear"
                    createBtn.isEnabled = true
                    createBtn.visibility = View.VISIBLE

                    dateToWrite = selectedDate
                },
                year,
                month,
                day
            )
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show()
        }



        return view

    }

    fun addEvent(view: View, mainActivity:MainActivity){

        val nameText: EditText? = view.findViewById(R.id.name_editText)
        val spinner: Spinner? = view.findViewById(R.id.project_spinner)
        //ADD DATE WHEN I FIGURE OUT DATE PICKER
        val descText: EditText? = view.findViewById(R.id.description_editText)
        // Get data from the EditText fields
        val data1: String = nameText?.text.toString()
        val data2: String = spinner?.selectedItem.toString()
        var data4: String = descText?.text.toString()


        if (data1.isEmpty() || data2.isEmpty()){
            Toast.makeText(mainActivity, "Please fill out all required fields.", Toast.LENGTH_LONG).show()
            return
        }

        val event = Event(
            name = data1,
            description = data4,
            project = data2,
            startDate = System.currentTimeMillis().toString(),
            endDate = dateToWrite.timeInMillis.toString(),
        )

        mainActivity.addEventToDatabase(event)

        mainActivity.returnToDash(this)

    }



}
