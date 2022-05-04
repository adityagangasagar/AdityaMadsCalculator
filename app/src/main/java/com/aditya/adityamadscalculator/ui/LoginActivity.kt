package com.aditya.adityamadscalculator.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aditya.adityamadscalculator.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var buttonLogin: Button
    private lateinit var edittextUsername: TextInputEditText
    private lateinit var edittextPassword: TextInputEditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        buttonLogin = findViewById(R.id.btnLogin)
        edittextUsername = findViewById(R.id.edEmail)
        edittextPassword = findViewById(R.id.edPassword)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this@LoginActivity)

        buttonLogin.setOnClickListener {
            val userEmail = edittextUsername.text.toString()
            val userPassword: String = edittextPassword.text.toString()

            if (userEmail.isEmpty()) {
                edittextUsername.error = "Please enter username/email id"
                edittextUsername.requestFocus()
            } else if (userPassword.isEmpty()) {
                edittextPassword.error = "Please enter the password"
                edittextPassword.requestFocus()
            } else if (!(userEmail.isEmpty() && userPassword.isEmpty())) {
                progressDialog.setMessage("PLease wait...")
                progressDialog.show()

                firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(
                        this@LoginActivity
                    ) { task ->
                        if (!task.isSuccessful) {
                            progressDialog.dismiss()
                            Toast.makeText(
                                this@LoginActivity,
                                "Please check your credentials",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            sendUserToCalculatorActivity()
                            progressDialog.dismiss()
                        }
                    }.addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@LoginActivity,
                            "Error : " + e.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
            }
        }
    }

    private fun sendUserToCalculatorActivity() {
        val intent = Intent(this@LoginActivity, CalculatorActivity::class.java)
        startActivity(intent)
        finish()
    }
}