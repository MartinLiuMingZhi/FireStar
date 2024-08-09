package com.example.firestar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firestar.R
import com.example.firestar.data.Messages

class MessageListAdapter(val messageList: List<Messages>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2
    inner class SendViewHolder(view: View): RecyclerView.ViewHolder(view){
        val sendMessage: TextView = view.findViewById(R.id.text_gchat_message_me)
    }

    inner class ReceiveViewHolder(view: View): RecyclerView.ViewHolder(view){
        val receiveMessage: TextView = view.findViewById(R.id.text_gchat_message_other)
    }

    override fun getItemViewType(position: Int): Int {
        val msg = messageList[position]
        return msg.type
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MESSAGE_SENT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_send_item,parent,false)
            SendViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_receive_item,parent,false)
            ReceiveViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messageList[position]
        when(holder){
            is SendViewHolder -> { holder.sendMessage.text = msg.context}
            is ReceiveViewHolder -> {holder.receiveMessage.text = msg.context}
        }
    }
}