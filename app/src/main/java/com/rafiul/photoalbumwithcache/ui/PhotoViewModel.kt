package com.rafiul.photoalbumwithcache.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafiul.photoalbumwithcache.base.ApiState
import com.rafiul.photoalbumwithcache.model.response.ResponsePhotoItem
import com.rafiul.photoalbumwithcache.repository.PhotoRepository
import com.rafiul.photoalbumwithcache.repository.PhotoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(private val photoRepository: PhotoRepository): ViewModel()  {

    private val _photos = MutableStateFlow<ApiState<List<ResponsePhotoItem>>>(ApiState.Loading)
    val photos: StateFlow<ApiState<List<ResponsePhotoItem>>>
        get() = _photos

    init {
        fetchAllPhotos()
    }

    private fun fetchAllPhotos() {
        viewModelScope.launch {
            photoRepository.getAllPhotosFromRepo().collect { state ->
                _photos.value = state
            }
        }
    }
}