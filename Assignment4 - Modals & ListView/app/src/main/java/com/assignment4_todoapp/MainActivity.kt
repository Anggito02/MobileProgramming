package com.assignment4_todoapp

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var BtnAdd : Button
    private lateinit var LV_Todo : ListView
    private lateinit var Adapter_Todo : TodoAdapter
    private var ArrList_Todos : ArrayList<TodoData> = arrayListOf()

    // Dialog variables
    private lateinit var D_NewTask : Dialog
    private lateinit var ET_NewTask : EditText
    private lateinit var IBtn_Check : ImageButton
    private lateinit var IBtn_Cancel : ImageButton

    // Data Handler
    private lateinit var DataHandler_Task : DataHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find resources
        LV_Todo = findViewById(R.id.LV_todo)
        BtnAdd = findViewById(R.id.btn_add)

        // Set Listener
        BtnAdd.setOnClickListener(this)

        // Get Task Data
        DataHandler_Task = DataHandler(this)
        ArrList_Todos = DataHandler_Task.getTodos()

        Adapter_Todo = TodoAdapter(this, ArrList_Todos)
        LV_Todo.adapter = Adapter_Todo
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_add -> {
                showAddTaskDialog()
            }

            R.id.btn_dialog_check -> {
                val newTask : String? = fetchNewTask()
                if (newTask == null) {
                    Toast.makeText(this, "Task can't be empty", Toast.LENGTH_SHORT).show()
                    return
                }

                if (!addTask(newTask)) {
                    Toast.makeText(this, "Failed to add task!", Toast.LENGTH_SHORT).show()
                    return
                }

                Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show()
                D_NewTask.dismiss()

                val newTaskData = TodoData(newTask, false)
                ArrList_Todos.add(newTaskData)
                Adapter_Todo.notifyDataSetChanged()
            }

            R.id.btn_dialog_cancel -> {
                D_NewTask.dismiss()
            }
        }
    }

    private fun showAddTaskDialog() {
        D_NewTask = Dialog(this)

        // Set dialog
        D_NewTask.setTitle("Add new Task")
        D_NewTask.setContentView(R.layout.add_item_dialog)
        D_NewTask.setCanceledOnTouchOutside(false)

        ET_NewTask = D_NewTask.findViewById(R.id.et_add_task)
        IBtn_Check = D_NewTask.findViewById(R.id.btn_dialog_check)
        IBtn_Cancel = D_NewTask.findViewById(R.id.btn_dialog_cancel)

        // Set Dialog Button Listener
        IBtn_Check.setOnClickListener(this)
        IBtn_Cancel.setOnClickListener(this)

        D_NewTask.show()
    }

    private fun fetchNewTask() : String? {
        if (ET_NewTask.text.toString() == "") {
            return null
        }
        return ET_NewTask.text.toString()
    }

    private fun addTask(newTask : String): Boolean {
        val newTaskData = TodoData(newTask, false)
        return DataHandler_Task.saveNewTask(newTaskData)
    }
}