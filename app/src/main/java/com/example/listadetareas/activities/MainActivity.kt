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
import com.example.listadetareas.adapter.CategoryAdapter
import com.example.listadetareas.databinding.ActivityMainBinding
import com.example.listadetareas.data.Category
import com.example.listadetareas.data.CategoryDAO
import com.example.listadetareas.databinding.EditCategoryBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val TASK_ID = "TASK_ID"
    }
    lateinit var binding: ActivityMainBinding
    lateinit var categoryDAO: CategoryDAO
    lateinit var categoryList: List<Category>
    lateinit var adapter: CategoryAdapter

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
        categoryDAO = CategoryDAO(this)
        supportActionBar?.title = "Mis categorias"
        adapter = CategoryAdapter(emptyList(), ::showCategory, ::editCategory, ::deleteCategory)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.addCategoryButton.setOnClickListener{
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        refreshList()
    }
    fun refreshList(){
        categoryList = categoryDAO.findAll()
        adapter.updateItems(categoryList)
    }
    // editar tarea con una alerta
    // uso el binding para poder acceder a los elementos del dialogo de edicion de tarea del categoryActivity

    fun showCategory(position: Int){
        val category = categoryList[position]

        val intent = Intent(this, TaskListActivity::class.java)
        intent.putExtra(TaskListActivity.CATEGORY_ID, category.id)
        startActivity(intent)
    }


    fun editCategory(position: Int){
        val category = categoryList[position]
        val dialogBinding = EditCategoryBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setTitle("Edit this category")
            .setMessage("Are you sure you want to edit this category?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                category.title = dialogBinding.titleEditText.text.toString()
                categoryDAO.update(category)
                refreshList()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setView(dialogBinding.root)
            //.setCancelable(false) // para evitar que se cancele al pulsar fuera del dialogo
            .show()

        dialogBinding.titleEditText.setText(category.title)
        //val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra(CategoryActivity.CATEGORY_ID, categoryList[position].id)
        startActivity(intent)
    }
    fun deleteCategory(position: Int){
        val category = categoryList[position]
        AlertDialog.Builder(this)
            .setTitle("Delete Category")
            .setMessage("Are you sure you want to delete this category?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                categoryDAO.delete(category)
                refreshList()
            }
            .setNegativeButton(android.R.string.cancel, null)
            //.setCancelable(false) para evitar que se cancele al pulsar fuera del dialogo
            .show()
        refreshList()
    }
}
