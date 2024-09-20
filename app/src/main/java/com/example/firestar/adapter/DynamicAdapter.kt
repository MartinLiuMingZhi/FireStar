package com.example.firestar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firestar.databinding.ItemDynamicBinding
import com.example.firestar.model.DynamicItem

class DynamicAdapter(private val items: List<DynamicItem>,
                     private val onItemClick: (DynamicItem) -> Unit
):RecyclerView.Adapter<DynamicAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemDynamicBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDynamicBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dynamic = items[position]
        holder.binding.itemDynamicText.text = dynamic.title
        holder.binding.itemDynamicImage.setImageResource(dynamic.imageResId)

        // 设置点击事件
        holder.binding.root.setOnClickListener {
            onItemClick(dynamic) // 调用回调，传递被点击的项
        }
    }

}