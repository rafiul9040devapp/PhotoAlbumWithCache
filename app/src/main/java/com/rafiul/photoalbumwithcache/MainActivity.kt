package com.rafiul.photoalbumwithcache

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafiul.photoalbumwithcache.adapter.PhotoAdapter
import com.rafiul.photoalbumwithcache.base.ApiState
import com.rafiul.photoalbumwithcache.databinding.ActivityMainBinding
import com.rafiul.photoalbumwithcache.ui.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<PhotoViewModel>()
    private lateinit var photoAdapter: PhotoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observePhotos()
    }

    private fun setupRecyclerView() {
        photoAdapter = PhotoAdapter()
        binding.photoRec.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = photoAdapter
        }
    }

    private fun observePhotos() {
        lifecycleScope.launch {
            viewModel.photos.collect { apiState ->
                when (apiState) {
                    is ApiState.Loading -> {
                        // Show progress indicator
                        binding.progress.visibility = android.view.View.VISIBLE
                    }
                    is ApiState.Success -> {
                        // Hide progress indicator
                        binding.progress.visibility = android.view.View.GONE
                        // Update RecyclerView with the photos
                        photoAdapter.submitList(apiState.data)
                    }
                    is ApiState.Failure -> {
                        // Hide progress indicator
                        binding.progress.visibility = android.view.View.GONE
                        binding.photoRec.visibility = android.view.View.GONE
                        // Show error message
                        Toast.makeText(this@MainActivity, "Error: ${apiState.message}", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }
}