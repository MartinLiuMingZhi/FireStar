package com.example.firestar.ui.chat.contact

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firestar.model.ContactItem
import com.example.firestar.network.NetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ViewModel 用于管理动态项的数据
class ContactViewModel : ViewModel() {

    private val TAG = "ContactViewModel"

    // MutableLiveData 用于存储和更新数据
    private val _items = MutableLiveData<List<ContactItem>>()

    // 公开 LiveData 以便 Fragment 观察
    val items: LiveData<List<ContactItem>> = _items

    init {
        // 初始化数据
        loadItems()
    }

    // 使用 Coroutine 异步加载数据
    fun loadItems() {
        viewModelScope.launch {
            try {
                // 切换到 IO 线程进行网络请求
                val response = withContext(Dispatchers.IO) { NetWork.getUsers() }

                if (response.code == "200") {
                    val list = response.data ?: emptyList()
                    // 确保在主线程上更新 LiveData
                    _items.postValue(list)
                } else {
                    Log.e(TAG, "getUsers请求失败，错误代码: ${response.code}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "getUsers请求异常: ${e.message}", e)
            }
        }
    }

}