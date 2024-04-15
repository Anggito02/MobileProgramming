package com.assignment4_todoapp

import android.content.Context

private val FILENAME = "tasks.txt"
class DataHandler(private val context: Context) {
    fun getTodos() : ArrayList<TodoData> {
        var rawTasks : List<String>? = getRawTasks()
        val todos : ArrayList<TodoData> = arrayListOf()

        if (rawTasks == null) {
            return todos
        }
        rawTasks = rawTasks.dropLast(1)

        for (task in rawTasks) {
            val taskFormatted : List<String> = task.split("%")
            val desc : String = taskFormatted[0]
            val isFinished : Boolean = taskFormatted[1].toBoolean()

            val data = TodoData(desc, isFinished)
            todos.add(data)
        }

        return todos
    }

    fun saveNewTask(newTask : TodoData): Boolean {
        val storageHandler = StorageHandler()
        val todos = getTodos()
        todos.add(newTask)

        val updatedData = dataListToString(todos)

        return storageHandler.saveData(context, FILENAME, updatedData)
    }

    fun updateTask(position: Int): Boolean {
        val storageHandler = StorageHandler()
        val todos = getTodos()
        todos[position].isFinished = true

        val updatedData = dataListToString(todos)

        return storageHandler.saveData(context, FILENAME, updatedData)
    }

    fun deleteTask(position: Int): Boolean {
        val storageHandler = StorageHandler()
        val todos = getTodos()
        todos.removeAt(position)

        val updatedData = dataListToString(todos)

        return storageHandler.saveData(context, FILENAME, updatedData)
    }

    private fun getRawTasks(): List<String>? {
        val rawTasks: String? = getRawData()
        if (rawTasks != null) {
            return rawTasks.split("\n")
        }
        return null
    }

    private fun getRawData(): String? {
        val storageHandler = StorageHandler()
        return storageHandler.getData(context, FILENAME)
    }

    private fun dataListToString(dataList : ArrayList<TodoData>): String {
        var stringData = ""
        for (task in dataList) {
            stringData = "$stringData${task.desc}%${task.isFinished}\n"
        }

        return stringData
    }
}