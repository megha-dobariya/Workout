package com.example.test.presentation

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.models.Day
import com.example.test.models.Exercise
import com.example.test.presentation.adapter.ExerciseAdapter
import com.example.test.utils.NotificationReceiver
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestNotificationPermission()
        createNotificationChannel()
        scheduleWorkouts(this)

        recyclerView = findViewById(R.id.exercise_recycler_view)
        adapter = ExerciseAdapter(createDummyData(), this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun scheduleWorkouts(context: Context) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val workoutDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

        for (day in workoutDays) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeek(day))
            calendar.set(Calendar.HOUR_OF_DAY, 13)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            Log.d("WorkoutScheduler", "Scheduling workout for $day at ${calendar.timeInMillis}")

            // Notify 10 minutes before workout
            val notificationIntent = Intent(context, NotificationReceiver::class.java)
            notificationIntent.putExtra("workout_day", day)
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis - 10 * 60 * 1000,
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent
            )
        }
    }

    private fun getDayOfWeek(day: String): Int {
        return when (day) {
            "Monday" -> Calendar.MONDAY
            "Tuesday" -> Calendar.TUESDAY
            "Wednesday" -> Calendar.WEDNESDAY
            "Thursday" -> Calendar.THURSDAY
            "Friday" -> Calendar.FRIDAY
            else -> throw IllegalArgumentException("Invalid day")
        }
    }

    private fun createDummyData(): List<Day> {
        return listOf(
            Day("Monday", listOf(Exercise("Cardio", 30), Exercise("Core Strength", 15), Exercise("stretching and cool down", 15))),
            Day("Tuesday", listOf(Exercise("Upper Body Strength", 20), Exercise("Bodyweight", 20), Exercise("stretching and cool down", 20))),
            Day("Wednesday", listOf(Exercise("Yoga", 60), Exercise("", 0), Exercise("", 0))),
            Day("Thursday", listOf(Exercise("Lower Body Strength", 20), Exercise("Bodyweight", 20), Exercise("stretching and cool down", 20))),
            Day("Friday", listOf(Exercise("Cardio Intervals", 30), Exercise("Core Strengthening", 15), Exercise("stretching and cool down", 15)))
        )
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "workoutChannelIdReminder"
            val description = "Channel For Workout Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("workoutChannelId", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }
    }
}
