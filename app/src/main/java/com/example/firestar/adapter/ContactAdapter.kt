package com.example.firestar.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.firestar.databinding.ItemPersonBinding
import com.example.firestar.model.ContactItem

class ContactAdapter(private val items: List<ContactItem>):RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPersonBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val user = items[position]
        Glide.with(holder.itemView.context).load(user.avatar).apply(
            RequestOptions().transform(
                RoundedCorners(64)
            )).into(holder.binding.roundAvatar)
        holder.binding.email.text = user.email
        holder.binding.username.text = user.username
        Log.d("ContactAdapter", "onBindViewHolder:${user.username} ${user.status}")
        if (user.status == 1L){
            holder.binding.statusText.text = "在线"
            holder.binding.dot.isSelected = true
        }else{
            holder.binding.statusText.text = "离线"
            holder.binding.dot.isSelected = false
        }

        holder.binding.itemPerson.setOnClickListener {

        }
    }


}