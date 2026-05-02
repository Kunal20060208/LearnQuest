package com.example.learnquest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // PROFILE
        findViewById<Button>(R.id.btnProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // UPLOAD
        findViewById<Button>(R.id.btnAssignments).setOnClickListener {
            startActivity(Intent(this, UploadAssignmentActivity::class.java))
        }

        findViewById<Button>(R.id.btnResources).setOnClickListener {
            startActivity(Intent(this, UploadResourcesActivity::class.java))
        }

        // CREATE QUIZ
        findViewById<Button>(R.id.btnQuiz).setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }

        // 🔥 VIEW FEATURES (THIS WAS MISSING)

        findViewById<Button>(R.id.btnViewAssignments).setOnClickListener {
            startActivity(Intent(this, ViewAssignmentsActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewResources).setOnClickListener {
            startActivity(Intent(this, ViewResourcesActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewQuiz).setOnClickListener {
            startActivity(Intent(this, ViewQuizActivity::class.java))
        }

        findViewById<Button>(R.id.btnResults).setOnClickListener {
            startActivity(Intent(this, ViewResultsActivity::class.java))
        }
    }
}