package com.example.todolist.ui

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.adapter.TaskListAdapter
import com.example.todolist.dataSource.TaskDataSource
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val adapter by lazy {
        TaskListAdapter()
    }
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.rvTask.adapter = adapter
        updateList()
        insertListeners()
        initLauncher()
        //Data Store
        //Room
    }

    private fun initLauncher() {
        resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                updateList()
            }
        }
    }


    private fun updateList() {
        val list = TaskDataSource.getList()
        viewBinding.includeEmptyState.emptyState.visibility = if (list.isEmpty()) View.VISIBLE
        else View.GONE
        adapter.submitList(list)
    }

    private fun insertListeners() {
        viewBinding.fbAdd.setOnClickListener {
            resultLauncher.launch(Intent(this, AddTaskActivity::class.java))
        }
        adapter.listennerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            resultLauncher.launch(intent)
        }
        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }
}