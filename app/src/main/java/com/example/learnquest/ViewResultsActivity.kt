package com.example.learnquest

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ViewResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_results)

        val listView = findViewById<ListView>(R.id.resultsList)
        val progress = findViewById<ProgressBar>(R.id.resultsProgress)

        val list = mutableListOf<String>()

        progress.visibility = View.VISIBLE

        FirebaseFirestore.getInstance()
            .collection("quiz_results")
            .get()
            .addOnSuccessListener {

                for (doc in it) {

                    val marks = doc.getLong("marksObtained") ?: 0
                    val total = doc.getLong("totalMarks") ?: 0

                    list.add("🎯 Score: $marks / $total")
                }

                progress.visibility = View.GONE

                listView.adapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
            }
            .addOnFailureListener {
                progress.visibility = View.GONE
                Toast.makeText(this, "Failed to load results ❌", Toast.LENGTH_SHORT).show()
            }
    }
}