package com.example.firestar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amap.api.location.AMapLocation


//创建一个共享的 ViewModel 来保存位置数据
class LocationViewModel : ViewModel() {
    private val _location = MutableLiveData<AMapLocation>()
    val location: LiveData<AMapLocation> = _location

    fun setLocation(location: AMapLocation) {
        _location.value = location
    }
}