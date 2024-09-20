package com.example.firestar.ui.chat.dynamic

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firestar.R
import com.example.firestar.adapter.DynamicAdapter
import com.example.firestar.model.DynamicItem

class DynamicFragment : Fragment() {

    companion object {
        fun newInstance() = DynamicFragment()
    }

    private val viewModel: DynamicViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DynamicAdapter
    private lateinit var dynamicItems: List<DynamicItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_dynamic, container, false)

        // 初始化 RecyclerView
        recyclerView = view.findViewById(R.id.dynamic_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //准备静态数据
        dynamicItems = listOf(
            DynamicItem(1,R.drawable.ic_dynamic, "好友动态"),
            DynamicItem(2,R.drawable.ic_sports, "运动"),
            DynamicItem(3,R.drawable.ic_game, "游戏"),
            DynamicItem(4,R.drawable.ic_movie, "电影"),
            DynamicItem(5,R.drawable.ic_travel, "旅行")
        )

        // 初始化 Adapter
        adapter = DynamicAdapter(dynamicItems){ dynamicItem ->
            when (dynamicItem.id) {
                1 -> {

                }
                2 -> {

                }
                3 -> {

                }
                // 其他情况
            }

        }

        // 绑定 Adapter
        recyclerView.adapter = adapter

        return view
    }
}