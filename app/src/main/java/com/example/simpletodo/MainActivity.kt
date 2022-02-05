package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FileUtils.writeLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import android.os.FileUtils as FileUtils1

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // remove the item from the list
                listOfTasks.removeAt(position)
                // notify the adapter that out data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }


        loadItems()

        // look up recycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // set up button and input field so user can enter a task
        findViewById<Button>(R.id.button).setOnClickListener{
            // grab inputted text and put into TaskField
            val userInputtedTask = findViewById<EditText>(R.id.addTaskField).text.toString()
            // add the string to out list of tasks
            listOfTasks.add(userInputtedTask)
            // notify adapter about data being updated
            adapter.notifyItemInserted(listOfTasks.size - 1)
            // reset text file
            inputTextField.setText("")

            saveItems()
        }
    }
    // save the data from user inputs
    // by writing and reading from a file

    // create a method to get the data file we need
    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }

    // load the items by reading every line from the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    // save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}

