package com.fitfinance.app.presentation.ui.photogallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fitfinance.app.databinding.ActivityContactBinding

class PhotoGalleryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityContactBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}