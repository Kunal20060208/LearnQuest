package com.example.learnquest

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val email = findViewById<EditText>(R.id.inputEmail)
        val pass = findViewById<EditText>(R.id.inputPassword)
        val btn = findViewById<Button>(R.id.btnLogin)

        btn.setOnClickListener {

            val e = email.text.toString().trim()
            val p = pass.text.toString().trim()

            if (e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Enter email & password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(e, p)
                .addOnSuccessListener {

                    val user = auth.currentUser

                    user?.let {

                        val userRef = db.collection("users").document(it.uid)

                        userRef.get().addOnSuccessListener { doc ->

                            // 👉 Only create if NOT exists (important)
                            if (!doc.exists()) {

                                val userData = hashMapOf(
                                    "name" to "Teacher",
                                    "role" to "teacher",
                                    "email" to it.email
                                )

                                userRef.set(userData)
                            }
                        }
                    }

                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }

                .addOnFailureListener {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
}