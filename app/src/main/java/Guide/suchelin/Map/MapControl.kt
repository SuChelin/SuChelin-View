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
import Guide.suchelin.DataClass.StoreDataClassMap
import Guide.suchelin.StoreDetail.StoreDetailActivity
import Guide.suchelin.databinding.FragmentMapBinding
import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.naver.maps.map.overlay.InfoWindow

class MapControl {

    companion object{
        private const val MARKER_ICON_HEIGHT = 60
        private const val MARKER_ICON_WEIGHT = 60
        private const val currentVisibleItemPx = 70
        private const val pageTranslationX = 150
    }

    // marker 와 viewpager2의 리스트 연결
    private var markerNumber = 0
    private var viewpagerNumber = 0
    private var mSuperDataList = arrayListOf<StoreDataClassMap>(
        StoreDataClassMap(0, "", "수원대학교 정문", "", 37.214185, 126.978792)
    )
    private var mBinding : FragmentMapBinding? = null
    private var mFragment : MapsFragment? = null
    private var mMarkerList = ArrayList<Marker>()
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
            LatLng(37.214200, 126.978750),
            17.0
        )
    }

    fun setMarkerAndViewpager(
        naverMap: NaverMap,
        superDataList: ArrayList<StoreDataClassMap>,
        binding: FragmentMapBinding,
        fragment: MapsFragment
    ) {
        mBinding = binding
        mFragment = fragment

        // 마커 설정
        val job = CoroutineScope(Dispatchers.Main).launch {
            val resource = R.drawable.premiumiconlocation1
            val markerIconStart = OverlayImage.fromResource(R.drawable.home)
            val markerIcon = OverlayImage.fromResource(resource)

            // 수원대학교 정문 표시
            mMarkerList.add(Marker().apply {
                position = LatLng(37.214185, 126.978792)
                icon = markerIconStart
                map = naverMap
                height = MARKER_ICON_HEIGHT
                width = MARKER_ICON_WEIGHT
            })

            mMarkerList[0].setOnClickListener {
                setStoreSelect(0, true, naverMap)
                true
            }

            // 나머지 마커 표시
            superDataList.forEachIndexed { index, data ->
                val marker = superMarkerSetting(data, naverMap, markerIcon)

                // 마커를 클릭했을 경우 marker 와 viewpager 설정
                marker.setOnClickListener {
                    Log.d("marker", "마커 : data : ${data} , index : $index")
                    setStoreSelect(data.id, true, naverMap)
                    true
                }

                mSuperDataList.add(data)
                mMarkerList.add(marker)
            }

            // info 창 초기 설정
            setInfoWindow(0, "수원대학교 정문")

            // Viewpager2 설정
            binding.mapViewpager2.adapter = MapStoreAdapter(mSuperDataList, this@MapControl)
            binding.mapViewpager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.mapViewpager2.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        // 새롭게 페이지 이동했을 경우 marker 와 viewpager 설정
                        setStoreSelect(position, false, naverMap)
                    }
                }
            )

            // Viewpager2 미리보기
            binding.mapViewpager2.addItemDecoration(object: RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.right = currentVisibleItemPx
                    outRect.left = currentVisibleItemPx
                }
            })
            binding.mapViewpager2.offscreenPageLimit = 1

            binding.mapViewpager2.setPageTransformer { page, position ->
                page.translationX = -pageTranslationX * ( position)
            }
        }


    }

    // 가게 선택했을 경우
    // 1. viewpager2 의 scroll 을 이용했을 경우
    // 2. 지도에 있는 marker 를 클릭했을 경우
    fun setStoreSelect(number: Int, viewpagerFlag: Boolean, naverMap: NaverMap){
        if(viewpagerNumber == number) return

        if(viewpagerFlag){
            // 지도에 있는 marker 를 클랙했을 경우
            // viewpager2 움직이기
            mBinding?.mapViewpager2?.setCurrentItem(number, true)
        }

        markerNumber = number
        viewpagerNumber = number

        moveMap(naverMap, LatLng(mSuperDataList!![markerNumber].latitude, mSuperDataList!![markerNumber].longitude))

        // 마커 infoWindow 표시하기
        setInfoWindow(markerNumber, mSuperDataList!![markerNumber].name)

    }

    // 마커 infoWindow 표시하기
    private fun setInfoWindow(markerIndex: Int, infoString: String){
        mFragment ?: return
        mInfoWindow.adapter = object : InfoWindow.DefaultTextAdapter(mFragment!!.requireContext()) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return infoString
            }
        }
        mInfoWindow.open(mMarkerList[markerIndex])
    }

    // 지도 움직이기
    private fun moveMap(naverMap: NaverMap, latLng: LatLng){
        naverMap.moveCamera(
            CameraUpdate.scrollTo(latLng).animate(CameraAnimation.Fly)
        )
    }

    private suspend fun superMarkerSetting(
        data: StoreDataClassMap,
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

    // StoreDetailActivity 로 넘어가기 , MapStoreAdapter 에서 사용
    fun startStoreDetailActivity(storeId: Int){
        mFragment?.let {
            it.startActivity(
                Intent(it.requireContext(), StoreDetailActivity::class.java).apply {
                    putExtra("StoreName", storeId)
                }
            )
        }
    }

    // 기본 홈(수원대학교 정문)으로 이동
    fun goHome(naverMap: NaverMap){
        val cameraUpdate = CameraUpdate.scrollTo(
            LatLng(37.214225, 126.978819)
        ).animate(CameraAnimation.Fly)

        naverMap.moveCamera(cameraUpdate)
    }
}