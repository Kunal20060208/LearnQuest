package com.example.learnquest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class QuizActivity : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private lateinit var addBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var shuffleAllBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        container = findViewById(R.id.questionContainer)
        addBtn = findViewById(R.id.addQuestionBtn)
        saveBtn = findViewById(R.id.saveQuiz)
        shuffleAllBtn = findViewById(R.id.shuffleAllBtn)

        addQuestion()

        addBtn.setOnClickListener { addQuestion() }
        shuffleAllBtn.setOnClickListener { shuffleQuestions() }
        saveBtn.setOnClickListener { saveQuizToFirebase() }
    }

    private fun addQuestion() {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.item_question, container, false)

        val removeBtn = view.findViewById<Button>(R.id.btnRemove)
        val shuffleBtn = view.findViewById<Button>(R.id.btnShuffleOptions)

        removeBtn.setOnClickListener {
            container.removeView(view)
        }

        shuffleBtn.setOnClickListener {
            shuffleOptions(view)
        }

        container.addView(view)
    }

    private fun shuffleOptions(view: View) {

        val optionFields = listOf(
            view.findViewById<EditText>(R.id.optionA),
            view.findViewById<EditText>(R.id.optionB),
            view.findViewById<EditText>(R.id.optionC),
            view.findViewById<EditText>(R.id.optionD)
        )

        val texts = optionFields.map { it.text.toString() }.shuffled()

        optionFields.forEachIndexed { index, editText ->
            editText.setText(texts[index])
        }

        // Reset correct answer selection
        val radioGroup = view.findViewById<RadioGroup>(R.id.optionsGroup)
        radioGroup.clearCheck()
    }

    private fun shuffleQuestions() {
        val views = mutableListOf<View>()

        for (i in 0 until container.childCount) {
            views.add(container.getChildAt(i))
        }

        container.removeAllViews()

        views.shuffled().forEach {
            container.addView(it)
        }
    }

    private fun saveQuizToFirebase() {

        val questionsList = mutableListOf<HashMap<String, Any>>()

        val timerInput = findViewById<EditText>(R.id.quizTimer)
        val titleInput = findViewById<EditText>(R.id.quizTitle)

        val timer = timerInput.text.toString().toIntOrNull()
        val title = titleInput.text.toString()

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter quiz title 😑", Toast.LENGTH_SHORT).show()
            return
        }

        if (timer == null) {
            Toast.makeText(this, "Enter valid timer 😑", Toast.LENGTH_SHORT).show()
            return
        }

        for (i in 0 until container.childCount) {

            val view = container.getChildAt(i)

            val question = view.findViewById<EditText>(R.id.question).text.toString()
            val optA = view.findViewById<EditText>(R.id.optionA).text.toString()
            val optB = view.findViewById<EditText>(R.id.optionB).text.toString()
            val optC = view.findViewById<EditText>(R.id.optionC).text.toString()
            val optD = view.findViewById<EditText>(R.id.optionD).text.toString()
            val marks = view.findViewById<EditText>(R.id.marks).text.toString().toIntOrNull()

            val radioGroup = view.findViewById<RadioGroup>(R.id.optionsGroup)

            if (question.isEmpty() || optA.isEmpty() || optB.isEmpty()
                || optC.isEmpty() || optD.isEmpty() || marks == null) {

                Toast.makeText(this, "Fill all fields in Q${i + 1}", Toast.LENGTH_SHORT).show()
                return
            }

            val selectedId = radioGroup.checkedRadioButtonId

            if (selectedId == -1) {
                Toast.makeText(this, "Select correct answer for Q${i + 1}", Toast.LENGTH_SHORT).show()
                return
            }

            val correctAnswer = when (selectedId) {
                R.id.optionA_radio -> optA
                R.id.optionB_radio -> optB
                R.id.optionC_radio -> optC
                R.id.optionD_radio -> optD
                else -> ""
            }

            val qData = hashMapOf<String, Any>(
                "question" to question,
                "optionA" to optA,
                "optionB" to optB,
                "optionC" to optC,
                "optionD" to optD,
                "correctAnswer" to correctAnswer,
                "marks" to marks
            )

            questionsList.add(qData)
        }

        val quizData = hashMapOf<String, Any>(
            "title" to title,
            "timer" to timer,
            "questions" to questionsList
        )

        FirebaseFirestore.getInstance()
            .collection("quizzes")
            .add(quizData)
            .addOnSuccessListener {
                Toast.makeText(this, "Quiz Saved 🚀", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed ❌", Toast.LENGTH_SHORT).show()
            }
    }
}