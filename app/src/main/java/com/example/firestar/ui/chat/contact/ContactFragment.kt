package com.example.firestar.ui.chat.contact

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firestar.R
import com.example.firestar.adapter.ContactAdapter

class ContactFragment : Fragment() {

    companion object {
        fun newInstance() = ContactFragment()
    }

    private val viewModel: ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_contact, container, false)

        //获取SwipeRefreshLayout
        val swipeRefreshLayout = view.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipe_refresh_layout)

        // 获取 RecyclerView 并设置布局管理器
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_contact)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 初始化适配器并设置到 RecyclerView
        val adapter = ContactAdapter(emptyList())  // 初始数据为空
        recyclerView.adapter = adapter

        // 观察 ViewModel 中的数据变化
        viewModel.items.observe(viewLifecycleOwner) { items ->
            // 当数据变化时，更新 RecyclerView 的适配器
            recyclerView.adapter = ContactAdapter(items)
            swipeRefreshLayout.isRefreshing = false
        }

        swipeRefreshLayout.setOnRefreshListener {
            // 在这里调用 ViewModel 的加载数据的方法
            viewModel.loadItems()
        }
        return view
    }
}