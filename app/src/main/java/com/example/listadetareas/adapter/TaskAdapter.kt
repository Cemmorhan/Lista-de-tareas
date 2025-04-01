package com.example.listadetareas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.listadetareas.data.Task
import com.example.listadetareas.databinding.ItemTaskBinding


class TaskAdapter(
    var items: List<Task>,
    val onClick: (Int) -> Unit,
    val onDelete: (Int)-> Unit,
    val onCheck: (Int) -> Unit)
    : Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = items[position]
        holder.render(task)
        holder.itemView.setOnClickListener {
            onClick(position)
        }
        holder.binding.deleteButton.setOnClickListener {
            onDelete(position)
        }
        holder.binding.doneCheckBox.setOnCheckedChangeListener { _, _ ->
            if(holder.binding.doneCheckBox.isPressed) {
                onCheck(position)

            }
        }
    }

    fun updateItems(items: List<Task>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class TaskViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root) {

    fun render(task: Task) {
        binding.titleTextView.text = task.title
        //esto es si quisiera mostrar la descripcion tambien, pero tendria que ponerle un espacio donde mostrarlo si o si
        //binding.descriptionTextView.text = task.description
        binding.doneCheckBox.isChecked = task.done
    }
}
