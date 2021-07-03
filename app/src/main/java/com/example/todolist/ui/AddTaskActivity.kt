package com.example.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.dataSource.TaskDataSource
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.example.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private val viewBinding by lazy {
        ActivityAddTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                viewBinding.tlTitle.text = it.title
                viewBinding.tlDate.text = it.date
                viewBinding.tlHour.text = it.hour
            }
        }
        insertListeners()
    }

    private fun insertListeners() {
        viewBinding.tlDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                viewBinding.tlDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
        viewBinding.tlHour.editText?.setOnClickListener {
            val timerPicker =
                MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timerPicker.addOnPositiveButtonClickListener {
                viewBinding.tlHour.text = "${String.format("%02d", timerPicker.hour)}:${
                    String.format(
                        "%02d",
                        timerPicker.minute
                    )
                }"
            }
            timerPicker.show(supportFragmentManager, null)
        }
        viewBinding.btCancel.setOnClickListener {
            finish()
        }
        viewBinding.btCreateTask.setOnClickListener {
            val task = Task(
                title = viewBinding.tlTitle.text,
                date = viewBinding.tlDate.text,
                hour = viewBinding.tlHour.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}