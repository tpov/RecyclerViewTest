package com.tpov.testphoto

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.*
import android.text.style.*
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var textViewPicture: CustomTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initTextView(getString(R.string.input_text))

    }

    private fun initView() {
        textViewPicture = findViewById(R.id.customTextView)
    }

    private fun initTextView(text: String) {
        textViewPicture.setText(text)
    }
}