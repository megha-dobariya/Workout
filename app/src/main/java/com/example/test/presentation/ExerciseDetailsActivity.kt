package com.example.test.presentation

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R

class ExerciseDetailsActivity : AppCompatActivity() {

    private lateinit var day: TextView

    private lateinit var exercise1TextView: TextView
    private lateinit var timer1TextView: TextView
    private lateinit var startPauseButton1: Button

    private lateinit var exercise2TextView: TextView
    private lateinit var timer2TextView: TextView
    private lateinit var startPauseButton2: Button

    private lateinit var exercise3TextView: TextView
    private lateinit var timer3TextView: TextView
    private lateinit var startPauseButton3: Button

    private lateinit var timers: MutableList<CountDownTimer>
    private lateinit var isTimerRunning: MutableList<Boolean>
    private lateinit var remainingTime: MutableList<Long>
    private lateinit var exerciseDurations: List<Long>
    private lateinit var timerTextViews: List<TextView>

    private lateinit var workoutDay: String
    private lateinit var exercise1Name: String
    private lateinit var exercise2Name: String
    private lateinit var exercise3Name: String
    private var exercise1Duration: Long = 0
    private var exercise2Duration: Long = 0
    private var exercise3Duration: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_details)

        initData()

        day = findViewById(R.id.workoutDay)

        exercise1TextView = findViewById(R.id.exercise1TextView)
        timer1TextView = findViewById(R.id.timer1TextView)
        startPauseButton1 = findViewById(R.id.startPauseButton1)

        exercise2TextView = findViewById(R.id.exercise2TextView)
        timer2TextView = findViewById(R.id.timer2TextView)
        startPauseButton2 = findViewById(R.id.startPauseButton2)

        exercise3TextView = findViewById(R.id.exercise3TextView)
        timer3TextView = findViewById(R.id.timer3TextView)
        startPauseButton3 = findViewById(R.id.startPauseButton3)

        day.text = workoutDay
        exercise1TextView.text = exercise1Name
        timer1TextView.text = formatTime(exercise1Duration*60*1000)

        if (exercise2Name == "") {
            exercise2TextView.visibility = View.GONE
            exercise3TextView.visibility = View.GONE
            timer2TextView.visibility = View.GONE
            timer3TextView.visibility = View.GONE
            startPauseButton2.visibility = View.GONE
            startPauseButton3.visibility = View.GONE
        }else {
            exercise2TextView.text = exercise2Name
            exercise3TextView.text = exercise3Name
            timer2TextView.text = formatTime(exercise2Duration*60*1000)
            timer3TextView.text = formatTime(exercise3Duration*60*1000)
        }


        timerTextViews = listOf(timer1TextView, timer2TextView, timer3TextView)

        exerciseDurations = listOf(
            exercise1Duration * 60 * 1000,
            exercise2Duration * 60 * 1000,
            exercise3Duration * 60 * 1000
        )

        remainingTime = exerciseDurations.toMutableList()

        timers = mutableListOf(
            createTimer(exerciseDurations[0], timer1TextView, 0),
            createTimer(exerciseDurations[1], timer2TextView, 1),
            createTimer(exerciseDurations[2], timer3TextView, 2)
        )

        isTimerRunning = MutableList(timers.size) { false }

        setupTimer(startPauseButton1, 0)
        setupTimer(startPauseButton2, 1)
        setupTimer(startPauseButton3, 2)
    }

    private fun setupTimer(button: Button, index: Int) {
        button.setOnClickListener {
            val timer = timers[index]
            if (isTimerRunning[index]) {
                timer.cancel()
                isTimerRunning[index] = false
                button.text = "Start"
            } else {
                startTimer(timer, index)
                button.text = "Reset"
            }
        }
    }

    private fun startTimer(timer: CountDownTimer, index: Int) {
        timer.start()
        isTimerRunning[index] = true
    }

    private fun createTimer(duration: Long, timerTextView: TextView, index: Int): CountDownTimer {
        return object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = formatTime(millisUntilFinished)
                remainingTime[index] = millisUntilFinished

                Log.d("Remaining Time TV", millisUntilFinished.toString())
                Log.d("Remaining Time Index", remainingTime[index].toString())
            }

            override fun onFinish() {
                timerTextView.text = "00:00"
                remainingTime[index] = 0
                isTimerRunning[index] = false
            }
        }
    }

    private fun formatTime(timeInMillis: Long): String {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    private fun initData() {
        workoutDay = intent.getStringExtra("day").toString()
        exercise1Name = intent.getStringExtra("Exercise1Name").toString()
        exercise2Name = intent.getStringExtra("Exercise2Name").toString()
        exercise3Name = intent.getStringExtra("Exercise3Name").toString()
        exercise1Duration = intent.getLongExtra("Exercise1Duration", 0)
        exercise2Duration = intent.getLongExtra("Exercise2Duration", 0)
        exercise3Duration = intent.getLongExtra("Exercise3Duration", 0)
    }
}

