package com.example.firestar.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firestar.R
import com.example.firestar.databinding.FragmentHomeBinding
import com.google.android.material.card.MaterialCardView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        // 找到包含的布局，并获取其中的 chat 控件
        val includedLayout = binding.homeMenu.findViewById<View>(R.id.included_layout) // 这里的 included_layout 是在 <include> 标签中定义的 layout id
        val chatCardView: MaterialCardView = includedLayout.findViewById(R.id.chat)

        // 为 chatCardView 添加点击事件
        chatCardView.setOnClickListener {
//            Toast.makeText(context, "Chat clicked!", Toast.LENGTH_SHORT).show()

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}