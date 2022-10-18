package com.bakharaalief.learnuikit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.error_btn).setOnClickListener {
            findViewById<TextInputLayout>(R.id.edit_text_error).error = "Error nih"
        }
    }
}