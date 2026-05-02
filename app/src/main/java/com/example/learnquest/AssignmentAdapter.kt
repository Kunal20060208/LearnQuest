package com.example.learnquest

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AssignmentAdapter(private val list: List<Map<String, String>>) :
    RecyclerView.Adapter<AssignmentAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.title)
        val desc: TextView = v.findViewById(R.id.desc)
        val viewBtn: Button = v.findViewById(R.id.viewFileBtn)
        val solutionsBtn: Button = v.findViewById(R.id.viewSolutionsBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_assignment, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        val title = item["title"] ?: "No Title"
        val desc = item["description"] ?: "No Description"
        val fileUrl = item["fileUrl"] ?: ""

        holder.title.text = title
        holder.desc.text = desc

        // 📂 OPEN ASSIGNMENT FILE
        holder.viewBtn.setOnClickListener {
            if (fileUrl.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(fileUrl)
                holder.itemView.context.startActivity(intent)
            } else {
                Toast.makeText(holder.itemView.context, "No file found 😑", Toast.LENGTH_SHORT).show()
            }
        }

        // 📥 VIEW STUDENT SOLUTIONS
        holder.solutionsBtn.setOnClickListener {

            val context = holder.itemView.context
            val db = FirebaseFirestore.getInstance()

            db.collection("solutions")
                .whereEqualTo("assignmentTitle", title) // 🔥 match assignment
                .get()
                .addOnSuccessListener { docs ->

                    if (docs.isEmpty) {
                        Toast.makeText(context, "No submissions yet 😴", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    for (doc in docs) {
                        val url = doc.getString("solutionUrl")

                        if (!url.isNullOrEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            context.startActivity(intent)
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to load solutions ❌", Toast.LENGTH_SHORT).show()
                }
        }
    }
}