package com.example.test.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.models.Day
import com.example.test.presentation.ExerciseDetailsActivity

class ExerciseAdapter(private val days: List<Day>, private val context: Context) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayTextView: TextView = itemView.findViewById(R.id.dayTextView)
        private val exercisesTextView1: TextView = itemView.findViewById(R.id.exercisesTextView1)

        @SuppressLint("SetTextI18n")
        fun bind(day: Day) {
            dayTextView.text = day.name
            val exercisesText = StringBuilder()
            for (exercise in day.exercises) {
                if (exercise.name != "") {
                    exercisesText.append("${exercise.name} - ${exercise.duration} minutes\n")
                }

            }
            exercisesTextView1.text = exercisesText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val day = days[position]
        holder.bind(day)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ExerciseDetailsActivity::class.java).apply {

                putExtra("day", day.name)
                putExtra("Exercise1Name",  day.exercises[0].name)
                putExtra("Exercise2Name",  day.exercises[1].name)
                putExtra("Exercise3Name",  day.exercises[2].name)
                putExtra("Exercise1Duration",  day.exercises[0].duration.toLong())
                putExtra("Exercise2Duration",  day.exercises[1].duration.toLong())
                putExtra("Exercise3Duration",  day.exercises[2].duration.toLong())
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }
}
