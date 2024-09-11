package com.example.firestar.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.example.firestar.databinding.FragmentMapBinding
import com.example.firestar.viewmodel.LocationViewModel


class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private var aMap: AMap? = null

    // 获取共享的 ViewModel 实例
    private val locationViewModel: LocationViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 初始化地图
        binding.mapView.onCreate(savedInstanceState) // 必须调用
        if (aMap == null){
            aMap = binding.mapView.map
            // 启用定位图标
            val myLocationStyle = MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.interval(3000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            aMap?.setMyLocationStyle(myLocationStyle) //设置定位蓝点的Style
            aMap?.isMyLocationEnabled = true
            aMap?.uiSettings?.isMyLocationButtonEnabled = true

            // 开启室内地图
            aMap?.showIndoorMap(true)


            // 地图控件设置
            val uiSettings = aMap?.uiSettings

            // 隐藏缩放按钮
            uiSettings?.isZoomControlsEnabled = false

            // 显示比例尺，默认不显示
            uiSettings?.isScaleControlsEnabled = true

        }

        // 观察位置信息变化
        locationViewModel.location.observe(viewLifecycleOwner) { location ->
            if (location != null) {
                // 更新地图显示当前位置
                val latLng = LatLng(location.latitude, location.longitude)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                aMap?.animateCamera(cameraUpdate)
            }
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
        _binding = null
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        binding.mapView.onDestroy()
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}