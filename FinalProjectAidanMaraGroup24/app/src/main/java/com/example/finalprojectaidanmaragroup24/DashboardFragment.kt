package com.example.finalprojectaidanmaragroup24

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.ceil

class DashboardFragment : Fragment() {

    lateinit var rv: RecyclerView
    lateinit var list: List<Event>
    lateinit var projectList: List<Project>
    lateinit var mainActivity: MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        rv = view.findViewById(R.id.horiRV)

        rv.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        mainActivity = requireActivity() as MainActivity
        list = mainActivity.getNextEvents()
        projectList = mainActivity.projectRepository.getProjects()

        val eventAdapter = EventAdapter(list, projectList)
        rv.adapter = eventAdapter

        return view
    }

}
class EventAdapter(private val events: List<Event>, private val projects: List<Project>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTitle:TextView = itemView.findViewById(R.id.cardTitle)
        val cardDate:TextView = itemView.findViewById(R.id.cardEndDate)
        val cardRemain:TextView = itemView.findViewById(R.id.cardTimeRemaining)
        val cardProj:TextView = itemView.findViewById(R.id.cardProject)
        val card:CardView = itemView.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return events.size // Return the actual size of the events list
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Implement this method to bind data to the views in the ViewHolder
        val event = events[position]

        holder.cardTitle.text = event.name
        holder.cardDate.text = convertLongToDate(event.endDate)
        holder.cardRemain.text = getRemainingTime(event.endDate)
        holder.card.setCardBackgroundColor(getProjectColor(holder.itemView.context, event.project))
        if(event.project != "None") {holder.cardProj.text = event.project} else {holder.cardProj.text = ""}


        val marginInDp = 10 // Replace this with your desired margin value in dp
        val marginInPx = (marginInDp * holder.itemView.context.resources.displayMetrics.density).toInt()

        val layoutParams = holder.card.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.bottomMargin = marginInPx
        holder.card.layoutParams = layoutParams

        holder.card.setOnLongClickListener {
            // Handle the long click event on the card to show the dialog
            val event = events[position]
            showEventDialog(holder.itemView.context, event)
            true // Return 'true' to indicate that the long click event is consumed.
        }

    }


    fun convertLongToDate(string: String):String{
        val timeLong:Long = string.toLong()

        val dateFormatNew = SimpleDateFormat("MM/dd/YYYY")
        val formattedDate: String = dateFormatNew.format(Date(timeLong))

        return formattedDate
    }


    fun getRemainingTime(string: String):String{
        val timeLong:Long = string.toLong()
        val currentTime:Long = System.currentTimeMillis()

        val timeRemaining:Long = timeLong-currentTime

        val years = (timeRemaining / 31556926000).toInt()
        val months = ((timeRemaining % 31556926000) / 2629743833.3).toInt()
        val days = ceil((timeRemaining % 2629743833.3) / 86400000).toInt()


        val yearString = if (years > 1) "$years years" else if (years == 1) "$years year" else ""
        val monthString = if (months > 1) "$months months" else if (months == 1) "$months month" else ""
        val dayString = if (days > 1) "$days days" else if (days == 1) "$days day" else ""

        if (days < 1 && months < 1 && years < 1){
            return "today."
        }

        val finalString = "$yearString $monthString $dayString remaining."

        return finalString
    }

    fun getProjectColor(context: Context, string: String): Int{
        val project:Project? = projects.find{it.name == string}

        if (project==null) {return ContextCompat.getColor(context, R.color.projectColorBlue)}

        val color:String = project.color

        val outColor: Int = when(color){
            "Blue"-> ContextCompat.getColor(context,R.color.projectColorBlue)
            "Brown"-> ContextCompat.getColor(context,R.color.projectColorBrown)
            "Green"-> ContextCompat.getColor(context,R.color.projectColorGreen)
            "Red"-> ContextCompat.getColor(context,R.color.projectColorRed)
            "Purple"-> ContextCompat.getColor(context,R.color.projectColorPurple)
            "Pink"-> ContextCompat.getColor(context,R.color.projectColorPink)
            "Yellow"-> ContextCompat.getColor(context,R.color.projectColorYellow)
            "None"-> ContextCompat.getColor(context,R.color.projectColorNone)
            else -> {
                ContextCompat.getColor(context,R.color.projectColorBlue)
            }
        }

        return outColor
    }
    private fun showEventDialog(context: Context, event: Event) {

        val timeLong: Long = event.endDate.toLong()
        val dateFormatNew = SimpleDateFormat("MM/dd/YYYY")


        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.apply {
            setTitle("${event.name} Details")
            setMessage("Due Date: ${dateFormatNew.format(Date(timeLong))}\nDescription: ${event.description}")

            setPositiveButton("Edit") { dialogInterface, _ ->

                val eventId = event.id.toString()
                val eventName = event.name
                val eventDescription = event.description
                val eventEndTime = event.endDate
                val eventProject = event.project

                val editEventFragment = EditEventFragment.newInstance(eventId, eventName, eventDescription, eventEndTime, eventProject)

                val fragmentManager = (context as FragmentActivity).supportFragmentManager
                editEventFragment.show(fragmentManager, "editEventDialog")


                dialogInterface.dismiss()
            }

            setNegativeButton("Return") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }


}