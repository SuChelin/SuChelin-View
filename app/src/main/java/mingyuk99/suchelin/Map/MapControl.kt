package mingyuk99.suchelin.Map

import android.app.ActionBar
import android.content.Context
import android.graphics.PointF
import android.util.Log
import android.view.Gravity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mingyuk99.suchelin.R

class MapControl {

    // map 사용자 인터페이스 설정
    fun setMapUI(naverMap: NaverMap){
        val uiSetting = naverMap.uiSettings

        // Map type
        naverMap.mapType = NaverMap.MapType.Basic

        // Map layout
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)

        // 실내 지도
        naverMap.isIndoorEnabled = true

        // Control setting
        uiSetting.apply {
            isCompassEnabled = true
            isScaleBarEnabled = false
            isIndoorLevelPickerEnabled = true
            isZoomControlEnabled = true
            isLocationButtonEnabled = false
        }

        // logo location
        uiSetting.logoGravity = (Gravity.TOP or Gravity.LEFT)
        uiSetting.isLogoClickEnabled = true
        uiSetting.setLogoMargin(20,20,0,0)

        // gesture setting
        uiSetting.apply {
            isScrollGesturesEnabled = true
            isZoomGesturesEnabled = true
            isTiltGesturesEnabled = false
            isRotateGesturesEnabled = true
            isStopGesturesEnabled = false
        }

        // 카메라 설정
        naverMap.cameraPosition = CameraPosition(
            LatLng(37.214225, 126.978819),
            17.0
        )
    }

    fun setMaker(naverMap: NaverMap, context: Context){
        val job = CoroutineScope(Dispatchers.Main).launch {


            val marker = Marker()
            val marker2 = Marker()

            marker.position = LatLng(37.214225, 126.978819)
            marker2.position = LatLng(37.214523, 126.978058)

            marker.map = naverMap
            marker2.map = naverMap

        }
    }

}