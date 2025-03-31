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
    val onDelete: (Int)-> Unit)
    : Adapter<SuperheroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuperheroViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SuperheroViewHolder, position: Int) {
        val task = items[position]
        holder.render(task)
        holder.itemView.setOnClickListener {
            onClick(position)
        }
        holder.binding.deleteButton.setOnClickListener {
            onDelete(position)
        }
    }

    fun updateItems(items: List<Task>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class SuperheroViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root) {

    fun render(task: Task) {
        binding.titleTextView.text = task.title
        binding.titleTextView.text = task.description
        binding.doneCheckBox.isChecked = task.done
    }
}
