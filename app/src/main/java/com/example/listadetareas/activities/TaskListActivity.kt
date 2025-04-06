package com.example.listadetareas.activities


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadetareas.R
import com.example.listadetareas.adapter.TaskAdapter
import com.example.listadetareas.data.Category
import com.example.listadetareas.data.CategoryDAO
import com.example.listadetareas.data.Task
import com.example.listadetareas.data.TaskDAO
import com.example.listadetareas.databinding.ActivityMainBinding
import com.example.listadetareas.databinding.ActivityTaskListBinding
import com.example.listadetareas.databinding.EditTaskBinding

class TaskListActivity : AppCompatActivity() {

    companion object {
        const val CATEGORY_ID = "CATEGORY_ID"
    }


    lateinit var binding: ActivityTaskListBinding
    lateinit var taskDAO: TaskDAO
    lateinit var categoryDAO: CategoryDAO
    lateinit var category: Category
    
    lateinit var taskList: List<Task>
    lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id= intent.getLongExtra(CATEGORY_ID, -1L)
        taskDAO = TaskDAO(this)
        categoryDAO = CategoryDAO(this)
        category = categoryDAO.findById(id)!!
        supportActionBar?.title = category.title
        adapter = TaskAdapter(emptyList(), ::editTask, ::deleteTask, ::checkTask)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.addTaskButton.setOnClickListener{
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra(TaskActivity.CATEGORY_ID, category.id)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        refreshList()
    }
    fun refreshList(){
        taskList = taskDAO.findAllByCategory(category)
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
                intent.putExtra(TaskActivity.TASK_ID, task.id)
                intent.putExtra(TaskActivity.CATEGORY_ID, category.id)
                startActivity(intent)
            }
            .setView(dialogBinding.root)
            //.setCancelable(false) // para evitar que se cancele al pulsar fuera del dialogo
            .show()

        dialogBinding.titleEditText.setText(task.title)
        dialogBinding.descriptionEditText.setText(task.description)
        //val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(TaskActivity.TASK_ID, taskList[position].id)
        intent.putExtra(TaskActivity.CATEGORY_ID, category.id)
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
    }
    fun checkTask(position: Int) {
        val task = taskList[position]
        task.done = !task.done
        taskDAO.update(task)
        refreshList()
    }
}