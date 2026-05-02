package com.example.learnquest

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.net.URL

class ViewFileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_file)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val progress = findViewById<ProgressBar>(R.id.progress)

        val url = intent.getStringExtra("fileUrl") ?: ""

        // 🔥 SHOW LOADER FIRST
        progress.visibility = View.VISIBLE

        // 👉 PDF HANDLING
        if (url.endsWith(".pdf")) {

            Toast.makeText(this, "Opening PDF...", Toast.LENGTH_SHORT).show()

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(url), "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

            progress.visibility = View.GONE

            startActivity(Intent.createChooser(intent, "Open PDF with"))
            finish()
            return
        }

        // 👉 IMAGE HANDLING
        Thread {
            try {
                val stream = URL(url).openStream()
                val bitmap = BitmapFactory.decodeStream(stream)

                runOnUiThread {
                    progress.visibility = View.GONE
                    imageView.visibility = View.VISIBLE
                    imageView.setImageBitmap(bitmap)
                }

            } catch (e: Exception) {
                runOnUiThread {
                    progress.visibility = View.GONE
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}