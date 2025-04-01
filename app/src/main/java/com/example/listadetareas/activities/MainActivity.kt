package com.example.listadetareas.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadetareas.R
import com.example.listadetareas.adapter.TaskAdapter
import com.example.listadetareas.databinding.ActivityMainBinding
import com.example.listadetareas.data.Task
import com.example.listadetareas.data.TaskDAO
import com.example.listadetareas.databinding.EditTaskBinding

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
        adapter = TaskAdapter(emptyList(), ::editTask, ::deleteTask, ::checkTask)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.addTaskButton.setOnClickListener{
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        refreshList()
    }
    fun refreshList(){
        taskList = taskDAO.findAll()
        adapter.updateItems(taskList)
    }
    // editar tarea con una alerta
    // uso el binding para poder acceder a los elementos del dialogo de edicion de tarea del taskActivity
    fun editTask(position: Int){
        val task = taskList[position]
        val dialogBinding = EditTaskBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setTitle("Edit this task")
            .setMessage("Are you sure you want to edit this task?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                task.title = dialogBinding.titleEditText.text.toString()
                task.description = dialogBinding.descriptionEditText.text.toString()
                taskDAO.update(task)
                refreshList()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setNeutralButton("More options to edit") { _, _ ->
                val intent = Intent(this, TaskActivity::class.java)
                intent.putExtra("TASK_ID", task.id)
                startActivity(intent)
            }
            .setView(dialogBinding.root)
            .setCancelable(false) // para evitar que se cancele al pulsar fuera del dialogo
            .show()

        dialogBinding.titleEditText.setText(task.title)
        dialogBinding.descriptionEditText.setText(task.description)
        //val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra("TASK_ID", taskList[position].id)
        startActivity(intent)
    }
    fun deleteTask(position: Int){
        val task = taskList[position]
        AlertDialog.Builder(this)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                taskDAO.delete(task)
                refreshList()
            }
            .setNegativeButton(android.R.string.cancel, null)
            //.setCancelable(false) para evitar que se cancele al pulsar fuera del dialogo
            .show()
        refreshList()
    }
    fun checkTask(position: Int) {
        val task = taskList[position]
        task.done = !task.done
        taskDAO.update(task)
        refreshList()
    }
}
