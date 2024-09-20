package com.example.firestar.ui.bilibili

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.firestar.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BilibiliFragment : Fragment() {

    companion object {
        fun newInstance() = BilibiliFragment()
    }

    private val viewModel: BilibiliViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_bilibili, container, false)

        // 获取 NavController 绑定到 NavHostFragment
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_bilibili_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        // 绑定 BottomNavigationView 和 NavController
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bilibili_navigation)
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