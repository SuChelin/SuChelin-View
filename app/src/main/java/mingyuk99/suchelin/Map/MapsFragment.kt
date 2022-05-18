package mingyuk99.suchelin.Map

import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.util.FusedLocationSource
import mingyuk99.suchelin.R
import mingyuk99.suchelin.SuperDetail.SuperDetailActivity
import mingyuk99.suchelin.config.BaseFragment
import mingyuk99.suchelin.dataSet
import mingyuk99.suchelin.databinding.FragmentMapBinding

class MapsFragment : BaseFragment<FragmentMapBinding>(
    FragmentMapBinding::bind,
    R.layout.fragment_map
) {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 10002
    }

    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private var naverMap: NaverMap? = null
    private var mapControl = MapControl()

    // 현재 위치 버튼 사용하기
    private var locationFlag = false
    private lateinit var locationManager: LocationManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        val mapDataList = arrayListOf(
            dataSet(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTAGJ28R7vVAaVouy37LhbNlptqTJQwl208Vg&usqp=CAU",
                "던킨도너츠 수원대점",
                "바바리안 도넛",
                37.214523,
                126.978058
            ),
            dataSet(
                "https://search.pstatic.net/common/?autoRotate=true&quality=95&type=f180_180&src=https%3A%2F%2Fldb-phinf.pstatic.net%2F20210302_125%2F161464487124061agC_JPEG%2FK15utTFWXeuNEny1JMXiV57W.jpg",
                "할리스 수원대학교점",
                "토피넛 라떼",
                37.214367,
                126.978968
            ),
        )

        // 권한 설정하기
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        // 위치 권한 설정하기
        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager

        // 지도 가져오기
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

                if (binding.mapSuperParent.visibility == View.VISIBLE) {
                    binding.mapSuperParent.visibility = View.GONE
                }
            }

            // 위치 추적 설정
            binding.mapLocationButton.setOnClickListener {
                naverMap ?: return@setOnClickListener

                // 현재 위치 기능 활성화 상태 확인
                if (!locationManager.isLocationEnabled) {
                    Toast.makeText(requireContext(), "현재 위치 기능을 활성화해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }
                locationFlag = if (!locationFlag) {
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

        // 미리보기 설정하면 superDetailActivity 로 넘어가기
        binding.mapSuperParent.setOnClickListener {
            val intent = Intent(requireContext(), SuperDetailActivity::class.java).apply {
                putExtra("SuperName", binding.mapSuperParent.tag as String)
            }
            startActivity(intent)
        }
    }

    // 미리보기 설정
    fun setSuper(data: dataSet) {
        binding.mapSuperParent.tag = data.name
        binding.mapSuperParent.visibility = View.VISIBLE
        Glide.with(binding.mapSuperParent)
            .load(data.imageUrl)
            .centerCrop()
            .into(binding.mapSuperImageView)
        binding.mapSuperTitleTextView.text = data.name
        binding.mapSuperDetailTextView.text = data.detail
        binding.mapSuperDetailTextView
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
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
