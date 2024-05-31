import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.finalprojectaidanmaragroup24.DashboardFragment
import com.example.finalprojectaidanmaragroup24.Event
import com.example.finalprojectaidanmaragroup24.MainActivity
import com.example.finalprojectaidanmaragroup24.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class EditEventFragment : DialogFragment() {
        companion object {
            private const val ARG_EVENT_ID = "arg_event_id"
            private const val ARG_EVENT_NAME = "arg_event_name"
            private const val ARG_EVENT_DESCRIPTION = "arg_event_description"
            private const val ARG_EVENT_ENDTIME = "arg_event_endtime"
            private const val ARG_EVENT_PROJECT = "arg_event_project"

            fun newInstance(id:String, eventName:String, eventDescription:String, eventEndTime:String, eventProject:String): EditEventFragment {
                val fragment = EditEventFragment()
                val args = Bundle()
                args.putString(ARG_EVENT_ID, id)
                args.putString(ARG_EVENT_NAME, eventName)
                args.putString(ARG_EVENT_DESCRIPTION, eventDescription)
                args.putString(ARG_EVENT_ENDTIME, eventEndTime)
                args.putString(ARG_EVENT_PROJECT, eventProject)

                fragment.arguments = args
                return fragment
            }
        }
        lateinit var dateToWrite: Calendar
        lateinit var dash: DashboardFragment

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                val view = inflater.inflate(R.layout.fragment_edit_event, container, false)

                val eventId:Int = arguments?.getString(ARG_EVENT_ID)!!.toInt()
                val eventName = arguments?.getString(ARG_EVENT_NAME)
                val eventDescription = arguments?.getString(ARG_EVENT_DESCRIPTION)
                val eventEndTime = arguments?.getString(ARG_EVENT_ENDTIME)
                val eventProject = arguments?.getString(ARG_EVENT_PROJECT)


                val nameEditText:EditText = view.findViewById(R.id.name_editText)
                nameEditText.setText(eventName)

                val descEditText:EditText = view.findViewById(R.id.description_editText)
                descEditText.setText(eventDescription)


            //Spinner Settings hardcoded bc its gonna be too much to do other
                val spinner: Spinner = view.findViewById(R.id.project_spinner)
                val mainActivity = requireActivity() as MainActivity

                val deleteBtn: Button = view.findViewById(R.id.delete_button)

                val updateButton:Button = view.findViewById(R.id.update_event_button)

                updateButton.setOnClickListener {
                    updateEvent(eventId, view, mainActivity)
                }

                deleteBtn.setOnClickListener {
                    deleteEvent(view, mainActivity, eventId)
                }

                val projectList = (activity as? MainActivity)?.projectRepository?.getAllNames()
                val selectedItemPosition = projectList?.indexOf(eventProject)

                if (projectList != null) {
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        projectList
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }

            if (selectedItemPosition != null) {
                spinner.setSelection(selectedItemPosition)
            }


                //Date Dialog Picker
                val pickDateBtn: Button = view.findViewById(R.id.date_button)


                val timeLong: Long = eventEndTime!!.toLong()

                val selectedDate = Calendar.getInstance()
                selectedDate.timeInMillis = timeLong

                val dateFormatNew = SimpleDateFormat("MM/dd/YYYY")
                val formattedDate: String = dateFormatNew.format(selectedDate.time)

                pickDateBtn.text = formattedDate
                dateToWrite = selectedDate

                pickDateBtn.setOnClickListener {
                    val c = Calendar.getInstance()

                    val year = c.get(Calendar.YEAR)
                    val month = c.get(Calendar.MONTH)
                    val day = c.get(Calendar.DAY_OF_MONTH)


                    val datePickerDialog = DatePickerDialog(
                        requireContext(),
                        DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                            val selectedDate = Calendar.getInstance()

                            selectedDate.set(Calendar.YEAR, selectedYear)
                            selectedDate.set(Calendar.MONTH, selectedMonth)
                            selectedDate.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)

                            pickDateBtn.text = "$selectedMonth/$selectedDayOfMonth/$selectedYear"

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


    fun updateEvent(int:Int, view:View, mainActivity: MainActivity){

        val nameText: EditText? = view.findViewById(R.id.name_editText)
        val spinner: Spinner? = view.findViewById(R.id.project_spinner)
        val descText: EditText? = view.findViewById(R.id.description_editText)


        val data1: String = nameText?.text.toString()
        val data2: String = spinner?.selectedItem.toString()
        var data4: String = descText?.text.toString()


        if (data1.isEmpty() || data2.isEmpty()){
            Toast.makeText(mainActivity, "Please fill out all required fields.", Toast.LENGTH_LONG).show()
            return
        }

        val event:Event = mainActivity.eventRepository.getTaskByID(int).first()
            event.name = data1
            event.description = data4
            event.project = data2
            event.endDate = dateToWrite.timeInMillis.toString()

        mainActivity.eventRepository.updateEvent(event)

        mainActivity.returnToDash(this)
        dismiss()
    }

    fun deleteEvent(view: View, mainActivity: MainActivity, int: Int){

        val event:Event = mainActivity.eventRepository.getTaskByID(int).first()

        mainActivity.eventRepository.deleteEvent(event)

        mainActivity.returnToDash(this)
        dismiss()
    }

    }
