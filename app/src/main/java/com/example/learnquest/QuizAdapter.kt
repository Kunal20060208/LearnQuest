package com.example.learnquest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class QuizAdapter(
    private val context: Context,
    private val list: List<Map<String, Any>>
) : RecyclerView.Adapter<QuizAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.quizTitle)
        val timer: TextView = v.findViewById(R.id.quizTimer)
        val questionsCount: TextView = v.findViewById(R.id.questionCount)
        val viewBtn: Button = v.findViewById(R.id.viewQuizBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        val title = item["title"] as? String ?: "Untitled Quiz"
        val timer = item["timer"].toString()
        val questions = item["questions"] as? List<*>

        holder.title.text = title
        holder.timer.text = "⏱ Timer: $timer mins"
        holder.questionsCount.text = "❓ Questions: ${questions?.size ?: 0}"

        holder.viewBtn.setOnClickListener {

            Toast.makeText(
                context,
                "Quiz: $title (View logic next 🔥)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}