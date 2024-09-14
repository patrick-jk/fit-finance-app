package com.fitfinance.app.presentation.ui.aboutus

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fitfinance.app.R
import com.fitfinance.app.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAboutUsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}