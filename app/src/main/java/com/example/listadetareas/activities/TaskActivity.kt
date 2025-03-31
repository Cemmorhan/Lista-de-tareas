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
    lateinit var task: Task


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
        val id = intent.getLongExtra("TASK_ID", -1L)
        taskDAO = TaskDAO(this)
        if (id != -1L) {
            task = taskDAO.findById(id)!!
            binding.titleEditText.setText(task.title)
            binding.descriptionEditText.setText(task.description)
        }else{
            task = Task(-1L, "", "")
        }

        binding.safeButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            task.title = title
            task.description = description
            if(id!=-1L){
                taskDAO.update(task)
            }else{
                taskDAO.insert(task)
            }
            finish()
        }
    }
}