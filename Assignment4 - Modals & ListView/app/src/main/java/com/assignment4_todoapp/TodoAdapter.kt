package com.assignment4_todoapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class TodoAdapter(private val context: Context, private val dataSource: ArrayList<TodoData>) : BaseAdapter() {
    private val layoutInflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): TodoData {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowItem = layoutInflater.inflate(R.layout.item_row, parent, false)
        val ET_task : EditText = rowItem.findViewById(R.id.et_row_task)
        val IBtn_Row_Check : ImageButton = rowItem.findViewById(R.id.ibtn_row_check)

        // Set button listener
        IBtn_Row_Check.setOnClickListener{
            if (ET_task.isEnabled) {
                ET_task.isEnabled = false
                ET_task.paintFlags = ET_task.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                updateTasks(position)
                notifyDataSetChanged()
            } else {
                deleteTask(position)
                notifyDataSetChanged()
            }
        }

        val taskData : TodoData = getItem(position)
        ET_task.setText(taskData.desc)

        if (!taskData.isFinished) {
            ET_task.isEnabled = true
        } else {
            ET_task.isEnabled = false
            ET_task.paintFlags = ET_task.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            IBtn_Row_Check.setImageResource(R.drawable.trash)
        }

        return rowItem
    }

    private fun updateTasks(position: Int) {
        val dataHandler = DataHandler(context)
        if (!dataHandler.updateTask(position)) {
            Toast.makeText(context, "Failed to update task", Toast.LENGTH_SHORT).show()
            return
        }

        dataSource[position].isFinished = true
        Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT).show()
    }

    private fun deleteTask(position: Int) {
        val dataHandler = DataHandler(context)
        if (!dataHandler.deleteTask(position)) {
            Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show()
            return
        }

        dataSource.removeAt(position)
        Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
        notifyDataSetChanged()
    }
}