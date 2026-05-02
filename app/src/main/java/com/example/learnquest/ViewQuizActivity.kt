package com.example.learnquest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ViewQuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_quiz)

        val recycler = findViewById<RecyclerView>(R.id.recyclerQuiz)
        recycler.layoutManager = LinearLayoutManager(this)

        val list = mutableListOf<Map<String, Any>>()
        val adapter = QuizAdapter(this, list)
        recycler.adapter = adapter

        FirebaseFirestore.getInstance()
            .collection("quizzes")
            .get()
            .addOnSuccessListener {

                for (doc in it) {
                    list.add(doc.data)
                }

                adapter.notifyDataSetChanged()
            }
    }
}