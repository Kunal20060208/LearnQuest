package com.example.learnquest

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ViewAssignmentsActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyText: TextView

    private val list = mutableListOf<Map<String, String>>()
    private lateinit var adapter: AssignmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_assignments)

        recycler = findViewById(R.id.recyclerAssignments)
        progressBar = findViewById(R.id.progressAssignments)
        emptyText = findViewById(R.id.emptyText)

        recycler.layoutManager = LinearLayoutManager(this)

        adapter = AssignmentAdapter(list)
        recycler.adapter = adapter

        loadAssignments()
    }

    private fun loadAssignments() {

        progressBar.visibility = View.VISIBLE
        emptyText.visibility = View.GONE

        FirebaseFirestore.getInstance()
            .collection("assignments")
            .get()
            .addOnSuccessListener { docs ->

                list.clear()

                for (doc in docs) {

                    val map = hashMapOf(
                        "title" to (doc.getString("title") ?: "No Title"),
                        "description" to (doc.getString("description") ?: "No Description"),
                        "fileUrl" to (doc.getString("fileUrl") ?: "")
                    )

                    list.add(map)
                }

                progressBar.visibility = View.GONE

                if (list.isEmpty()) {
                    emptyText.visibility = View.VISIBLE
                }

                adapter.notifyDataSetChanged()
            }

            .addOnFailureListener {
                progressBar.visibility = View.GONE
                emptyText.visibility = View.VISIBLE
                Toast.makeText(this, "Failed to load assignments ❌", Toast.LENGTH_SHORT).show()
            }
    }
}