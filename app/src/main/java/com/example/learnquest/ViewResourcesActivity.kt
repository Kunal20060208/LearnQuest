package com.example.learnquest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ViewResourcesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_resources)

        val recycler = findViewById<RecyclerView>(R.id.recyclerResources)
        recycler.layoutManager = LinearLayoutManager(this)

        val list = mutableListOf<Map<String, String>>()
        val adapter = ResourceAdapter(list)
        recycler.adapter = adapter

        FirebaseFirestore.getInstance()
            .collection("resources")
            .get()
            .addOnSuccessListener {

                for (doc in it) {
                    list.add(doc.data as Map<String, String>)
                }

                adapter.notifyDataSetChanged()
            }
    }
}