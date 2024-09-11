package com.example.firestar.ui.chat.message

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firestar.R
import com.example.firestar.adapter.MessageAdapter

class MessageFragment : Fragment() {

    companion object {
        fun newInstance() = MessageFragment()
    }

    private val viewModel: MessageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_message, container, false)

        // 获取 RecyclerView 并设置布局管理器
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_messages)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 初始化适配器并设置到 RecyclerView
        val adapter = MessageAdapter(emptyList())  // 初始数据为空
        recyclerView.adapter = adapter

//        // 观察 ViewModel 中的数据变化
//        viewModel.items.observe(viewLifecycleOwner) { items ->
//            // 当数据变化时，更新 RecyclerView 的适配器
//            recyclerView.adapter = MessageAdapter(items)
//        }

        return view
    }
}