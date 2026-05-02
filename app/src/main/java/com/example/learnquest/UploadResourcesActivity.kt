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

class UploadResourcesActivity : AppCompatActivity() {

    private var fileUri: Uri? = null

    private lateinit var title: EditText
    private lateinit var uploadBtn: Button
    private lateinit var selectBtn: Button
    private lateinit var fileText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    // 📂 FILE PICKER
    private val filePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                fileUri = it
                fileText.text = "Selected: ${it.lastPathSegment}"
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_resources)

        title = findViewById(R.id.resourceTitle)
        uploadBtn = findViewById(R.id.uploadResource)
        selectBtn = findViewById(R.id.selectFileBtn)
        fileText = findViewById(R.id.selectedFileText)
        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)

        // 📂 Select file
        selectBtn.setOnClickListener {
            filePicker.launch("*/*")
        }

        // 🚀 Upload manually (better control)
        uploadBtn.setOnClickListener {

            if (fileUri == null) {
                Toast.makeText(this, "Select file first 😑", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (title.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter title 😑", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            uploadResource()
        }
    }

    private fun uploadResource() {

        val storageRef = FirebaseStorage.getInstance().reference
        val fileRef = storageRef.child("resources/${UUID.randomUUID()}")

        progressBar.visibility = View.VISIBLE
        progressText.visibility = View.VISIBLE

        fileUri?.let {

            fileRef.putFile(it)

                .addOnProgressListener { task ->
                    val progress =
                        (100.0 * task.bytesTransferred / task.totalByteCount).toInt()

                    progressBar.progress = progress
                    progressText.text = "Uploading... $progress%"
                }

                .addOnSuccessListener {

                    fileRef.downloadUrl.addOnSuccessListener { uri ->

                        val data = hashMapOf(
                            "title" to title.text.toString(),
                            "fileUrl" to uri.toString()
                        )

                        FirebaseFirestore.getInstance()
                            .collection("resources")
                            .add(data)

                        progressBar.visibility = View.GONE
                        progressText.visibility = View.GONE

                        Toast.makeText(this, "Resource Uploaded 🚀", Toast.LENGTH_SHORT).show()

                        // Reset UI
                        fileUri = null
                        fileText.text = "No file selected"
                        title.setText("")
                    }
                }

                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    progressText.visibility = View.GONE

                    Toast.makeText(this, "Upload Failed ❌", Toast.LENGTH_SHORT).show()
                }
        }
    }
}