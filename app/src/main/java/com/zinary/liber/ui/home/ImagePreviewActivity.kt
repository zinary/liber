package com.zinary.liber.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.zinary.liber.constants.Constants.IMAGE_INDEX
import com.zinary.liber.constants.Constants.IMAGE_URLS
import com.zinary.liber.databinding.ActivityImagePreviewBinding

class ImagePreviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityImagePreviewBinding

    private var imageUrls = arrayListOf<String>()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.extras?.let { args ->
            imageUrls = args.getStringArrayList(IMAGE_URLS) ?: arrayListOf()
            currentIndex = args.getInt(IMAGE_INDEX)
        }
        binding.closeButton.setOnClickListener {
            finish()
        }

        setCurrentPage(currentIndex)
        binding.imageViewPager.adapter = ImagesPreviewAdapter(this, imageUrls)
        binding.imageViewPager.currentItem = currentIndex
        binding.imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentPage(position)
            }
        })
        }

        private fun setCurrentPage(position: Int) {
            binding.count.text = "${position + 1} / ${imageUrls.count()}"
        }

    }