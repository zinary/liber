package com.zinary.liber.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.zinary.liber.R
import com.zinary.liber.constants.Constants
import com.zinary.liber.constants.Constants.IMAGE_INDEX
import com.zinary.liber.constants.Constants.IMAGE_URLS
import com.zinary.liber.databinding.ActivityImagePreviewBinding
import com.zinary.liber.utils.Utils

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

        binding.toolBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolBar.setNavigationOnClickListener {
            finish()
        }

        setSupportActionBar(binding.toolBar)


        setCurrentPage(currentIndex)
        binding.imageViewPager.adapter = ImagesPreviewAdapter(this, imageUrls)
        binding.imageViewPager.currentItem = currentIndex
        binding.imageViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentPage(position)
            }
        })
    }

    private fun setCurrentPage(position: Int) {
        binding.toolBar.title = "${position + 1} / ${imageUrls.count()}"
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.image_preview_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.download -> {
                val imagePath: String =
                    Constants.BASE_IMAGE_URL_ORIGINAL + imageUrls[binding.imageViewPager.currentItem]
                Utils.downloadFile(imagePath)
                Toast.makeText(this, "downloading", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}