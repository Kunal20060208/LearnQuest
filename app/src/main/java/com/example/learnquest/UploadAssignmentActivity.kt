package com.example.learnquest

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UploadAssignmentActivity : AppCompatActivity() {

    private var fileUri: Uri? = null

    // 🔥 Modern file picker
    private val filePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                fileUri = it
                Toast.makeText(this, "File Selected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_assignment)

        val btnUpload = findViewById<Button>(R.id.uploadFile)
        val btnSubmit = findViewById<Button>(R.id.submitAssignment)
        val title = findViewById<EditText>(R.id.title)
        val desc = findViewById<EditText>(R.id.desc)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val progressText = findViewById<TextView>(R.id.progressText)

        btnUpload.setOnClickListener {
            filePicker.launch("*/*")
        }

        btnSubmit.setOnClickListener {

            if (fileUri == null) {
                Toast.makeText(this, "Select file first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (title.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            uploadFile(
                title.text.toString(),
                desc.text.toString(),
                progressBar,
                progressText,
                btnSubmit
            )
        }
    }

    private fun uploadFile(
        title: String,
        desc: String,
        progressBar: ProgressBar,
        progressText: TextView,
        btnSubmit: Button
    ) {

        val storageRef = FirebaseStorage.getInstance().reference
        val fileRef = storageRef.child("assignments/${UUID.randomUUID()}")

        progressBar.visibility = View.VISIBLE
        progressText.visibility = View.VISIBLE
        btnSubmit.isEnabled = false

        fileUri?.let { uri ->

            fileRef.putFile(uri)
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

                    progressBar.progress = progress.toInt()
                    progressText.text = "Uploading: ${progress.toInt()}%"
                }

                .addOnSuccessListener {

                    fileRef.downloadUrl.addOnSuccessListener { downloadUri ->

                        val data = hashMapOf(
                            "title" to title,
                            "description" to desc,
                            "fileUrl" to downloadUri.toString()
                        )

                        FirebaseFirestore.getInstance()
                            .collection("assignments")
                            .add(data)

                        Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()

                        // RESET UI
                        progressBar.visibility = View.GONE
                        progressText.visibility = View.GONE
                        btnSubmit.isEnabled = true
                    }
                }

                .addOnFailureListener {
                    Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show()

                    progressBar.visibility = View.GONE
                    progressText.visibility = View.GONE
                    btnSubmit.isEnabled = true
                }
        }
    }
}