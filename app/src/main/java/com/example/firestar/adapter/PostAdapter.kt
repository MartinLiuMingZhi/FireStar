package com.example.firestar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.firestar.databinding.ItemPostBinding
import com.example.firestar.model.PostItem

class PostAdapter(private val items: List<PostItem>):RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPostBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = items[position]
        Glide.with(holder.itemView.context).load(post.avatar).apply ( RequestOptions().transform(
            RoundedCorners(64)
        ) ).into(holder.binding.imageAvatar)
        holder.binding.textUsername.text = post.username
        holder.binding.textTime.text = post.time
        holder.binding.textContent.text = post.content
        holder.binding.listLikeUsername.text = post.listLikeUsername

        //初始化内部评论列表CommentRecyclerView适配器
        holder.binding.commentRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.binding.commentRecyclerView.adapter = CommentAdapter(post.commentList)

    }


}