package mingyuk99.suchelin.Map

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.widget.LocationButtonView
import com.naver.maps.map.widget.ScaleBarView
import com.naver.maps.map.widget.ZoomControlView
import mingyuk99.suchelin.R
import mingyuk99.suchelin.config.BaseFragment
import mingyuk99.suchelin.dataSet
import mingyuk99.suchelin.databinding.FragmentMapBinding

class MapsFragment : BaseFragment<FragmentMapBinding>(
    FragmentMapBinding::bind,
    R.layout.fragment_map
){

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 10002
    }
    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private var naverMap : NaverMap? = null
    private var mapControl = MapControl()

    // 현재 위치 버튼 사용하기
    private var locationFlag = false
    private lateinit var mapLocationButton : ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        val mapDataList = arrayListOf(
            dataSet("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTAGJ28R7vVAaVouy37LhbNlptqTJQwl208Vg&usqp=CAU","던킨도너츠 수원대점","바바리안 도넛",37.214523,126.978058),
            dataSet("https://search.pstatic.net/common/?autoRotate=true&quality=95&type=f180_180&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210302_125%2F161464487124061agC_JPEG%2FK15utTFWXeuNEny1JMXiV57W.jpg","할리스 수원대학교점","토피넛 라떼",37.214367, 126.978968),
        )

        // 뷰 설정
        mapLocationButton = view.findViewById(R.id.mapLocationButton)

        // 권한 설정하기
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        mapView.getMapAsync { map ->
            naverMap = map
            naverMap ?: return@getMapAsync

            // 현재 위치 활성화
            naverMap!!.locationSource = locationSource

            // 지도 설정
            mapControl.setMapUI(naverMap!!)
            mapControl.setMaker(naverMap!!, mapDataList, this@MapsFragment)

            // 지도 클릭 이벤트
            naverMap?.setOnMapClickListener { pointF, latLng ->

                if(binding.mapSuperParent.visibility == View.VISIBLE){
                    binding.mapSuperParent.visibility = View.GONE
                }
            }

            // 위치 추적 설정
            mapLocationButton.setOnClickListener{
                naverMap ?: return@setOnClickListener

                locationFlag = if(!locationFlag){
                    naverMap?.locationTrackingMode = LocationTrackingMode.Follow
                    true
                } else {
                    naverMap?.locationTrackingMode = LocationTrackingMode.None
                    // 홈으로 이동
                    mapControl.goHome(naverMap!!)
                    false
                }
            }
        }

    }

    fun setSuper(data: dataSet){
        binding.mapSuperParent.visibility = View.VISIBLE
        Glide.with(binding.mapSuperParent)
            .load(data.imageUrl)
            .centerCrop()
            .into(binding.mapSuperImageView)
        binding.mapSuperTitleTextView.text = data.name
        binding.mapSuperDetailTextView.text = data.detail
        binding.mapSuperDetailTextView
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap?.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}
