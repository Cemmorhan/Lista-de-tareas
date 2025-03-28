package com.example.listadetareas.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadetareas.R
import com.example.listadetareas.adapter.TaskAdapter
import com.example.listadetareas.databinding.ActivityMainBinding
import com.example.listadetareas.data.Task
import com.example.listadetareas.data.TaskDAO

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var taskDAO: TaskDAO
    lateinit var taskList: List<Task>
    lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        taskDAO = TaskDAO(this)
        adapter = TaskAdapter(emptyList()){
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.addTaskButton.setOnClickListener{
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        taskList = taskDAO.findAll()
        adapter.updateItems(taskList)
    }
}