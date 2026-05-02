package com.example.learnquest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var emailText: TextView
    private lateinit var nameText: TextView
    private lateinit var roleText: TextView
    private lateinit var uidText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var logoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        emailText = findViewById(R.id.emailText)
        nameText = findViewById(R.id.nameText)
        roleText = findViewById(R.id.roleText)
        uidText = findViewById(R.id.uidText)
        progressBar = findViewById(R.id.profileProgress)
        logoutBtn = findViewById(R.id.logoutBtn)

        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        if (user == null) {
            Toast.makeText(this, "User not logged in ❌", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val userRef = db.collection("users").document(user.uid)

        progressBar.visibility = View.VISIBLE

        userRef.get()
            .addOnSuccessListener { doc ->

                if (!doc.exists()) {
                    // 🔥 CREATE PROFILE IF MISSING
                    val newUser = hashMapOf(
                        "name" to "New User",
                        "role" to "Student"
                    )

                    userRef.set(newUser).addOnSuccessListener {
                        loadProfile(userRef, user)
                    }
                } else {
                    loadProfile(userRef, user)
                }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Error loading profile ❌", Toast.LENGTH_SHORT).show()
            }

        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadProfile(
        userRef: com.google.firebase.firestore.DocumentReference,
        user: com.google.firebase.auth.FirebaseUser
    ) {
        userRef.get().addOnSuccessListener { doc ->

            progressBar.visibility = View.GONE

            val name = doc.getString("name") ?: "Not Set"
            val role = doc.getString("role") ?: "Not Set"

            nameText.text = "Name: $name"
            emailText.text = "Email: ${user.email}"
            roleText.text = "Role: $role"
            uidText.text = "UID: ${user.uid}"
        }
    }
}