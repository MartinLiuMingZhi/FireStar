package com.example.firestar.ui.chat

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.firestar.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChatFragment : Fragment() {

    companion object {
        fun newInstance() = ChatFragment()
    }

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // 获取 NavController 绑定到 NavHostFragment
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        // 绑定 BottomNavigationView 和 NavController
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController)
        } else {
            // 处理 navController 为空的情况
            // 例如：log error 或者展示错误信息
            Log.e("ChatFragment","navController为空")
        }

        return view
    }
}