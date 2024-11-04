package com.example.firestar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firestar.databinding.ItemCommentBinding
import com.example.firestar.model.CommentItem

class CommentAdapter(private val comments: List<CommentItem>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.binding.tvUsername.text = String.format("%s:", comment.username)
        holder.binding.tvComment.text = comment.content
    }

    override fun getItemCount(): Int = comments.size
}
