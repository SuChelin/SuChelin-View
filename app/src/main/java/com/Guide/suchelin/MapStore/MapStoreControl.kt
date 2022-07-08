package com.Guide.suchelin.MapStore

import com.Guide.suchelin.Map.MapControl
import com.Guide.suchelin.R
import android.content.Context
import android.view.Gravity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class MapStoreControl(
    val context: Context,
    val latitude: Double,
    val longitude: Double,
    val storeName: String
) {
    private var mInfoWindow = InfoWindow()

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
            isCompassEnabled = false
            isScaleBarEnabled = false
            isIndoorLevelPickerEnabled = true
            isZoomControlEnabled = false
            isLocationButtonEnabled = false
        }

        // logo location
        uiSetting.logoGravity = (Gravity.TOP or Gravity.LEFT)
        uiSetting.isLogoClickEnabled = true
        uiSetting.setLogoMargin(20,20,0,0)

        // 카메라 설정
        naverMap.cameraPosition = CameraPosition(
            LatLng(latitude, longitude),
            17.0
        )

        // 마커표시
        val resource = R.drawable.premiumiconlocation1
        val markerIcon = OverlayImage.fromResource(resource)

        val superMarker = superMarkerSetting(naverMap, markerIcon)
        setInfoWindow(superMarker, storeName)
    }

    private fun superMarkerSetting(
        naverMap: NaverMap,
        markerIcon: OverlayImage
    ) : Marker {
        val marker = Marker().apply {
            position = LatLng(latitude, longitude)
            icon = markerIcon
            map = naverMap
            height = MapControl.MARKER_ICON_HEIGHT
            width = MapControl.MARKER_ICON_WEIGHT
        }

        return marker
    }

    // 마커 infoWindow 표시하기
    private fun setInfoWindow(marker: Marker, infoString: String){
        mInfoWindow.adapter = object : InfoWindow.DefaultTextAdapter(context) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return infoString
            }
        }
        mInfoWindow.open(marker)
    }
}