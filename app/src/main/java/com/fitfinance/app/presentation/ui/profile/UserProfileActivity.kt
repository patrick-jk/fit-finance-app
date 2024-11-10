package com.fitfinance.app.presentation.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fitfinance.app.databinding.ActivityAboutUsBinding

class UserProfileActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAboutUsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}