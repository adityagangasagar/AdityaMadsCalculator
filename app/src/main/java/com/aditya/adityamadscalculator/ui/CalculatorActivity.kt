package com.aditya.adityamadscalculator.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aditya.adityamadscalculator.models.HistoryData
import com.aditya.adityamadscalculator.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CalculatorActivity : AppCompatActivity() {
    var currentuser: FirebaseUser? = null
    var firebaseAuth: FirebaseAuth? = null
    private var editText1: EditText? = null
    private var editText2: EditText? = null
    private var buttonEqual: Button? = null
    private var buttonHistory: Button? = null
    private var count = 0
    private var expression = ""
    private var text = ""
    private var resultQ = 0
    var historyData: HistoryData? = null

    override fun onStart() {
        super.onStart()
        if (currentuser == null) {
            startActivity(Intent(this@CalculatorActivity, LoginActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.taskbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                firebaseAuth!!.signOut()
                startActivity(Intent(this@CalculatorActivity, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        firebaseAuth = FirebaseAuth.getInstance()
        currentuser = firebaseAuth!!.currentUser
        editText1 = findViewById(R.id.edittext1)
        editText2 = findViewById(R.id.edittext2)
        buttonEqual = findViewById(R.id.operatorEquals)
        historyData = HistoryData()
        buttonHistory = findViewById(R.id.btnHistory)

        buttonEqual!!.setOnClickListener {
            if (editText2!!.length() != 0) {
                text = editText2!!.text.toString()
                expression = editText1!!.text.toString() + text
                editText1!!.setText(expression)
            }
            editText2!!.setText("")
            if (expression.isEmpty()) {
                expression = "0.0"
            } else {
                resultQ = solve(expression)

                count += 1
                if (count in 0..10) {
                    val p = HistoryData(expression, resultQ, count)
                    FirebaseDatabase.getInstance().getReference("History")
                        .child(firebaseAuth!!.uid!!)
                        .child(count.toString()).setValue(p).addOnCompleteListener {
                        }.addOnFailureListener { e ->
                            Toast.makeText(
                                this@CalculatorActivity,
                                "Error Occured : " + e.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            editText1!!.setText("" + resultQ)
        }

        buttonHistory!!.setOnClickListener {
            startActivity(
                Intent(
                    this@CalculatorActivity,
                    ViewHistoryActivity::class.java
                )
            )
        }
    }

    @SuppressLint("SetTextI18n")
    fun onClick(view: View) {
        when (view.id) {
            R.id.num0 -> editText2!!.setText(editText2!!.text.toString() + "0")
            R.id.num1 -> editText2!!.setText(editText2!!.text.toString() + "1")
            R.id.num2 -> editText2!!.setText(editText2!!.text.toString() + "2")
            R.id.num3 -> editText2!!.setText(editText2!!.text.toString() + "3")
            R.id.num4 -> editText2!!.setText(editText2!!.text.toString() + "4")
            R.id.num5 -> editText2!!.setText(editText2!!.text.toString() + "5")
            R.id.num6 -> editText2!!.setText(editText2!!.text.toString() + "6")
            R.id.num7 -> editText2!!.setText(editText2!!.text.toString() + "7")
            R.id.num8 -> editText2!!.setText(editText2!!.text.toString() + "8")
            R.id.num9 -> editText2!!.setText(editText2!!.text.toString() + "9")
            R.id.clearText -> {
                editText1!!.setText("")
                editText1!!.setText("")
                expression = ""
            }
            R.id.backspace -> {
                text = editText2!!.text.toString()
                if (text.isNotEmpty()) {
                    editText2!!.setText(text.substring(0, text.length - 1))
                }
            }
            R.id.operatorPlus -> operationSelected("+")
            R.id.operatorSub -> operationSelected("-")
            R.id.operatorMul -> operationSelected("*")
            R.id.operatorDiv -> operationSelected("/")
        }
    }

    @SuppressLint("SetTextI18n")
    fun operationSelected(operator: String) {
        if (editText2!!.length() != 0) {
            val text = editText2!!.text.toString()
            editText1!!.setText(editText1!!.text.toString() + text + operator)
            editText2!!.setText("")
        } else {
            val text = editText1!!.text.toString()
            if (text.isNotEmpty()) {
                editText1!!.setText(text + operator)
            }
        }
    }

    companion object {
        fun solve(expression: String): Int {
            val tokens = expression.toCharArray()
            val numbers = Stack<Int>()
            val operators = Stack<Char>()
            var i = 0
            while (i < tokens.size) {
                if (tokens[i] == ' ') {
                    i++
                    continue
                }
                if (tokens[i] in '0'..'9') {
                    val stringBuffer = StringBuffer()
                    while (i < tokens.size && tokens[i] >= '0' && tokens[i] <= '9') stringBuffer.append(
                        tokens[i++]
                    )
                    numbers.push(stringBuffer.toString().toInt())
                    i--
                } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                    while (!operators.empty() && hasPrecedence(tokens[i], operators.peek())) {
                        numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()))
                    }
                    operators.push(tokens[i])
                }
                i++
            }
            while (!operators.empty()) {
                numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()))
            }
            return numbers.pop()
        }

        private fun hasPrecedence(
            op1: Char, op2: Char
        ): Boolean {
            return if (op1 == '*' && (op2 == '/' || op2 == '-' || op1 == '+')) false else if (op1 == '+' && (op2 == '/' || op2 == '-')) false else !(op1 == '/' && op2 == '-')
        }

        fun applyOperation(op: Char, b: Int, a: Int): Int {
            when (op) {
                '+' -> return a + b
                '-' -> return a - b
                '*' -> return a * b
                '/' -> {
                    if (b == 0) throw UnsupportedOperationException("Cannot divide by zero")
                    return a / b
                }
            }
            return 0
        }
    }
}