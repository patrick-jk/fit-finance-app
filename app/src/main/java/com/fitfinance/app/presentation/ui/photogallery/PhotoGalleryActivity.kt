package com.fitfinance.app.presentation.ui.photogallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fitfinance.app.databinding.ActivityPhotoGalleryBinding
import com.squareup.picasso.Picasso

class PhotoGalleryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPhotoGalleryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUi()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupUi() {
        val picasso = Picasso.get()
        picasso.load("https://www.theofficepass.com/toppings/wp-content/uploads/2022/09/business-and-finance.jpg")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(binding.ivPhotoGallery1)

        picasso.load("https://www.theofficepass.com/toppings/wp-content/uploads/2022/09/Tips-for-Managing-Small-Business-Finances-595x373.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(binding.ivPhotoGallery2)

        picasso.load("https://bb.everydata.com/hs-fs/hubfs/Untitled%20design%20(67).png?width=1006&height=587&name=Untitled%20design%20(67).png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(binding.ivPhotoGallery3)

        picasso.load("https://www.crefisa.com.br/wp-content/uploads/2023/11/cropped-logo-512x512-1-1.png")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(binding.ivPhotoGallery4)

        picasso.load("https://miro.medium.com/v2/resize:fit:720/format:webp/1*lJoevcmt5HHTWbaz0u-s6w.jpeg")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(binding.ivPhotoGallery5)
    }
}