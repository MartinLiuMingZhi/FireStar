package com.example.firestar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.firestar.databinding.ItemMessageBinding
import com.example.firestar.model.MessageItem

class MessageAdapter(private val items: List<MessageItem>): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = items[position]
        Glide.with(holder.itemView.context).load(message.avatar).apply(
            RequestOptions().transform(
                RoundedCorners(64)
            )).into(holder.binding.roundAvatar)
        holder.binding.messages.text = message.message
        holder.binding.username.text = message.username
        holder.binding.time.text = message.time
    }


}