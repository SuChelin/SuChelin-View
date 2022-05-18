package Guide.suchelin.Map

import android.view.Gravity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import Guide.suchelin.R
import Guide.suchelin.DataSet

class MapControl {

    companion object{
        private const val MARKER_ICON_HEIGHT = 60
        private const val MARKER_ICON_WEIGHT = 60
    }

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
            LatLng(37.214225, 126.978819),
            17.0
        )
    }

    fun setMaker(naverMap: NaverMap,
                 superDataList: ArrayList<DataSet>,
                 fragment: MapsFragment
    ) {
        val job = CoroutineScope(Dispatchers.Main).launch {

            val markerIcon = OverlayImage.fromResource(R.drawable.ic_marker)

            superDataList.forEach { data ->
                val marker = superMarkerSetting(data, naverMap, markerIcon)

                marker.setOnClickListener {
                    fragment.setSuper(data)
                    true
                }
            }

            // 수원대학교  전문 표시
            Marker().apply {
                position = LatLng(37.214225, 126.978819)
                icon = markerIcon
                map = naverMap
                height = MARKER_ICON_HEIGHT
                width = MARKER_ICON_WEIGHT
            }
        }
    }

    private suspend fun superMarkerSetting(
        data: DataSet,
        naverMap: NaverMap,
        markerIcon: OverlayImage
    ) : Marker {
        val marker = Marker().apply {
            position = LatLng(data.latitude, data.longitude)
            icon = markerIcon
            map = naverMap
            height = MARKER_ICON_HEIGHT
            width = MARKER_ICON_WEIGHT
        }

        return marker
    }

    // 기본 홈(수원대학교 정문)으로 이동
    fun goHome(naverMap: NaverMap){
        val cameraUpdate = CameraUpdate.scrollTo(
            LatLng(37.214225, 126.978819)
        ).animate(CameraAnimation.Fly)

        naverMap.moveCamera(cameraUpdate)
    }
}