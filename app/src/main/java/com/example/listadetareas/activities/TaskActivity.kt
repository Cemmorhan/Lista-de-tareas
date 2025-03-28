package com.example.listadetareas.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.listadetareas.R
import com.example.listadetareas.data.Task
import com.example.listadetareas.data.TaskDAO
import com.example.listadetareas.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    lateinit var binding: ActivityTaskBinding
    lateinit var taskDAO: TaskDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding= ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        taskDAO = TaskDAO(this)

        binding.safeButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val task = Task(-1L, title)
            taskDAO.insert(task)
            finish()
        }
    }
}