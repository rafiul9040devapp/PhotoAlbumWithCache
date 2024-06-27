package com.rafiul.photoalbumwithcache.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rafiul.photoalbumwithcache.databinding.ItemPhotoViewholderBinding
import com.rafiul.photoalbumwithcache.model.response.ResponsePhotoItem

class PhotoAdapter :
    ListAdapter<ResponsePhotoItem, PhotoAdapter.PhotoViewHolder>(PhotoDiffCallBack()) {

    inner class PhotoViewHolder(private val binding: ItemPhotoViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photoItem: ResponsePhotoItem) {
            with(binding) {
                title.text = photoItem.title
                Glide.with(itemView.context)
                    .load(photoItem.url)
                    .into(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPhotoViewholderBinding.inflate(layoutInflater, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) =
        holder.bind(getItem(position))

}

class PhotoDiffCallBack : DiffUtil.ItemCallback<ResponsePhotoItem>() {
    override fun areItemsTheSame(
        oldItem: ResponsePhotoItem,
        newItem: ResponsePhotoItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ResponsePhotoItem,
        newItem: ResponsePhotoItem
    ): Boolean {
        return oldItem == newItem
    }


}