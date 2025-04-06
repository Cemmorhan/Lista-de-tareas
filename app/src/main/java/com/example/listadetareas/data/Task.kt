package com.example.listadetareas.data

class Task (
    var id: Long,
    var title: String,
    var description: String,
    var done: Boolean = false,
    var category: Category

) {


    companion object {
        const val TABLE_NAME = "Tasks"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_DONE = "done"
        const val COLUMN_NAME_CATEGORY = "category"
    }
}