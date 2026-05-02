package com.example.learnquest

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class ResourceAdapter(private val list: List<Map<String, String>>) :
    RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.title)
        val openBtn: Button = v.findViewById(R.id.openResourceBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resource, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        val title = item["title"] ?: "Untitled Resource"
        val fileUrl = item["fileUrl"] ?: ""

        holder.title.text = title

        // 📂 OPEN RESOURCE FILE
        holder.openBtn.setOnClickListener {

            if (fileUrl.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(fileUrl)
                holder.itemView.context.startActivity(intent)
            } else {
                Toast.makeText(
                    holder.itemView.context,
                    "No file found 😑",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}